package ca.wescook.nutrition.proxy;

import ca.wescook.nutrition.Nutrition;
import ca.wescook.nutrition.capabilities.CapabilityManager;
import ca.wescook.nutrition.events.*;
import ca.wescook.nutrition.gui.ModGuiHandler;
import ca.wescook.nutrition.network.ModPacketHandler;
import ca.wescook.nutrition.nutrients.NutrientUtils;
import ca.wescook.nutrition.potions.ModPotions;
import ca.wescook.nutrition.utility.Config;
import ca.wescook.nutrition.utility.DataImporter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {
		Config.registerConfigs(event.getModConfigurationDirectory()); // Load Config file
		ModPacketHandler.registerMessages(); // Register network messages
		CapabilityManager.register(); // Register capability

		ModPotions.createPotions(); // Register custom potions
		MinecraftForge.EVENT_BUS.register(new EventRegistry()); // Register custom potions
		MinecraftForge.EVENT_BUS.register(new EventPlayerJoinWorld()); // Attach capability to player
		MinecraftForge.EVENT_BUS.register(new EventPlayerDeath()); // Player death and warping
		MinecraftForge.EVENT_BUS.register(new EventEatFood()); // Register use item event
		MinecraftForge.EVENT_BUS.register(new EventPlayerUpdate()); // Register update event for nutrition decay and potion effects
	}

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(Nutrition.instance, new ModGuiHandler()); // Register GUI handler
	}

	public void postInit(FMLPostInitializationEvent event) {
		DataImporter.reload(); // Load nutrients and effects
		if (Config.logMissingNutrients) // List all foods registered in-game without nutrients
			NutrientUtils.findRegisteredFoods();
	}
}
