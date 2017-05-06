package ca.wescook.nutrition.events;

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

		// The item we're eating
		ItemFood eatingFood = (ItemFood) event.getItem().getItem();

		// Loop through nutrients to look for food
		foodSearch:
		for (Nutrient nutrient : NutrientList.get()) { // All nutrients
			// Search food items
			for (ItemFood listedFood : nutrient.foodItems) { // All foods in that nutrient
				if (listedFood.equals(eatingFood)) {
					int nutrientIncrease = eatingFood.getHealAmount(new ItemStack(eatingFood)); // Must pass ItemStack in as well, otherwise Raw Fish crash the game
					player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).add(nutrient, nutrientIncrease); // Add nutrient
					continue foodSearch; // Skip rest of search in this nutrient, try others
				}
			}

			// Search ore dictionary
			for (String listedOreDict : nutrient.foodOreDict) { // All ore dicts in that nutrient
				for (ItemStack testingItemStack : OreDictionary.getOres(listedOreDict)) { // All items that match that oredict (eg. listAllmilk)
					Item testingItem = testingItemStack.getItem(); // Get real item
					if (testingItem.equals(eatingFood)) { // Our food matches oredict
						int nutrientIncrease = eatingFood.getHealAmount(new ItemStack(eatingFood));
						player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).add(nutrient, nutrientIncrease); // Add nutrient
						continue foodSearch; // Skip rest of search in this nutrient, try others
					}
				}
			}
		}
	}
}
