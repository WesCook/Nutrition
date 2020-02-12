package ca.wescook.nutrition.gui;

import ca.wescook.nutrition.Nutrition;
import ca.wescook.nutrition.network.Sync;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.resources.I18n;

import java.io.IOException;

public class NutritionGui extends GuiScreenDynamic {
	private GuiButton buttonClose;
	private GuiLabel label;

	///////////////////
	// Magic Numbers //
	///////////////////

	// Gui Container
	private final int GUI_BASE_WIDTH = 184;
	private final int GUI_BASE_HEIGHT = 72;
	private final int NUTRITION_DISTANCE = 20; // Vertical distance between each nutrient

	// Nutrition Title
	private final int TITLE_VERTICAL_OFFSET = 18;

	// Nutrition icon positions
	private final int NUTRITION_ICON_HORIZONTAL_OFFSET = 10;
	private final int NUTRITION_ICON_VERTICAL_OFFSET = 32;

	// Nutrition bar positions
	private final int NUTRITION_BAR_WIDTH = 130;
	private final int NUTRITION_BAR_HEIGHT = 13;
	private final int NUTRITION_BAR_HORIZONTAL_OFFSET = 40;
	private final int NUTRITION_BAR_VERTICAL_OFFSET = 33;

	// Nutrition label positions
	private final int LABEL_NAME_HORIZONTAL_OFFSET = 30;
	private final int LABEL_VALUE_HORIZONTAL_OFFSET = 43;
	private final int LABEL_VERTICAL_OFFSET = 41;
	private int labelCharacterPadding = 0; // Add padding for long nutrient names

	// Close button position
	private final int CLOSE_BUTTON_WIDTH = 70;
	private final int CLOSE_BUTTON_HEIGHT = 20;
	private final int CLOSE_BUTTON_OFFSET = 12;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks); // Background
		drawNutritionBars(); // Nutrition bars
		super.drawLabels(mouseX, mouseY); // Labels/buttons
	}

	private void drawNutritionBars() {
		int i = 0;
		for (Nutrient nutrient : NutrientList.getVisible()) {
			// Calculate percentage width for nutrition bars
			float currentNutrient = (ClientProxy.localNutrition != null && ClientProxy.localNutrition.get(nutrient) != null) ? Math.round(ClientProxy.localNutrition.get(nutrient)) : 0; // Display empty if null
			int nutritionBarDisplayWidth = (int) (currentNutrient / 100 * NUTRITION_BAR_WIDTH);

			// Draw icons
			itemRender.renderItemIntoGUI(nutrient.icon, left + NUTRITION_ICON_HORIZONTAL_OFFSET, top + NUTRITION_ICON_VERTICAL_OFFSET + (i * NUTRITION_DISTANCE));

			// Draw black background
			drawRect(
					left + NUTRITION_BAR_HORIZONTAL_OFFSET + labelCharacterPadding - 1,
					top + NUTRITION_BAR_VERTICAL_OFFSET + (i * NUTRITION_DISTANCE) - 1,
					left + NUTRITION_BAR_HORIZONTAL_OFFSET + NUTRITION_BAR_WIDTH + labelCharacterPadding + 1,
					top + NUTRITION_BAR_VERTICAL_OFFSET + (i * NUTRITION_DISTANCE) + NUTRITION_BAR_HEIGHT + 1,
					0xff000000
			);

			// Draw colored bar
			drawRect(
					left + NUTRITION_BAR_HORIZONTAL_OFFSET + labelCharacterPadding,
					top + NUTRITION_BAR_VERTICAL_OFFSET + (i * NUTRITION_DISTANCE),
					left + NUTRITION_BAR_HORIZONTAL_OFFSET + nutritionBarDisplayWidth + labelCharacterPadding,
					top + NUTRITION_BAR_VERTICAL_OFFSET + (i * NUTRITION_DISTANCE) + NUTRITION_BAR_HEIGHT,
					nutrient.color
			);

			i++;
		}
	}

	// Called when GUI is opened or resized
	@Override
	public void initGui() {
		// Sync Nutrition info from server to client
		Sync.clientRequest();

		// Calculate label offset for long nutrition names
		for (Nutrient nutrient : NutrientList.getVisible()) {
			int nutrientWidth = fontRenderer.getStringWidth(I18n.format("nutrient." + Nutrition.MODID + ":" + nutrient.name)); // Get width of localized string
			nutrientWidth = (nutrientWidth / 4) * 4; // Round to nearest multiple of 4
			if (nutrientWidth > labelCharacterPadding)
				labelCharacterPadding = nutrientWidth;
		}

		// Update dynamic GUI size
		super.updateContainerSize(GUI_BASE_WIDTH + labelCharacterPadding, GUI_BASE_HEIGHT + (NutrientList.getVisible().size() * NUTRITION_DISTANCE));

		// Add Close button
		buttonList.add(buttonClose = new GuiButton(
				0,
				(width / 2) - (CLOSE_BUTTON_WIDTH / 2),
				bottom - CLOSE_BUTTON_HEIGHT - CLOSE_BUTTON_OFFSET,
				CLOSE_BUTTON_WIDTH,
				CLOSE_BUTTON_HEIGHT,
				I18n.format("gui." + Nutrition.MODID + ":close")
		));

		// Draw labels
		redrawLabels();
	}

	// Called when needing to propagate the window with new information
	public void redrawLabels() {
		// Clear existing labels for nutrition value or screen changes
		labelList.clear();

		// Draw title
		String nutritionTitle = I18n.format("gui." + Nutrition.MODID + ":nutrition_title");
		labelList.add(label = new GuiLabel(fontRenderer, 0, (width / 2) - (fontRenderer.getStringWidth(nutritionTitle) / 2), top + TITLE_VERTICAL_OFFSET, 0, 0, 0xffffffff));
		label.addLine(nutritionTitle);

		// Nutrients names and values
		int i = 0;
		for (Nutrient nutrient : NutrientList.getVisible()) {
			// Create labels for each nutrient type name
			labelList.add(label = new GuiLabel(fontRenderer, 0, left + LABEL_NAME_HORIZONTAL_OFFSET, top + LABEL_VERTICAL_OFFSET + (i * NUTRITION_DISTANCE), 0, 0, 0xffffffff));
			label.addLine(I18n.format("nutrient." + Nutrition.MODID + ":" + nutrient.name)); // Add name from localization file

			// Create percent value labels for each nutrient value
			labelList.add(label = new GuiLabel(fontRenderer, 0, left + LABEL_VALUE_HORIZONTAL_OFFSET + labelCharacterPadding, top + LABEL_VERTICAL_OFFSET + (i * NUTRITION_DISTANCE), 0, 0, 0xffffffff));
			if (ClientProxy.localNutrition != null && ClientProxy.localNutrition.get(nutrient) != null) // Ensure local nutrition data exists
				label.addLine(Math.round(ClientProxy.localNutrition.get(nutrient)) + "%%");
			else
				label.addLine(I18n.format("gui." + Nutrition.MODID + ":updating"));
			i++;
		}
	}

	// Called when button/element is clicked
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button == buttonClose) {
			// Close GUI
			mc.player.closeScreen();
			if (mc.currentScreen == null)
				mc.setIngameFocus();
		}
	}

	// Close GUI if inventory key is hit again
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);

		// If escape key (1), or player inventory key (E), or Nutrition GUI key (N) is pressed
		if (keyCode == 1 || keyCode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode() || keyCode == ClientProxy.keyNutritionGui.getKeyCode()) {
			// Close GUI
			mc.player.closeScreen();
			if (mc.currentScreen == null)
				mc.setIngameFocus();
		}
	}

	// Opening Nutrition menu doesn't pause game
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
