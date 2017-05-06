package ca.wescook.nutrition.nutrients;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

// Nutrient object represents a type of food group
public class Nutrient {
	public String name;
	public ItemStack icon;
	public int color;
	List<String> foodOreDict = new ArrayList<>();
	List<Item> foodItems = new ArrayList<>();
}
