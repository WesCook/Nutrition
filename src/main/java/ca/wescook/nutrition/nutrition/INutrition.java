package ca.wescook.nutrition.nutrition;

import java.util.Map;

// Capability Interface that describes what methods the Implementations should understand.
public interface INutrition {
	// Return all nutrients
	Map<Nutrient, Float> get();

	// Return specific nutrient
	Float get(Nutrient nutrient);

	// Overwrite all nutrients
	void set(Map<Nutrient, Float> nutrientData);

	// Increase nutrition
	void add(Nutrient nutrient, float amount);

	// Decrease nutrition
	void subtract(Nutrient nutrient, float amount);

	// Penalize all skills on death
	void deathPenalty();
}
