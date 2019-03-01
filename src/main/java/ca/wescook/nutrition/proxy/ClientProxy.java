package ca.wescook.nutrition.proxy;

import ca.wescook.nutrition.capabilities.INutrientManager;
import ca.wescook.nutrition.events.EventNutritionButton;
import ca.wescook.nutrition.events.EventNutritionKey;
import ca.wescook.nutrition.events.EventTooltip;
import ca.wescook.nutrition.utility.Config;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy {
	public static INutrientManager localNutrition; // Holds local copy of data/methods for client-side prediction
	public static KeyBinding keyNutritionGui;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
	}

	@Override
	public void init(FMLInitializationEvent event) {

		if (Config.enableGui) { // If GUI is enabled
			ClientRegistry.registerKeyBinding(keyNutritionGui = new KeyBinding("key.nutrition", 49, "Nutrition")); // Register Nutrition keybind, default to "N"
			MinecraftForge.EVENT_BUS.register(new EventNutritionKey()); // Register key input event to respond to keybind
			if (Config.enableGuiButton)
				MinecraftForge.EVENT_BUS.register(new EventNutritionButton()); // Register GUI button event
		}

		if (Config.enableTooltips)
			MinecraftForge.EVENT_BUS.register(new EventTooltip()); // Register tooltip event
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
	}
}
