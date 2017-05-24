package ca.wescook.nutrition.nutrients;

import ca.wescook.nutrition.utility.Log;
import net.minecraft.block.BlockCake;
import net.minecraft.item.*;

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
			// Skip if nutrient is not enabled, or if field omitted (null)
			if (nutrientRaw.enabled != null && !nutrientRaw.enabled)
				continue;

			// Copying and cleaning data
			Nutrient nutrient = new Nutrient();

			// Name, icon color
			try {
				nutrient.name = nutrientRaw.name;
				nutrient.icon = new ItemStack(Item.getByNameOrId(nutrientRaw.icon)); // Create ItemStack used to represent icon
				nutrient.color = Integer.parseUnsignedInt("ff" + nutrientRaw.color, 16); // Convert hex string to int
			} catch (NullPointerException e) {
				Log.fatal("Missing or invalid JSON.  A name, icon, and color are required.");
				throw e;
			}

			// Food - Ore Dictionary
			if (nutrientRaw.food.oredict != null)
				nutrient.foodOreDict = nutrientRaw.food.oredict; // Ore dicts remains as strings

			// Extract metadata
			int metadata = 0; // TODO: Read from file

			// Food Items
			if (nutrientRaw.food.items != null) {
				for (String itemName : nutrientRaw.food.items) {
					// Get item
					Item foodItem = Item.getByNameOrId(itemName);
					if (foodItem == null) {
						Log.missingFood(itemName + " is not a valid item name (" + nutrient.name + ")");
						continue;
					}

					// Verify it meets a valid type
					boolean validItem = false;
					if (foodItem instanceof ItemFood) // ItemFood
						validItem = true;
					else if (foodItem instanceof ItemBlock && ((ItemBlock) foodItem).getBlock() instanceof BlockCake) // Cake - Vanilla
						validItem = true;
					else if (foodItem instanceof ItemBlockSpecial && ((ItemBlockSpecial) foodItem).getBlock() instanceof BlockCake) // Cake - Modded
						validItem = true;

					// Add to nutrient, or report error
					if (validItem)
						nutrient.foodItems.add(new ItemStack(foodItem, 1, metadata));
					else
						Log.missingFood(itemName + " is not a valid food (" + nutrient.name + ")");
				}
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
}
