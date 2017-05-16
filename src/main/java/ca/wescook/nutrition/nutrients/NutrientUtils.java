package ca.wescook.nutrition.nutrients;

import ca.wescook.nutrition.configs.Config;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class NutrientUtils {
	// Returns list of nutrients that food belongs to
	public static List<Nutrient> getFoodNutrients(ItemFood eatingFood) {
		List<Nutrient> nutrientsFound = new ArrayList<>();

		// Loop through nutrients to look for food
		foodSearch:
		for (Nutrient nutrient : NutrientList.get()) { // All nutrients
			// Search food items
			for (ItemFood listedFood : nutrient.foodItems) { // All foods in that nutrient
				if (listedFood.equals(eatingFood)) {
					nutrientsFound.add(nutrient); // Add nutrient
					continue foodSearch; // Skip rest of search in this nutrient, try others
				}
			}

			// Search ore dictionary
			for (String listedOreDict : nutrient.foodOreDict) { // All ore dicts in that nutrient
				for (ItemStack testingItemStack : OreDictionary.getOres(listedOreDict)) { // All items that match that oredict (eg. listAllmilk)
					Item testingItem = testingItemStack.getItem(); // Get real item
					if (testingItem.equals(eatingFood)) { // Our food matches oredict
						nutrientsFound.add(nutrient); // Add nutrient
						continue foodSearch; // Skip rest of search in this nutrient, try others
					}
				}
			}
		}

		return nutrientsFound;
	}

	// Calculate nutrition value for supplied food
	// Requires nutrient list from that food for performance reasons (see getFoodNutrients)
	public static float calculateNutrition(ItemFood food, List<Nutrient> nutrients) {
		int foodValue = food.getHealAmount(new ItemStack(food)); // Number of half-drumsticks food heals
		float adjustedFoodValue = foodValue * Config.nutritionMultiplier; // Multiply by config value
		float lossPercentage = (float) Config.lossPerNutrient / 100; // Loss percentage from config file
		float foodLoss = (adjustedFoodValue * lossPercentage * (nutrients.size() - 1)); // Lose 15% (configurable) for each nutrient added after the first nutrient
		float nutritionValue = Math.max(1, adjustedFoodValue - foodLoss); // Subtract from true value, with a floor of 1 (prevent zero/negatives)

		return nutritionValue;
	}
}
