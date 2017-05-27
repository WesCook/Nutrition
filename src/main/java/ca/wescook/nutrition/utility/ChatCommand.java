package ca.wescook.nutrition.utility;

import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.nutrition.NutritionProvider;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.lang3.math.NumberUtils;

public class ChatCommand extends CommandBase {
	@Override
	public String getCommandName() {
		return "nutrition";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/nutrition <get/set> <nutrient> <value>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			// Which command to execute
			if (args.length == 0 || args[0].equals("help"))
				commandHelp(sender);
			else if (args[0].equals("get"))
				commandGetNutrition(sender, args);
			else if (args[0].equals("set"))
				commandSetNutrition(sender, args);
		}
	}

	private void commandHelp(ICommandSender sender) {
		sender.addChatMessage(new TextComponentString("/nutrition <get/set> <nutrient> <value>"));
	}

	private void commandGetNutrition(ICommandSender sender, String[] args) {
		// If missing parameter, offer help
		if (args.length != 2) {
			sender.addChatMessage(new TextComponentString("Invalid format.  /nutrition get <nutrient>"));
			return;
		}

		// Write nutrient name and percentage to chat
		EntityPlayer player = (EntityPlayer) sender;
		Nutrient nutrient = NutrientList.getByName(args[1]);
		if (nutrient != null) {
			Float nutrientValue = player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).get(nutrient);
			sender.addChatMessage(new TextComponentString(nutrient.name + ": " + String.format("%.2f", nutrientValue) + "%"));
		}
		else // Write error message
			sender.addChatMessage(new TextComponentString("'" + args[1] + "' is not a valid nutrient."));
	}

	private void commandSetNutrition(ICommandSender sender, String[] args) {
		// If missing parameter, offer help
		if (args.length != 3) {
			sender.addChatMessage(new TextComponentString("Invalid format.  /nutrition set <nutrient> <value>"));
			return;
		}

		// Valid number check
		Float newValue;
		if (NumberUtils.isNumber(args[2]))
			newValue = Float.parseFloat(args[2]);
		else {
			sender.addChatMessage(new TextComponentString("Value is not a number."));
			return;
		}

		// Range check (don't sue me Oracle)
		if (!(newValue >= 0 && newValue <= 100)) {
			sender.addChatMessage(new TextComponentString("Value is not between 0 and 100."));
			return;
		}

		// Write nutrient name and percentage to chat
		EntityPlayer player = (EntityPlayer) sender;
		Nutrient nutrient = NutrientList.getByName(args[1]);

		if (nutrient != null) {
			player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).set(nutrient, newValue);
			sender.addChatMessage(new TextComponentString(nutrient.name + " updated!"));
		}
		else // Write error message
			sender.addChatMessage(new TextComponentString("'" + args[1] + "' is not a valid nutrient."));
	}
}
