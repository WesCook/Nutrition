package ca.wescook.nutrition.capabilities;

import ca.wescook.nutrition.nutrients.Nutrient;

import java.util.List;
import java.util.Map;

// Capability Interface that describes what methods the Implementations should understand.
// This is essentially our API, internally and externally
public interface INutrientManager {
	// Return all nutrients and values
	Map<Nutrient, Float> get();

	// Return value of specific nutrient
	Float get(Nutrient nutrient);

	// Set value of specific nutrient
	void set(Nutrient nutrient, Float value);

	// Update all nutrients
	void set(Map<Nutrient, Float> nutrientData);

	// Increase specific nutrient by amount
	void add(Nutrient nutrient, float amount);

	// Increase list of nutrients by amount
	void add(List<Nutrient> nutrientData, float amount);

	// Decrease specific nutrient by amount
	void subtract(Nutrient nutrient, float amount);

	// Decrease list of nutrients by amount
	void subtract(List<Nutrient> nutrientData, float amount);

	// Reset specific nutrient to default nutrition
	void reset(Nutrient nutrient);

	// Reset all nutrients to default nutrition
	void reset();

	// Internal: Called to update Nutrient object references in player's capability
	void updateCapability();
}
