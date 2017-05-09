package ca.wescook.nutrition.nutrients;

import ca.wescook.nutrition.configs.Config;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

// Maintains information about nutrients (name, color, icon)
// Stored client and server-side
public class NutrientList {
	private static List<JsonNutrient> jsonNutrients = new ArrayList<>(); // Raw deserialized data from JSON
	private static List<Nutrient> nutrients = new ArrayList<>(); // Parsed nutrients list

	// Register single JSON object
	public static void register(JsonNutrient jsonNutrientIn) {
		jsonNutrients.add(jsonNutrientIn);
	}

	// Register list of JSON objects
	public static void register(List<JsonNutrient> jsonNutrientsIn) {
		NutrientList.jsonNutrients.addAll(jsonNutrientsIn);
	}

	// Parse JSON data into more useful objects
	// Run during Post-Init, so most foodItems will be in-game by now
	public static void parseJson() {
		for (JsonNutrient nutrientRaw : jsonNutrients) {
			// Copying and cleaning data
			Nutrient nutrient = new Nutrient();
			nutrient.name = nutrientRaw.name; // Localization key used in lang file
			nutrient.icon = new ItemStack(Item.getByNameOrId(nutrientRaw.icon)); // Create ItemStack used to represent icon
			nutrient.color = Integer.parseUnsignedInt("ff" + nutrientRaw.color, 16); // Convert hex string to int
			nutrient.foodOreDict = nutrientRaw.food.oredict; // Ore dicts remains as strings

			// Food - Items
			for (String itemName : nutrientRaw.food.items) {
				Item foodItem = Item.getByNameOrId(itemName);
				if (foodItem == null) // If food has valid item (warning is incorrect)
					logFoodError(itemName + " is not a valid item (" + nutrient.name + ")");
				else if (!(foodItem instanceof ItemFood) && Config.enableLogging) // If item is specified as a food
					logFoodError(itemName + " is not a valid food (" + nutrient.name + ")");
				else
					nutrient.foodItems.add((ItemFood) foodItem); // Register it!
			}

			// Register nutrient
			nutrients.add(nutrient);
		}
	}

	// Return all parsed nutrients
	public static List<Nutrient> get() {
		return nutrients;
	}

	// Return nutrient by name (null if not found)
	public static Nutrient getByName(String name) {
		for (Nutrient nutrient : nutrients) {
			if (nutrient.name.equals(name))
				return nutrient;
		}
		return null;
	}

	// This method mostly exists to simplify the if/else chain up there
	private static void logFoodError(String msg) {
		if (Config.enableLogging)
			System.out.println(msg);
	}
}
