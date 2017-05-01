package ca.wescook.nutrition.nutrition;

import net.minecraft.item.ItemStack;

public class Nutrient {
	// Nutrient properties
	public String name;
	public int color;
	public ItemStack icon;

	// Update object
	Nutrient(String name, int color, ItemStack icon) {
		this.name = name;
		this.color = color;
		this.icon = icon;
	}
}
