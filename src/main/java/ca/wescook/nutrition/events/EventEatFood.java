package ca.wescook.nutrition.events;

import ca.wescook.nutrition.configs.Config;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.nutrition.NutritionProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class EventEatFood {
	@SubscribeEvent
	public void finishUsingItem(LivingEntityUseItemEvent.Finish event) {
		checkFoodEaten(event);
	}

	// Checks which food has been eaten, and updates nutrition
	private void checkFoodEaten(LivingEntityUseItemEvent.Finish event) {
		// Only run on server
		EntityPlayer player = (EntityPlayer) event.getEntity();
		if (player.getEntityWorld().isRemote)
			return;

		// Get out if not food item
		Item item = event.getItem().getItem();
		if (!(item instanceof ItemFood))
			return;

		ItemFood eatingFood = (ItemFood) event.getItem().getItem(); // The item we're eating
		List<Nutrient> foundNutrients = getFoodNutrients(eatingFood, player); // Nutrient list for that item

		// Calculate nutrition value
		int foodValue = eatingFood.getHealAmount(new ItemStack(eatingFood)); // Number of half-drumsticks food heals
		float adjustedFoodValue = foodValue * Config.nutritionMultiplier; // Multiply by config value
		float lossPercentage = (float) Config.lossPerNutrient / 100; // Loss percentage from config file
		float foodLoss = (adjustedFoodValue * lossPercentage * (foundNutrients.size() - 1)); // Lose 15% (configurable) for each nutrient added after the first nutrient
		float nutritionValue = Math.max(1, adjustedFoodValue - foodLoss); // Subtract from true value, with a floor of 1 (prevent zero/negatives)

		// Add to each nutrition
		for (Nutrient nutrient : foundNutrients) {
			player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).add(nutrient, nutritionValue);
		}
	}

	// Returns list of nutrients that food belongs to
	private List<Nutrient> getFoodNutrients(ItemFood eatingFood, EntityPlayer player) {
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
}
