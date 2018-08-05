package ca.wescook.nutrition.effects;

import java.util.ArrayList;
import java.util.List;

// Maintains information about effects (name, potion, nutrient conditions)
// Stored client and server-side
public class EffectsList {
	public static List<JsonEffect> jsonEffects = new ArrayList<>(); // Raw deserialized data from JSON
	public static List<Effect> effects = new ArrayList<>(); // Parsed effects list

	// Register list of JSON objects
	public static void register(List<JsonEffect> jsonEffectsIn) {
		jsonEffects.clear();
		jsonEffects.addAll(jsonEffectsIn);
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
