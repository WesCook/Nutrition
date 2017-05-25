package ca.wescook.nutrition.gui;

import ca.wescook.nutrition.Nutrition;
import ca.wescook.nutrition.network.ModPacketHandler;
import ca.wescook.nutrition.network.PacketNutritionRequest;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class NutritionGui extends GuiScreen {
	private GuiButton buttonClose;
	private GuiLabel label;

	// Magic numbers
	private int nutritionDistance = 20; // Vertical distance between each entry

	// Nutrition Title
	private int titleVerticalOffset = -123;

	// Background texture/size
	private ResourceLocation nutritionBackground = new ResourceLocation(Nutrition.MODID, "textures/gui/nutrition.png");
	private int backgroundWidth = 230;
	private int backgroundHeight = 180;

	// Nutrition icon positions
	private int nutritionIconHorizontalOffset = -106;
	private int nutritionIconVerticalOffset = -59;

	// Nutrition bar positions
	private int nutritionBarWidth = 130;
	private int nutritionBarHeight = 13;
	private int nutritionBarHorizontalOffset = -27;
	private int nutritionBarVerticalOffset = -57;

	// Nutrition label positions
	private int labelNameHorizontalOffset = -85;
	private int labelValueHorizontalOffset = -24;
	private int labelVerticalOffset = -99;

	// Close button position
	private int closeButtonWidth = 70;
	private int closeButtonHeight = 20;
	private int closeButtonVerticalOffset = 56;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// Darken background
		this.drawDefaultBackground();

		// Draw GUI background
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f); // Reset color
		this.mc.getTextureManager().bindTexture(nutritionBackground);
		this.drawTexturedModalRect((width / 2) - (backgroundWidth / 2), (height / 2) - (backgroundHeight / 2), 0, 0, backgroundWidth, backgroundHeight);

		// Nutrition bars
		int i = 0;
		for (Nutrient nutrient : NutrientList.get()) {
			// Calculate percentage width for nutrition bars
			float currentNutrient = (ClientProxy.nutrientData != null && ClientProxy.nutrientData.get(nutrient) != null) ? Math.round(ClientProxy.nutrientData.get(nutrient)) : 0; // If null, setPlayerNutrition to 0, else getPlayerNutrition true value
			int nutritionBarDisplayWidth = (int) (currentNutrient / 100 * nutritionBarWidth);

			// Draw icons
			this.itemRender.renderItemIntoGUI(nutrient.icon, (width / 2) + nutritionIconHorizontalOffset, (height / 2) + nutritionIconVerticalOffset + (i * nutritionDistance));

			// Draw black background
			drawRect(
				(width / 2) + nutritionBarHorizontalOffset - 1,
				(height / 2) + nutritionBarVerticalOffset + (i * nutritionDistance) - 1,
				(width / 2) + nutritionBarHorizontalOffset + nutritionBarWidth + 1,
				(height / 2) + nutritionBarVerticalOffset + (i * nutritionDistance) + nutritionBarHeight + 1,
				0xff000000
			);

			// Draw colored bar
			drawRect(
				(width / 2) + nutritionBarHorizontalOffset,
				(height / 2) + nutritionBarVerticalOffset + (i * nutritionDistance),
				(width / 2) + nutritionBarHorizontalOffset + nutritionBarDisplayWidth,
				(height / 2) + nutritionBarVerticalOffset + (i * nutritionDistance) + nutritionBarHeight,
				nutrient.color
			);

			i++;
		}

		// Call parent
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	// Called when GUI is opened or resized
	@Override
	public void initGui() {
		// Nutrition sync request
		ModPacketHandler.NETWORK_CHANNEL.sendToServer(new PacketNutritionRequest.Message());

		// Add buttons
		this.buttonList.add(this.buttonClose = new GuiButton(0, (width / 2) - (closeButtonWidth / 2), (height / 2) + closeButtonVerticalOffset, closeButtonWidth, closeButtonHeight, "Close"));

		// Draw labels
		updateInformation();
	}

	// Called when needing to propagate the window with new information
	public void updateInformation() {
		// Clear existing labels for nutrition value or screen changes
		this.labelList.clear();

		// Draw title
		String nutritionTitle = I18n.format("gui." + Nutrition.MODID + ":nutrition_title");
		this.labelList.add(label = new GuiLabel(fontRendererObj, 0, (width / 2) - (fontRendererObj.getStringWidth(nutritionTitle) / 2), (height / 2) + titleVerticalOffset, 200, 100, 0xffffffff));
		label.addLine(nutritionTitle);

		// Nutrients names and values
		int i = 0;
		for (Nutrient nutrient : NutrientList.get()) {
			// Create labels for each nutrient type name
			this.labelList.add(label = new GuiLabel(fontRendererObj, 0, (width / 2) + labelNameHorizontalOffset, (height / 2) + labelVerticalOffset + (i * nutritionDistance), 200, 100, 0xffffffff));
			label.addLine(I18n.format("nutrient." + Nutrition.MODID + ":" + nutrient.name)); // Add name from localization file

			// Create percent value labels for each nutrient value
			this.labelList.add(label = new GuiLabel(fontRendererObj, 0, (width / 2) + labelValueHorizontalOffset, (height / 2) + labelVerticalOffset + (i * nutritionDistance), 200, 100, 0xffffffff));
			if (ClientProxy.nutrientData != null && ClientProxy.nutrientData.get(nutrient) != null) // Ensure local nutrition data exists
				label.addLine(Math.round(ClientProxy.nutrientData.get(nutrient)) + "%%");
			else
				label.addLine(I18n.format("gui." + Nutrition.MODID + ":updating"));
			i++;
		}
	}

	// Called when button/element is clicked
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.buttonClose) {
			this.mc.displayGuiScreen(null); // Close GUI
			if (this.mc.currentScreen == null)
				this.mc.setIngameFocus(); // Focus game
		}
	}

	// Close GUI if inventory key is hit again
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);

		// If "E" (open GUI) key is pressed
		if (keyCode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
			// Close GUI
			this.mc.displayGuiScreen(null);

			// Set focus
			if (this.mc.currentScreen == null)
				this.mc.setIngameFocus();
		}
	}

	// Opening Nutrition menu doesn't pause game
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
