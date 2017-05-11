package ca.wescook.nutrition.events;

import ca.wescook.nutrition.configs.Config;
import ca.wescook.nutrition.effects.Effect;
import ca.wescook.nutrition.effects.EffectsList;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.nutrition.NutritionProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;

public class EventPlayerUpdate {
	// Tracking how often these events trigger
	private int decayCounter = 0;
	private int potionCounter = 0;

	@SubscribeEvent
	public void PlayerTickEvent(TickEvent.PlayerTickEvent event) {
		// Only run on server
		EntityPlayer player = event.player;
		if (player.getEntityWorld().isRemote)
			return;

		if (Config.enableDecay)
			nutritionDecay(player);
		potionCheck(player);
	}

	private void nutritionDecay(EntityPlayer player) {
		if (decayCounter >= Config.decayRate) { // When the elapsed tick count reaches the configured value, trigger payload
			if (player.getFoodStats().getFoodLevel() <= Config.decayHungerLevel) { // When the food level of the player is below the threshold
				for (Nutrient nutrient : NutrientList.get()) // Cycle through nutrient list
					player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).subtract(nutrient, 0.1F); // And update player nutrition
				decayCounter = 0;
			}
		}
		decayCounter++;
	}

	private void potionCheck(EntityPlayer player) {
		// Run every 10 seconds
		if (potionCounter > 200) {
			// Get info
			Map<Nutrient, Float> playerNutrition = player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).get();

			// Track states
			Float total;
			Float specificNutrient;
			boolean allWithinThreshold;

			// Read in list of potion effects to apply
			for (Effect effect : EffectsList.get()) {
				// Apply effect based on "detect" condition
				switch (effect.detect) {
					// If any nutrient is within the threshold
					case "any": {
						setPotionStatus(player, effect, false);
						for (Map.Entry<Nutrient, Float> entry : playerNutrition.entrySet()) {
							if (entry.getValue() > effect.minimum && entry.getValue() < effect.maximum)
								setPotionStatus(player, effect, true);
						}
					}
					break;

					// If the average of all nutrients is within the threshold
					case "average": {
						total = 0f;
						for (Map.Entry<Nutrient, Float> entry : playerNutrition.entrySet())
							total += entry.getValue();
						if (total > effect.minimum && total < effect.maximum)
							setPotionStatus(player, effect, true);
						else
							setPotionStatus(player, effect, false);
					}
					break;

					// If all nutrients are within the threshold
					case "all": {
						allWithinThreshold = true;
						for (Map.Entry<Nutrient, Float> entry : playerNutrition.entrySet()) {
							if (!(entry.getValue() > effect.minimum && entry.getValue() < effect.maximum)) // Fail check
								allWithinThreshold = false;
						}
						if (allWithinThreshold)
							setPotionStatus(player, effect, true);
						else
							setPotionStatus(player, effect, false);
					}
					break;

					// If specific nutrient is within the threshold
					case "nutrient": {
						specificNutrient = playerNutrition.get(effect.nutrient);
						if (specificNutrient > effect.minimum && specificNutrient < effect.maximum)
							setPotionStatus(player, effect, true);
						else
							setPotionStatus(player, effect, false);
					}
					break;
				}
			}

			potionCounter = 0;
		}
		potionCounter++;
	}

	// Apply (or remove) a potion effect from a player
	private void setPotionStatus(EntityPlayer player, Effect effect, boolean apply) {
		if (apply)
			player.addPotionEffect(effect.potionEffect);
		else if (player.isPotionActive(effect.potion))
			player.removePotionEffect(effect.potion);
	}
}
