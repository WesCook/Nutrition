package ca.wescook.nutrition.nutrients;

import ca.wescook.nutrition.utility.Log;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

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

			// Food Items
			if (nutrientRaw.food.items != null) {
				for (String fullName : nutrientRaw.food.items) {
					// Initial values
					String name = fullName;
					int metadata = 0;

					// Null check input string
					if (name == null) {
						Log.fatal("There is a null item in the '" + nutrient.name + "' JSON.  Check for a trailing comma in the file.");
						throw new NullPointerException("There is a null item in the '" + nutrient.name + "' JSON.  Check for a trailing comma in the file.");
					}

					// If string includes meta data, update name/meta
					if (StringUtils.countMatches(fullName, ":") == 2) { // Two colons for metadata (eg. minecraft:golden_apple:1)
						// Get data
						name = StringUtils.substringBeforeLast(fullName, ":");
						String metaString = StringUtils.substringAfterLast(fullName, ":");

						// Is valid metadata
						if (NumberUtils.isCreatable(metaString))
							metadata = Integer.decode(metaString);
						else {
							Log.missingFood(fullName + " does not contain valid metadata (" + fullName + ")");
							continue;
						}
					}

					// Get item
					Item item = Item.getByNameOrId(name);

					// Null test item
					if (item == null) {
						Log.missingFood(name + " is not a valid item name (" + fullName + ")");
						continue;
					}

					// Add to nutrient, or report error
					ItemStack itemStack = new ItemStack(item, 1, metadata);
					if (NutrientUtils.isValidFood(itemStack))
						nutrient.foodItems.add(itemStack);
					else
						Log.missingFood(name + " is not a valid food (" + fullName + ")");
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
