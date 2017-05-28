package ca.wescook.nutrition;

import ca.wescook.nutrition.proxy.CommonProxy;
import ca.wescook.nutrition.utility.ChatCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import static ca.wescook.nutrition.Nutrition.MODID;
import static ca.wescook.nutrition.Nutrition.MODNAME;

@Mod(modid = MODID, name = MODNAME, version = "@VERSION@", acceptableRemoteVersions = "*")
public class Nutrition {
	public static final String MODID = "nutrition";
	public static final String MODNAME = "Nutrition";

	// Create instance of mod
	@Mod.Instance
	public static Nutrition instance;

	// Create instance of proxy
	// This will vary depending on if the client or server is running
	@SidedProxy(clientSide="ca.wescook.nutrition.proxy.ClientProxy", serverSide="ca.wescook.nutrition.proxy.ServerProxy")
	static private CommonProxy proxy;

	// Events
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Nutrition.proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		Nutrition.proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Nutrition.proxy.postInit(event);
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new ChatCommand());
	}
}
