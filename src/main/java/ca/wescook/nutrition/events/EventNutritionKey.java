package ca.wescook.nutrition.events;

import ca.wescook.nutrition.Nutrition;
import ca.wescook.nutrition.gui.ModGuiHandler;
import ca.wescook.nutrition.utility.ClientData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class EventNutritionKey {

	// TODO: Verify if this event will work
	@SubscribeEvent (priority = EventPriority.LOWEST)
	public void keyInput(TickEvent.ClientTickEvent event) {

//	@SubscribeEvent
//	public void keyInput(InputEvent.KeyInputEvent event) {
//		// Exit on key de-press
//		if (!Keyboard.getEventKeyState()) {
//			return;
//		}

		// If Nutrition key is pressed, and F3 key is not being held (F3+N toggles Spectator mode)
		long handle = Minecraft.getInstance().mainWindow.getHandle();
		if (ClientData.keyNutritionGui.isKeyDown() && !InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_F3)) {
			openNutritionGui();
		}
	}

	// Opens GUI to show nutrition menu
	private void openNutritionGui() {
		// Get data
		PlayerEntity player = Minecraft.getInstance().player;
		World world = Minecraft.getInstance().world;

		// Open GUI
		player.openGui(Nutrition.instance, ModGuiHandler.NUTRITION_GUI_ID, world, (int) player.posX, (int) player.posY, (int) player.posZ);
	}
}
