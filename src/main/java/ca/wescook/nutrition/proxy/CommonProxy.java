package ca.wescook.nutrition.proxy;

import ca.wescook.nutrition.Nutrition;
import ca.wescook.nutrition.configs.Config;
import ca.wescook.nutrition.events.EventEatFood;
import ca.wescook.nutrition.events.EventNutritionDecay;
import ca.wescook.nutrition.events.EventPlayerAttachCapability;
import ca.wescook.nutrition.events.EventPlayerClone;
import ca.wescook.nutrition.gui.ModGuiHandler;
import ca.wescook.nutrition.network.ModPacketHandler;
import ca.wescook.nutrition.nutrition.INutrition;
import ca.wescook.nutrition.nutrition.NutrientList;
import ca.wescook.nutrition.nutrition.NutritionStorage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {
		Config.registerConfigs(event.getModConfigurationDirectory()); // Create config files
		ModPacketHandler.registerMessages(); // Register network messages
		NutrientList.register(); // Register list of nutrients
		CapabilityManager.INSTANCE.register(INutrition.class, new NutritionStorage(), ca.wescook.nutrition.nutrition.Nutrition.class); // Register capability
		MinecraftForge.EVENT_BUS.register(new EventPlayerAttachCapability()); // Attach capability to player
	}

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(Nutrition.instance, new ModGuiHandler()); // Register GUI handler
	}

	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new EventPlayerClone()); // Player death and warping
		MinecraftForge.EVENT_BUS.register(new EventEatFood()); // Register use item event
		MinecraftForge.EVENT_BUS.register(new EventNutritionDecay()); // Register update event for Attrition
	}
}
