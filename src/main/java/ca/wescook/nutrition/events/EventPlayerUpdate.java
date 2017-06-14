package ca.wescook.nutrition.events;

import ca.wescook.nutrition.capabilities.CapProvider;
import ca.wescook.nutrition.effects.EffectsManager;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.utility.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;

public class EventPlayerUpdate {
	private Map<EntityPlayer, Integer> playerFoodLevels = new HashMap<>(); // Track food level between ticks
	private int potionCounter = 0; // Count ticks to reapply potion effects

	@SubscribeEvent
	public void PlayerTickEvent(TickEvent.PlayerTickEvent event) {
		// Only run on server, and during end phase (post-vanilla)
		if (event.player.getEntityWorld().isRemote || event.phase != TickEvent.Phase.END)
			return;

		EntityPlayer player = event.player;

		// Apply decay check each tick
		if (Config.enableDecay)
			nutritionDecay(player);

		// Reapply potion effects every 5 seconds
		if (potionCounter > 100) {
			EffectsManager.reapplyEffects(player);
			potionCounter = 0;
		}
		potionCounter++;
	}

	private void nutritionDecay(EntityPlayer player) {
		// Get player food levels
		int foodLevelNew = player.getFoodStats().getFoodLevel(); // Current food level
		Integer foodLevelOld = playerFoodLevels.get(player); // Food level last tick

		// If food level has reduced, also lower nutrition
		if (foodLevelOld != null && foodLevelNew < foodLevelOld) {
			int difference = foodLevelOld - foodLevelNew;
			float decay = (float) (difference * 0.075 * Config.decayMultiplier); // Lower number for reasonable starting point, then apply multiplier from config
			player.getCapability(CapProvider.NUTRITION_CAPABILITY, null).subtract(NutrientList.get(), decay, true); // Subtract from every nutrient
		}

		// Update for the next pass
		playerFoodLevels.put(player, foodLevelNew);
	}
}
