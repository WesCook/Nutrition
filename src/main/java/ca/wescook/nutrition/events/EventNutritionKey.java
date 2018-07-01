package ca.wescook.nutrition.events;

import ca.wescook.nutrition.Nutrition;
import ca.wescook.nutrition.gui.ModGuiHandler;
import ca.wescook.nutrition.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class EventNutritionKey {
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void keyInput(InputEvent.KeyInputEvent event) {
		// Exit on key de-press
		if (!Keyboard.getEventKeyState()) {
			return;
		}

		// If Nutrition key is pressed, and F3 key is not being held (F3+N toggles Spectator mode)
		if (ClientProxy.keyNutritionGui.isKeyDown() && !Keyboard.isKeyDown(Keyboard.KEY_F3)) {
			openNutritionGui();
		}
	}

	// Opens GUI to show nutrition menu
	@SideOnly(Side.CLIENT)
	private void openNutritionGui() {
		// Get data
		EntityPlayer player = Minecraft.getMinecraft().player;
		World world = Minecraft.getMinecraft().world;

		// Open GUI
		player.openGui(Nutrition.instance, ModGuiHandler.NUTRITION_GUI_ID, world, (int) player.posX, (int) player.posY, (int) player.posZ);
	}
}
