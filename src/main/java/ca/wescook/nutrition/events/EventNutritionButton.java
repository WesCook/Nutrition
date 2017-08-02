package ca.wescook.nutrition.events;

import ca.wescook.nutrition.Nutrition;
import ca.wescook.nutrition.gui.ModGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventNutritionButton {
	private int NUTRITION_ID = 800;
	private ResourceLocation NUTRITION_ICON = new ResourceLocation(Nutrition.MODID, "textures/gui/gui.png");
	private GuiButtonImage buttonNutrition;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void guiOpen(GuiScreenEvent.InitGuiEvent.Post event) {
		// If any inventory except player inventory is opened, get out
		GuiScreen gui = event.getGui();
		if (!(gui instanceof GuiInventory))
			return;

		// Button position
		int xPosition = ((GuiInventory) gui).getGuiLeft() + 132;
		int yPosition = ((GuiInventory) gui).getGuiTop() + 61;

		// Create button
		buttonNutrition = new GuiButtonImage(NUTRITION_ID, xPosition, yPosition, 20, 18, 14, 0, 19, NUTRITION_ICON);
		event.getButtonList().add(buttonNutrition);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void guiButtonClick(GuiScreenEvent.ActionPerformedEvent.Post event) {
		// Only run on GuiInventory
		if (!(event.getGui() instanceof GuiInventory))
			return;

		// If nutrition button is clicked
		if (event.getButton().equals(buttonNutrition)) {
			// Get data
			EntityPlayer player = Minecraft.getMinecraft().player;
			World world = Minecraft.getMinecraft().world;

			// Open GUI
			player.openGui(Nutrition.instance, ModGuiHandler.NUTRITION_GUI_ID, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		} else {
			// Presumably recipe book button was clicked - recalculate nutrition button position
			int xPosition = ((GuiInventory) event.getGui()).getGuiLeft() + 132;
			int yPosition = ((GuiInventory) event.getGui()).getGuiTop() + 61;
			buttonNutrition.setPosition(xPosition, yPosition);
		}
	}
}
