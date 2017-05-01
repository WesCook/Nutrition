package ca.wescook.nutrition.nutrition;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

// Maintains list of information about nutrients (name, color, icon)
// Stored client and server-side
public class NutrientList {
	private static List<Nutrient> nutrients = new ArrayList<Nutrient>();

	// Register nutrients
	public static void register() {
		nutrients.add(new Nutrient("grain", 0xfff4d92e, new ItemStack(Items.WHEAT)));
		nutrients.add(new Nutrient("fruit", 0xffcd73f4, new ItemStack(Items.APPLE)));
		nutrients.add(new Nutrient("vegetable", 0xff72dd5a, new ItemStack(Items.CARROT)));
		nutrients.add(new Nutrient("protein", 0xffdb6c23, new ItemStack(Items.COOKED_BEEF)));
		nutrients.add(new Nutrient("dairy", 0xffa0d4f7, new ItemStack(Items.MILK_BUCKET)));
	}

	// Return list of all nutrients
	public static List<Nutrient> get() {
		return nutrients;
	}

	// Return nutrient by name (null if not found)
	public static Nutrient get(String name) {
		for (Nutrient nutrient : nutrients) {
			if (nutrient.name.equals(name))
				return nutrient;
		}
		return null;
	}
}
