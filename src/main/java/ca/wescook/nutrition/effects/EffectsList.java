package ca.wescook.nutrition.effects;

import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.utility.Log;
import net.minecraft.potion.Potion;

import java.util.ArrayList;
import java.util.List;

public class EffectsList {
	private static List<JsonEffect> jsonEffects = new ArrayList<>(); // Raw deserialized data from JSON
	private static List<Effect> effects = new ArrayList<>(); // Parsed effects list

	// Register single JSON object
	public static void register(JsonEffect jsonEffectIn) {
		jsonEffects.add(jsonEffectIn);
	}

	// Register list of JSON objects
	public static void register(List<JsonEffect> jsonEffectsIn) {
		jsonEffects.addAll(jsonEffectsIn);
	}

	// Parse JSON data into more useful objects
	public static void parseJson() {
		for (JsonEffect effectRaw : jsonEffects) {
			// Skip if effect is not enabled, or if field omitted (null)
			if (effectRaw.enabled != null && !effectRaw.enabled)
				continue;

			// Get potion from config
			Potion potion = Potion.getPotionFromResourceLocation(effectRaw.potion);
			if (potion == null) {
				Log.error("Potion '" + effectRaw.potion + "' is not valid (" + effectRaw.name + ").");
				continue;
			}

			// Copying and cleaning data
			Effect effect = new Effect();
			effect.name = effectRaw.name;
			effect.potion = potion;
			effect.minimum = effectRaw.minimum;
			effect.maximum = effectRaw.maximum;
			effect.detect = effectRaw.detect;

			// Amplifier defaults to 0 if undefined
			effect.amplifier = (effectRaw.amplifier != null) ? effectRaw.amplifier : 0;

			// Default the cumulative modifier to 1 if not defined
			effect.cumulativeModifier = (effectRaw.cumulative_modifier != null) ? effectRaw.cumulative_modifier : 1;

			// Build list of applicable nutrients
			// If nutrients are unspecified in file, this defaults to include every nutrient
			if (effectRaw.nutrients.size() == 0) {
				effect.nutrients.addAll(NutrientList.get());
			} else { // Field has been set, so fetch nutrients by name
				for (String nutrientName : effectRaw.nutrients) {
					Nutrient nutrient = NutrientList.getByName(nutrientName);
					if (nutrient != null)
						effect.nutrients.add(nutrient); // Nutrient checks out, add to list
					else
						Log.error("Nutrient " + nutrientName + " not found (" + effectRaw.name + ").");
				}
			}

			// Register effect
			effects.add(effect);
		}
	}

	// Return all parsed effects
	public static List<Effect> get() {
		return effects;
	}

	// Return effect by name (null if not found)
	public static Effect getByName(String name) {
		for (Effect effect : effects) {
			if (effect.name.equals(name))
				return effect;
		}
		return null;
	}
}
