package ca.wescook.nutrition.effects;

import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrition.NutritionProvider;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EffectsManager {
	// Called from EventPlayerUpdate.
	public static void potionManage(EntityPlayer player) {
		List<Effect> effects = removeDuplicates(getEffectsInThreshold(player));
		applyEffects(player, effects);
	}

	// Returns which effects match threshold conditions
	private static List<Effect> getEffectsInThreshold(EntityPlayer player) {
		// Get info
		Map<Nutrient, Float> playerNutrition = player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).get();

		// Track states
		List<Effect> effectsInThreshold = new ArrayList<>();
		Float total;
		Float average;
		Float specificNutrient;
		boolean allWithinThreshold;

		// Read in list of potion effects to apply
		for (Effect effect : EffectsList.get()) {
			// Apply effect based on "detect" condition
			switch (effect.detect) {
				// If any nutrient is within the threshold
				case "any": {
					// Loop all nutrients
					for (Map.Entry<Nutrient, Float> entry : playerNutrition.entrySet()) {
						// If any are found within threshold
						if (entry.getValue() >= effect.minimum && entry.getValue() <= effect.maximum) {
							effectsInThreshold.add(effect); // Add effect, once
							break;
						}
					}
				}
				break;

				// If the average of all nutrients is within the threshold
				case "average": {
					// Reset counter each new loop
					total = 0f;

					// Loop all nutrients
					for (Map.Entry<Nutrient, Float> entry : playerNutrition.entrySet())
						total += entry.getValue(); // Add each value to total

					// Divide by number of nutrients for average (division by zero check)
					average = (playerNutrition.size() != 0) ? total / playerNutrition.size() : -1f;

					// Check average is inside the threshold
					if (average >= effect.minimum && average <= effect.maximum)
						effectsInThreshold.add(effect);
				}
				break;

				// If all nutrients are within the threshold
				case "all": {
					// Condition starts true, and must be triggered to fail
					allWithinThreshold = true;

					// Loop all nutrients
					for (Map.Entry<Nutrient, Float> entry : playerNutrition.entrySet()) {
						if (!(entry.getValue() >= effect.minimum && entry.getValue() <= effect.maximum)) // If nutrient isn't within threshold
							allWithinThreshold = false; // Fail check
					}

					// If check wasn't failed, set effect
					if (allWithinThreshold)
						effectsInThreshold.add(effect);
				}
				break;

				// If specific nutrient is within the threshold
				case "nutrient": {
					specificNutrient = playerNutrition.get(effect.nutrient);
					if (specificNutrient >= effect.minimum && specificNutrient <= effect.maximum)
						effectsInThreshold.add(effect);
				}
				break;
			}
		}

		return effectsInThreshold;
	}

	// Determines highest amplifier for duplicates, and removes them
	private static List<Effect> removeDuplicates(List<Effect> effectsInput) {
		List<Effect> effectsOutput = new ArrayList<>();
		boolean foundMatch = false;
		for (Effect effectIn : effectsInput) { // Loop through supplied effects
			for (Effect effectOut : effectsOutput) { // Loop through curated list, if it exists
				if (effectIn.potion == effectOut.potion) { // Potion types match (eg. Weakness I and Weakness II)
					if (effectIn.potionEffect.getAmplifier() > effectOut.potionEffect.getAmplifier()) { // New effect has a higher amplifier
						int listIndex = effectsOutput.indexOf(effectOut); // Get index of position in list
						effectsOutput.add(listIndex, effectIn); // Replace entry
					}
					foundMatch = true;
					break;
				}
			}

			// If potion wasn't already found, add to list
			if (!foundMatch)
				effectsOutput.add(effectIn);
		}

		return effectsOutput;
	}

	// Applies the actual effects to the player
	private static void applyEffects(EntityPlayer player, List<Effect> effectsQueued) {
//		for (Effect effect : EffectsList.get()) {
//			// If effect is queued, apply it
//			if (effectsQueued.contains(effect))
//				player.addPotionEffect(effect.potionEffect);
//		}
	}
}
