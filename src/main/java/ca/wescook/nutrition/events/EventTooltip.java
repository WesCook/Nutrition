package ca.wescook.nutrition.events;

import ca.wescook.nutrition.Nutrition;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.StringJoiner;

public class EventTooltip {
	@SubscribeEvent
	public void tooltipEvent(ItemTooltipEvent event) {
		ItemStack itemStack = event.getItemStack();
		String tooltip = null;

		// Get out if not a food item
		if (!NutrientUtils.isValidFood(itemStack))
			return;

		// Create readable list of nutrients
		StringJoiner stringJoiner = new StringJoiner(", ");
		List<Nutrient> foundNutrients = NutrientUtils.getFoodNutrients(itemStack);
		for (Nutrient nutrient : foundNutrients) // Loop through nutrients from food
			if (nutrient.visible)
				stringJoiner.add(I18n.format("nutrient." + Nutrition.MODID + ":" + nutrient.name));
		String nutrientString = stringJoiner.toString();

		// Get nutrition value
		float nutritionValue = NutrientUtils.calculateNutrition(itemStack, foundNutrients);

		// Build tooltip
		if (!nutrientString.equals("")) {
			tooltip = I18n.format("tooltip." + Nutrition.MODID + ":nutrients") + " " +
			TextFormatting.DARK_GREEN + nutrientString +
			TextFormatting.DARK_AQUA + " (" + String.format("%.1f", nutritionValue) + "%)";
		}

		// Add to item tooltip
		if (tooltip != null)
			event.getToolTip().add(tooltip);
	}
}
