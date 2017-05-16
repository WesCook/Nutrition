package ca.wescook.nutrition.proxy;

import ca.wescook.nutrition.configs.Config;
import ca.wescook.nutrition.events.EventNutritionButton;
import ca.wescook.nutrition.events.EventNutritionKey;
import ca.wescook.nutrition.events.EventTooltip;
import ca.wescook.nutrition.nutrients.Nutrient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.Map;

public class ClientProxy extends CommonProxy {
	public static KeyBinding keyNutritionGui;
	public static Map<Nutrient, Float> nutrientData; // Holds local copy of nutrition data, which resyncs routinely

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		if (Config.enableGui) { // If GUI is enabled
			ClientRegistry.registerKeyBinding(keyNutritionGui = new KeyBinding("key.nutrition", 49, "Nutrition")); // Register Nutrition keybind, default to "N"
			MinecraftForge.EVENT_BUS.register(new EventNutritionKey()); // Register key input event to respond to keybind
			if (Config.enableGuiButton)
				MinecraftForge.EVENT_BUS.register(new EventNutritionButton()); // Register GUI button event
		}

		if (Config.enableTooltips)
			MinecraftForge.EVENT_BUS.register(new EventTooltip()); // Register tooltip event
	}
}
