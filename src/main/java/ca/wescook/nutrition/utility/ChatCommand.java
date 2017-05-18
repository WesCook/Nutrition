package ca.wescook.nutrition.utility;

import ca.wescook.nutrition.Nutrition;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.nutrition.NutritionProvider;
import net.minecraft.client.resources.I18n;
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
		return I18n.format("command." + Nutrition.MODID + ":help");
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
		sender.addChatMessage(new TextComponentString(I18n.format("command." + Nutrition.MODID + ":help")));
	}

	private void commandGetNutrition(ICommandSender sender, String[] args) {
		// If missing parameter, offer help
		if (args.length != 2) {
			sender.addChatMessage(new TextComponentString(I18n.format("command." + Nutrition.MODID + ":error_get_missing")));
			return;
		}

		// Write nutrient name and percentage to chat
		EntityPlayer player = (EntityPlayer) sender;
		Nutrient nutrient = NutrientList.getByName(args[1]);
		if (nutrient != null) {
			String localizedName = I18n.format("nutrient." + Nutrition.MODID + ":" + nutrient.name);
			Float nutrientValue = player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).get(nutrient);
			sender.addChatMessage(new TextComponentString(localizedName + ": " + String.format("%.2f", nutrientValue) + "%"));
		}
		else // Write error message
			sender.addChatMessage(new TextComponentString("'" + args[1] + "' " + I18n.format("command." + Nutrition.MODID + ":error_invalid_nutrient")));
	}

	private void commandSetNutrition(ICommandSender sender, String[] args) {
		// If missing parameter, offer help
		if (args.length != 3) {
			sender.addChatMessage(new TextComponentString(I18n.format("command." + Nutrition.MODID + ":error_set_missing")));
			return;
		}

		// Valid number check
		Float newValue;
		if (NumberUtils.isNumber(args[2]))
			newValue = Float.parseFloat(args[2]);
		else {
			sender.addChatMessage(new TextComponentString(I18n.format("command." + Nutrition.MODID + ":error_number_invalid")));
			return;
		}

		// Range check (don't sue me Oracle)
		if (!(newValue >= 0 && newValue <= 100)) {
			sender.addChatMessage(new TextComponentString(I18n.format("command." + Nutrition.MODID + ":error_number_range")));
			return;
		}

		// Write nutrient name and percentage to chat
		EntityPlayer player = (EntityPlayer) sender;
		Nutrient nutrient = NutrientList.getByName(args[1]);

		if (nutrient != null) {
			String localizedName = I18n.format("nutrient." + Nutrition.MODID + ":" + nutrient.name);
			player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).set(nutrient, newValue);
			sender.addChatMessage(new TextComponentString(localizedName + " " + I18n.format("command." + Nutrition.MODID + ":set_success")));
		}
		else // Write error message
			sender.addChatMessage(new TextComponentString("'" + args[1] + "' " + I18n.format("command." + Nutrition.MODID + ":error_invalid_nutrient")));
	}
}
