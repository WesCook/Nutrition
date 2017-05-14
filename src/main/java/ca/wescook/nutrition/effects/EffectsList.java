package ca.wescook.nutrition.effects;

import ca.wescook.nutrition.nutrients.NutrientList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

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
			// Skip if potion is not enabled
			if (!effectRaw.enabled)
				break;

			// Get vanilla potion from config
			Potion vanillaPotion = Potion.getPotionFromResourceLocation(effectRaw.potion);
			if (vanillaPotion == null) {
				System.out.println("Potion '" + effectRaw.potion + "' is not valid (" + effectRaw.name + ").");
				return;
			}

			// Create custom potion
			Potion potion = new PotionCustom(true);
			potion.setPotionName(effectRaw.name);

			// Get attributes from vanilla potion and assign to custom potion (requires access transformer)
			potion.attributeModifierMap = vanillaPotion.attributeModifierMap;

			// Assign new attributes
			// TODO: UUID should probably be deterministic based on potion data
			//potion.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "f74ac47c-dfcc-400f-b245-bcccae9f2847", 10D, 0);

			// Create potion effect
			PotionEffect potionEffect = new PotionEffect(potion, Integer.MAX_VALUE, effectRaw.amplifier, true, true);

			// Copying and cleaning data
			Effect effect = new Effect();
			effect.name = effectRaw.name;
			effect.potion = potion;
			effect.potionEffect = potionEffect;
			effect.minimum = effectRaw.minimum;
			effect.maximum = effectRaw.maximum;
			effect.detect = effectRaw.detect;
			effect.hidden = effectRaw.hidden;

			// Assigning appropriate nutrient
			if (effect.detect.equals("nutrient")) {
				effect.nutrient = NutrientList.getByName(effectRaw.nutrient);
				if (effect.nutrient == null) {
					System.out.println("Nutrient '" + effectRaw.nutrient + "' cannot be found.");
					return;
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
