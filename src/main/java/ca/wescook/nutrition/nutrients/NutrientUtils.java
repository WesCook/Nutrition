package ca.wescook.nutrition.nutrients;

import ca.wescook.nutrition.utility.Config;
import ca.wescook.nutrition.utility.Log;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class NutrientUtils {
	// Returns list of nutrients that food belongs to
	public static List<Nutrient> getFoodNutrients(ItemStack eatingFood) {
		List<Nutrient> nutrientsFound = new ArrayList<>();

		// Loop through nutrients to look for food
		foodSearch:
		for (Nutrient nutrient : NutrientList.get()) { // All nutrients
			// Search foods
			for (ItemStack listedFood : nutrient.foodItems) { // All foods in that category
				if (listedFood.isItemEqual(eatingFood)) {
					nutrientsFound.add(nutrient); // Add nutrient
					continue foodSearch; // Skip rest of search in this nutrient, try others
				}
			}

			// Search ore dictionary
			for (String listedOreDict : nutrient.foodOreDict) { // All ore dicts in that nutrient
				for (ItemStack itemStack : OreDictionary.getOres(listedOreDict)) { // All items that match that oredict (eg. listAllmilk)
					if (itemStack.isItemEqual(eatingFood)) { // Our food matches oredict
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
	public static float calculateNutrition(ItemStack itemStack, List<Nutrient> nutrients) {
		// Get item/block
		Item item = itemStack.getItem();

		// Base food value
		int foodValue = 0;
		if (item.isFood())
			foodValue = item.getFood().getHealing(); // Number of half-drumsticks food heals
		// TODO: Support cakes again
		//else if (item instanceof ItemBlock || item instanceof ItemBlockSpecial) // Cake, most likely
		//	foodValue = 2; // Hardcoded value from vanilla
		else if (item instanceof MilkBucketItem)
			foodValue = 4; // Hardcoded milk value

		// Apply multipliers
		float adjustedFoodValue = (float) (foodValue * 0.5); // Halve to start at reasonable starting point
		adjustedFoodValue = adjustedFoodValue * Config.nutritionMultiplier; // Multiply by config value
		float lossPercentage = (float) Config.lossPerNutrient / 100; // Loss percentage from config file
		float foodLoss = (adjustedFoodValue * lossPercentage * (nutrients.size() - 1)); // Lose 15% (configurable) for each nutrient added after the first nutrient
		float nutritionValue = Math.max(0, adjustedFoodValue - foodLoss); // Subtract from true value, with a floor of 0

		return nutritionValue;
	}

	// Verify it meets a valid type
	// Little bit of guesswork in this one...
	public static boolean isValidFood(ItemStack itemStack) {
		Item item = itemStack.getItem();

		// Regular ItemFood
		if (item.isFood())
			return true;

		// Cake - Vanilla
		// TODO: Support cakes again
		//if (item instanceof ItemBlock && ((ItemBlock) item).getBlock() instanceof BlockCake)
			//return true;

		// Cake - Modded
		// TODO: Support cakes again
		//if (item instanceof ItemBlockSpecial && ((ItemBlockSpecial) item).getBlock() instanceof BlockCake)
			//return true;

		// Milk Bucket
		if (item instanceof MilkBucketItem)
			return true;

		return false;
	}

	// List all foods registered in-game without nutrients
	public static void findRegisteredFoods() {
		for (Item item : ForgeRegistries.ITEMS) {
			ItemStack itemStack = new ItemStack(item);
			if (isValidFood(itemStack) && getFoodNutrients(itemStack).size() == 0)
				Log.warn("Registered food without nutrients: " + item.getRegistryName());
		}
	}
}
