package ca.wescook.nutrition.nutrition;

import ca.wescook.nutrition.nutrients.Nutrient;

import java.util.Map;

// Capability Interface that describes what methods the Implementations should understand.
public interface INutrition {
	// Return all nutrients
	Map<Nutrient, Float> get();

	// Return specific nutrient
	Float get(Nutrient nutrient);

	// Overwrite all nutrients
	void set(Map<Nutrient, Float> nutrientData);

	// Overwrite specific nutrient
	void set(Nutrient nutrient, Float value);

	// Increase nutrition
	void add(Nutrient nutrient, float amount);

	// Decrease nutrition
	void subtract(Nutrient nutrient, float amount);

	// Penalize all skills on death
	void deathPenalty();

	// Sync nutrition data to local dummy
	void resync();
}
