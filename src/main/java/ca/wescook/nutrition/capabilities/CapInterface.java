package ca.wescook.nutrition.capabilities;

import ca.wescook.nutrition.nutrients.Nutrient;

import java.util.List;
import java.util.Map;

// Capability Interface that describes what methods the Implementations should understand.
public interface CapInterface {
	// Return all nutrients
	Map<Nutrient, Float> get();

	// Return specific nutrient
	Float get(Nutrient nutrient);

	// Overwrite all nutrients
	void set(Map<Nutrient, Float> nutrientData, boolean sync);

	// Overwrite specific nutrient
	void set(Nutrient nutrient, Float value, boolean sync);

	// Increase nutrition of specific nutrient
	void add(Nutrient nutrient, float amount, boolean sync);

	// Increase nutrition of list of nutrients
	void add(List<Nutrient> nutrientData, float amount, boolean sync);

	// Decrease nutrition of specific nutrient
	void subtract(Nutrient nutrient, float amount, boolean sync);

	// Decrease nutrition of list of nutrients
	void subtract(List<Nutrient> nutrientData, float amount, boolean sync);

	// Penalize all skills on death
	void deathPenalty();

	// Sync nutrition data to local dummy
	void resync();
}
