package ca.wescook.nutrition.proxy;

import ca.wescook.nutrition.events.EventNutritionButton;
import ca.wescook.nutrition.events.EventNutritionKey;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class ClientProxy extends CommonProxy {
	public static KeyBinding keyNutritionGui;

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		ClientRegistry.registerKeyBinding(keyNutritionGui = new KeyBinding("key.nutrition", 49, "Homestead")); // Register Nutrition keybind, default to "N"
		MinecraftForge.EVENT_BUS.register(new EventNutritionKey()); // Register key input event to respond to keybind
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		MinecraftForge.EVENT_BUS.register(new EventNutritionButton()); // Register GUI events
	}
}
