package ca.wescook.nutrition.events;

import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.nutrition.NutritionProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

		// Add nutrition value
		ItemFood food = (ItemFood) event.getItem().getItem();
		if (food.equals(Items.BEETROOT)) { // TODO: Dynamic categories - only test item
			Nutrient vegetable = NutrientList.get("vegetable"); // Get relevant nutrient
			player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).add(vegetable, food.getHealAmount(null)); // Update player nutrition
		}
	}
}
