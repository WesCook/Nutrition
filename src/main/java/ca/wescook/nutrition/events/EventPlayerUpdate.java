package ca.wescook.nutrition.events;

import ca.wescook.nutrition.effects.EffectsManager;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.nutrition.NutritionProvider;
import ca.wescook.nutrition.utility.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventPlayerUpdate {
	private Integer foodLevel; // Track food level between ticks
	private int potionCounter = 0; // Count ticks to reapply potion effects

	@SubscribeEvent
	public void PlayerTickEvent(TickEvent.PlayerTickEvent event) {
		// Only run on server, and during end phase (post-vanilla)
		if (event.player.getEntityWorld().isRemote || event.phase != TickEvent.Phase.END)
			return;

		EntityPlayer player = event.player;

		// Apply decay check each tick
		if (Config.enableDecay) {
			nutritionDecay(player);
		}

		// Reapply potion effects every 5 seconds
		if (potionCounter > 100) {
			EffectsManager.reapplyEffects(player);
			potionCounter = 0;
		}
		potionCounter++;
	}

	private void nutritionDecay(EntityPlayer player) {
		// Get player food level
		int foodLevelNew = player.getFoodStats().getFoodLevel();

		// If food level has reduced, also lower nutrition
		if (foodLevel != null && foodLevelNew < foodLevel) {
			int difference = foodLevel - foodLevelNew;
			float decay = (float) (difference * 0.075 * Config.decayMultiplier); // Lower number for reasonable starting point, then apply multiplier from config
			for (Nutrient nutrient : NutrientList.get())
				player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).subtract(nutrient, decay);
		}

		// Update for the next pass
		foodLevel = foodLevelNew;
	}
}
