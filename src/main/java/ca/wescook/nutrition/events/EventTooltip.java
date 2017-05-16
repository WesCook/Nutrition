package ca.wescook.nutrition.events;

import ca.wescook.nutrition.Nutrition;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.StringJoiner;

public class EventTooltip {
	@SubscribeEvent
	public void tooltipEvent(ItemTooltipEvent event) {
		// Get out if not food item
		Item item = event.getItemStack().getItem();
		if (!(item instanceof ItemFood))
			return;

		ItemFood food = (ItemFood) item;

		// Create readable list of nutrients
		StringJoiner stringJoiner = new StringJoiner(", ");
		List<Nutrient> foundNutrients = NutrientUtils.getFoodNutrients(food);
		for (Nutrient nutrient : foundNutrients) // Loop through nutrients from food
			stringJoiner.add(I18n.format("nutrient." + Nutrition.MODID + ":" + nutrient.name));
		String nutrientString = stringJoiner.toString();

		// Get nutrition value
		float nutritionValue = NutrientUtils.calculateNutrition(food, foundNutrients);

		// Add tooltip
		if (!nutrientString.equals(""))
			event.getToolTip().add(I18n.format("tooltip." + Nutrition.MODID + ":nutrients") + " " + nutrientString + " (" + String.format("%.1f", nutritionValue) + "%)");
	}
}
