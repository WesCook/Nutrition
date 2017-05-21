package ca.wescook.nutrition.nutrients;

import ca.wescook.nutrition.utility.Config;
import ca.wescook.nutrition.utility.Log;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
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

			// Food - Items
			if (nutrientRaw.food.items != null) {
				for (String itemName : nutrientRaw.food.items) {
					Item foodItem = Item.getByNameOrId(itemName);
					if (foodItem == null) // If food has valid item (warning is incorrect)
						Log.missingFood(itemName + " is not a valid item (" + nutrient.name + ")");
					else if (!(foodItem instanceof ItemFood) && Config.enableLogging) // If item isn't a real ItemFood
						Log.missingFood(itemName + " is not a valid food (" + nutrient.name + ")");
					else
						nutrient.foodItems.add((ItemFood) foodItem); // Register it!
				}
			}

			// Food - Cakes
			if (nutrientRaw.food.cakes != null) {
				for (String cakeName : nutrientRaw.food.cakes) {
					Block blockCake = Block.getBlockFromName(cakeName);
					if (blockCake == null) // If cake has valid block
						Log.missingFood(cakeName + " is not a valid block (" + nutrient.name + ")");
					else if (!(blockCake instanceof BlockCake) && Config.enableLogging) // If cake isn't a real BlockCake
						Log.missingFood(cakeName + " is not a valid cake (" + nutrient.name + ")");
					else
						nutrient.foodCakes.add((BlockCake) blockCake); // Register it!
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
