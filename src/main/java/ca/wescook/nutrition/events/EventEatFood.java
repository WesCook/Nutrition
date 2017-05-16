package ca.wescook.nutrition.events;

import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientUtils;
import ca.wescook.nutrition.nutrition.NutritionProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
		List<Nutrient> foundNutrients = NutrientUtils.getFoodNutrients(eatingFood); // Nutrient list for that food
		float nutritionValue = NutrientUtils.calculateNutrition(eatingFood, foundNutrients); // Nutrition value for that food

		// Add to each nutrition
		for (Nutrient nutrient : foundNutrients) {
			player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).add(nutrient, nutritionValue);
		}
	}
}
