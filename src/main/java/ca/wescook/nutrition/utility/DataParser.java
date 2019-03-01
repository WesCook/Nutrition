package ca.wescook.nutrition.utility;

import ca.wescook.nutrition.effects.Effect;
import ca.wescook.nutrition.effects.JsonEffect;
import ca.wescook.nutrition.nutrients.JsonNutrient;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.nutrients.NutrientUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.Loader;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

public class DataParser {
	// Accepts a list of raw JSON objects, which are returned as cleaned Nutrients
	public static List<Nutrient> parseNutrients(List<JsonNutrient> jsonNutrients) {
		List<Nutrient> nutrients = new ArrayList<>();

		for (JsonNutrient nutrientRaw : jsonNutrients) {
			// Skip if nutrient is not enabled, or if field omitted (null)
			if (nutrientRaw.enabled != null && !nutrientRaw.enabled)
				continue;

			// Copying and cleaning data
			Nutrient nutrient = new Nutrient();

			// Name, icon color
			try {
				nutrient.name = nutrientRaw.name;
				nutrient.icon = new ItemStack(Item.getByNameOrId(nutrientRaw.icon)); // Create ItemStack used to represent icon
				nutrient.color = Integer.parseUnsignedInt("ff" + nutrientRaw.color, 16); // Convert hex string to int
			} catch (NullPointerException e) {
				Log.fatal("Missing or invalid JSON.  A name, icon, and color are required.");
				throw e;
			}

			// Decay rate multiplier
			// Determined either by global rate, or optional override in nutrient file
			if (nutrientRaw.decay == null)
				nutrient.decay = Config.decayMultiplier; // Set to global value
			else if (nutrientRaw.decay >= -100 && nutrientRaw.decay <= 100)
				nutrient.decay = nutrientRaw.decay;
			else {
				nutrient.decay = 0;
				Log.error("Decay rate must be between -100 and 100 (" + nutrient.name + ").");
				continue;
			}

			// Nutrient Visibility
			nutrient.visible = (nutrientRaw.visible == null || nutrientRaw.visible);

			// Food - Ore Dictionary
			if (nutrientRaw.food.oredict != null)
				nutrient.foodOreDict = nutrientRaw.food.oredict; // Ore dicts remains as strings

			// Food Items
			if (nutrientRaw.food.items != null) {
				for (String fullName : nutrientRaw.food.items) {
					// Initial values
					String name = fullName;
					int metadata = 0;

					// Null check input string
					if (name == null) {
						Log.fatal("There is a null item in the '" + nutrient.name + "' JSON.  Check for a trailing comma in the file.");
						throw new NullPointerException("There is a null item in the '" + nutrient.name + "' JSON.  Check for a trailing comma in the file.");
					}

					// If string includes meta data, update name/meta
					if (StringUtils.countMatches(fullName, ":") == 2) { // Two colons for metadata (eg. minecraft:golden_apple:1)
						// Get data
						name = StringUtils.substringBeforeLast(fullName, ":");
						String metaString = StringUtils.substringAfterLast(fullName, ":");

						// Is valid metadata
						if (NumberUtils.isCreatable(metaString))
							metadata = Integer.decode(metaString);
						else {
							Log.warn(fullName + " does not contain valid metadata");
							continue;
						}
					}

					// Get item
					Item item = Item.getByNameOrId(name);

					// Item ID not found, issue warning and skip adding item
					if (item == null) {
						String modid = fullName.substring(0, fullName.indexOf(":")); // Get string before first colon
						if (Config.logMissingFood && Loader.isModLoaded(modid))
							Log.warn("Food with nutrients doesn't exist: " + fullName + " (" + nutrient.name + ")");
						continue;
					}

					// Add to nutrient, or report error
					ItemStack itemStack = new ItemStack(item, 1, metadata);
					if (NutrientUtils.isValidFood(itemStack))
						nutrient.foodItems.add(itemStack);
					else
						Log.warn(name + " is not a valid food (" + fullName + ")");
				}
			}

			// Register nutrient
			nutrients.add(nutrient);
		}

		return nutrients;
	}

	// Accepts a list of raw JSON objects, which are returned as cleaned Effects
	public static List<Effect> parseEffects(List<JsonEffect> jsonEffects) {
		List<Effect> effects = new ArrayList<>();

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

			// Find enum from string, default to TRANSPARENT if not defined
			try {
				effect.particles = (effectRaw.particles != null ? Effect.ParticleVisibility.valueOf(effectRaw.particles.toUpperCase()) : Effect.ParticleVisibility.TRANSPARENT);
			}
			catch(java.lang.IllegalArgumentException exception) {
				Log.error("Particle visibility of '" + effect.name + "' is invalid.  Skipping effect.");
				continue;
			}

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

		return effects;
	}
}
