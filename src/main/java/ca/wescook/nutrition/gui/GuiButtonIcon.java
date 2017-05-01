package ca.wescook.nutrition.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

public class GuiButtonIcon extends GuiButton {
	ItemStack icon;

	// This is the same as GuiButton, but uses an icon (ItemStack) instead of text
	public GuiButtonIcon(int buttonId, int x, int y, int widthIn, int heightIn, ItemStack icon) {
		super(buttonId, x, y, widthIn, heightIn, ""); // Pass in empty string
		this.icon = icon;
	}

	// This exists to draw an icon over the button
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		drawHorizontalLine(xPosition, xPosition + width - 1, yPosition + height, 0xff000000); // Draw line on bottom
		super.drawButton(mc, mouseX, mouseY); // Draw regular button
		mc.getRenderItem().renderItemIntoGUI(icon, xPosition + (width / 2) - 8, yPosition + (height / 2)  - 7); // Draw our icon centered
	}
}
