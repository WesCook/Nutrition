package ca.wescook.nutrition.events;

import ca.wescook.nutrition.Nutrition;
import ca.wescook.nutrition.gui.GuiButtonIcon;
import ca.wescook.nutrition.gui.ModGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventNutritionButton {
	private GuiButton buttonNutrition;
	private int NUTRITION_ID = 800;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void guiOpen(GuiScreenEvent.InitGuiEvent.Post event) {
		// If any inventory except player inventory is opened, get out
		GuiScreen gui = event.getGui();
		if (!(gui instanceof GuiInventory))
			return;

		// Create button
		int xPosition = (gui.width / 2) + 10;
		int yPosition = (gui.height / 2) - 22;
		event.getButtonList().add(this.buttonNutrition = new GuiButtonIcon(NUTRITION_ID, xPosition, yPosition, 18, 17, new ItemStack(Items.CARROT)));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void guiButtonClick(GuiScreenEvent.ActionPerformedEvent.Post event) {
		// Only continue if nutrition button is pressed
		if (!event.getButton().equals(buttonNutrition))
			return;

		// Get data
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		World world = Minecraft.getMinecraft().theWorld;

		// Open GUI
		player.openGui(Nutrition.instance, ModGuiHandler.NUTRITION_GUI_ID, world, (int) player.posX, (int) player.posY, (int) player.posZ);
	}
}
