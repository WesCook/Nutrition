package ca.wescook.nutrition.capabilities;

import ca.wescook.nutrition.nutrients.Nutrient;

import java.util.List;
import java.util.Map;

// Capability Interface that describes what methods the Implementations should understand.
// This is essentially our API, internally and externally
public interface INutrientManager {
	// Return all nutrients
	Map<Nutrient, Float> get();

	// Return specific nutrient
	Float get(Nutrient nutrient);

	// Overwrite all nutrients
	void set(Map<Nutrient, Float> nutrientData);

	// Overwrite specific nutrient
	void set(Nutrient nutrient, Float value);

	// Increase nutrition of specific nutrient
	void add(Nutrient nutrient, float amount);

	// Increase nutrition of list of nutrients
	void add(List<Nutrient> nutrientData, float amount);

	// Decrease nutrition of specific nutrient
	void subtract(Nutrient nutrient, float amount);

	// Decrease nutrition of list of nutrients
	void subtract(List<Nutrient> nutrientData, float amount);

	// Penalize all skills on death
	void deathPenalty();
}
