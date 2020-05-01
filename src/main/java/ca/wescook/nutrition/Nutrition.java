package ca.wescook.nutrition;

import ca.wescook.nutrition.capabilities.INutrientManager;
import ca.wescook.nutrition.capabilities.NutritionCapabilityManager;
import ca.wescook.nutrition.capabilities.SimpleImpl;
import ca.wescook.nutrition.events.*;
import ca.wescook.nutrition.gui.ModGuiHandler;
import ca.wescook.nutrition.network.ModPacketHandler;
import ca.wescook.nutrition.utility.ClientData;
import ca.wescook.nutrition.utility.Config;
import ca.wescook.nutrition.utility.DataImporter;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;

@Mod("nutrition")
public class Nutrition {
	public static final String MODID = "nutrition";
	public static Nutrition instance;

	public Nutrition() {
		instance = this;

		// Events
		MinecraftForge.EVENT_BUS.register(new EventRegisterPotions()); // Register custom potions
		MinecraftForge.EVENT_BUS.register(new EventPlayerJoinWorld()); // Attach capability to player
		MinecraftForge.EVENT_BUS.register(new EventPlayerDeath()); // Player death and warping
		MinecraftForge.EVENT_BUS.register(new EventEatFood()); // Register use item event
		MinecraftForge.EVENT_BUS.register(new EventWorldTick()); // Register update event for nutrition decay and potion effects

		// Config
		//MinecraftForge.EVENT_BUS.register(new Config());
		//Config.registerConfigs(event.getModConfigurationDirectory()); // Load Config file

		// Queue up other stages
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	private void onSetup(final FMLCommonSetupEvent event) {
		ModPacketHandler.registerMessages(); // Register network messages
		CapabilityManager.INSTANCE.register(INutrientManager.class, new NutritionCapabilityManager.Storage(), SimpleImpl::new); // Registers capability
		NetworkRegistry.INSTANCE.registerGuiHandler(Nutrition.instance, new ModGuiHandler()); // Register GUI handler

		// TODO: This obviously won't work if fired simultaneously
		DataImporter.load(); // Load nutrients and effects
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	private void onClientSetup(final FMLClientSetupEvent event) {
		if (Config.enableGui) { // If GUI is enabled
			ClientRegistry.registerKeyBinding(ClientData.keyNutritionGui = new KeyBinding("key.nutrition", 49, "Nutrition")); // Register Nutrition keybind, default to "N"
			MinecraftForge.EVENT_BUS.register(new EventNutritionKey()); // Register key input event to respond to keybind
			if (Config.enableGuiButton)
				MinecraftForge.EVENT_BUS.register(new EventNutritionButton()); // Register GUI button event
		}

		if (Config.enableTooltips)
			MinecraftForge.EVENT_BUS.register(new EventTooltip()); // Register tooltip event
	}

	@SubscribeEvent
	private void onServerStarting(FMLServerStartingEvent event) {
		// TODO: Repair commands
		//event.registerServerCommand(new ChatCommand());
	}
}

// TODO: Notes
// - Update event names to onEventName for consistency
// - Maybe move out some of the events in Nutrition.java (this file)
// - Add NBT support?
