package ca.wescook.nutrition.utility;

import ca.wescook.nutrition.capabilities.INutrientManager;
import ca.wescook.nutrition.network.Sync;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import org.apache.commons.lang3.math.NumberUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChatCommand extends CommandBase {
	@CapabilityInject(INutrientManager.class)
	private static final Capability<INutrientManager> NUTRITION_CAPABILITY = null;

	private final List<String> playerSubCommands = Arrays.asList("get", "set", "add", "subtract", "reset"); // Suggest player names following these subcommands
	private final String helpString = "/nutrition <get/set/add/subtract/reset/reload> <player> <nutrient> <value>";
	private enum actions {SET, ADD, SUBTRACT}

	@Override
	public String getName() {
		return "nutrition";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return helpString;
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		if (args.length == 1) { // Sub-commands list
			return getListOfStringsMatchingLastWord(args, Arrays.asList("get", "set", "add", "subtract", "reset", "reload"));
		}
		else if (args.length == 2 && playerSubCommands.contains(args[0])) { // Player list/reload command
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		}
		else if (args.length == 3) { // Nutrients list
			List<String> nutrientList = new ArrayList<>();
			for (Nutrient nutrient : NutrientList.get())
				nutrientList.add(nutrient.name);
			return getListOfStringsMatchingLastWord(args, nutrientList);
		}

		// Default
		return Collections.emptyList();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// Get player
		EntityPlayerMP player = null;
		if (args.length > 0 && playerSubCommands.contains(args[0]))
			player = CommandBase.getPlayer(server, sender, args[1]);

		// Which sub-command to execute
		if (args.length == 0 || args[0].equals("help"))
			commandHelp(sender);
		else if (args[0].equals("get"))
			commandGetNutrition(player, sender, args);
		else if (args[0].equals("set"))
			commandSetNutrition(player, sender, args, actions.SET);
		else if (args[0].equals("add"))
			commandSetNutrition(player, sender, args, actions.ADD);
		else if (args[0].equals("subtract"))
			commandSetNutrition(player, sender, args, actions.SUBTRACT);
		else if (args[0].equals("reset"))
			commandResetNutrition(player, sender, args);
		else if (args[0].equals("reload"))
			commandReload(server, sender);
	}

	private void commandHelp(ICommandSender sender) {
		sender.sendMessage(new TextComponentString(helpString));
	}

	private void commandReload(MinecraftServer server, ICommandSender sender) {
		DataImporter.reload();
		DataImporter.updatePlayerCapabilitiesOnServer(server);
		sender.sendMessage(new TextComponentString("Nutrients and effects reloaded"));
	}

	private void commandGetNutrition(EntityPlayer player, ICommandSender sender, String[] args) {
		// Write nutrient name and percentage to chat
		Nutrient nutrient = NutrientList.getByName(args[2]);
		if (nutrient != null) {
			Float nutrientValue = player.getCapability(NUTRITION_CAPABILITY, null).get(nutrient);
			sender.sendMessage(new TextComponentString(nutrient.name + ": " + String.format("%.2f", nutrientValue) + "%"));
		}
		else // Write error message
			sender.sendMessage(new TextComponentString("'" + args[2] + "' is not a valid nutrient."));
	}

	// Used to set, add, and subtract nutrients (defined under actions)
	private void commandSetNutrition(EntityPlayer player, ICommandSender sender, String[] args, actions action) {
		// Sanity checking
		if (!validNumber(sender, args[3]))
			return;

		// Set nutrient value and output
		Nutrient nutrient = NutrientList.getByName(args[2]);
		if (nutrient != null) {
			// Update nutrition based on action type
			if (action == actions.SET)
				player.getCapability(NUTRITION_CAPABILITY, null).set(nutrient, Float.parseFloat(args[3]));
			else if (action == actions.ADD)
				player.getCapability(NUTRITION_CAPABILITY, null).add(nutrient, Float.parseFloat(args[3]));
			else if (action == actions.SUBTRACT)
				player.getCapability(NUTRITION_CAPABILITY, null).subtract(nutrient, Float.parseFloat(args[3]));

			// Sync nutrition
			Sync.serverRequest(player);

			// Update chat
			sender.sendMessage(new TextComponentString(nutrient.name + " updated!"));
		}
		else // Write error message
			sender.sendMessage(new TextComponentString("'" + args[2] + "' is not a valid nutrient."));
	}

	private void commandResetNutrition(EntityPlayer player, ICommandSender sender, String[] args) {
		// Reset single nutrient
		if (args.length == 3) {
			Nutrient nutrient = NutrientList.getByName(args[2]);
			if (nutrient != null) {
				player.getCapability(NUTRITION_CAPABILITY, null).reset(nutrient);
				sender.sendMessage(new TextComponentString("Nutrient " + nutrient.name + " reset for " + player.getName() + "!"));
			}
		}
		// Reset all nutrients
		else if (args.length == 2) {
			player.getCapability(NUTRITION_CAPABILITY, null).reset();
			sender.sendMessage(new TextComponentString("Nutrition reset for " + player.getName() + "!"));
		}

		// Sync nutrition
		Sync.serverRequest(player);
	}

	// Checks if the supplied nutrient value is valid and in an acceptable range
	// Spits out an error if problem is met
	private boolean validNumber(ICommandSender sender, String value) {
		// Valid number check
		float newValue;
		if (NumberUtils.isCreatable(value))
			newValue = Float.parseFloat(value);
		else {
			sender.sendMessage(new TextComponentString("'" + value + "' is not a number."));
			return false;
		}

		// Range check (don't sue me Oracle)
		if (!(newValue >= 0 && newValue <= 100)) {
			sender.sendMessage(new TextComponentString("'" + value + "' is not a number between 0 and 100."));
			return false;
		}

		return true;
	}
}
