package ca.wescook.nutrition.events;

import ca.wescook.nutrition.commands.CommandNutrition;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class EventServerStart {
	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandNutrition());
	}
}
