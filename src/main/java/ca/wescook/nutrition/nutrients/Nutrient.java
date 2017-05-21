package ca.wescook.nutrition.nutrients;

import net.minecraft.block.BlockCake;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

// Nutrient object represents a type of food group
public class Nutrient {
	public String name;
	public ItemStack icon;
	public int color;
	public boolean enabled;
	public List<String> foodOreDict = new ArrayList<>();
	public List<ItemFood> foodItems = new ArrayList<>();
	public List<BlockCake> foodCakes = new ArrayList<>();
}
