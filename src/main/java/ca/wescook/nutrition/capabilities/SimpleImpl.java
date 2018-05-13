package ca.wescook.nutrition.capabilities;

import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.utility.Config;
import com.google.common.primitives.Floats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Baseline implementation of Capability
// Contains basic logic for each method defined in the Interface
public class SimpleImpl implements INutrientManager {
	// Stored nutrition for the attached player
	private Map<Nutrient, Float> nutrition = new HashMap<>();

	SimpleImpl() {
		// Populate nutrient data with starting nutrition
		for (Nutrient nutrient : NutrientList.get())
			nutrition.put(nutrient, (float) Config.startingNutrition);
	}

	public Map<Nutrient, Float> get() {
		return nutrition;
	}

	public Float get(Nutrient nutrient) {
		return nutrition.get(nutrient);
	}

	public void set(Nutrient nutrient, Float value) {
		nutrition.put(nutrient, value);
	}

	public void set(Map<Nutrient, Float> nutrientData) {
		for (Map.Entry<Nutrient, Float> entry : nutrientData.entrySet())
			nutrition.put(entry.getKey(), entry.getValue());
	}

	public void add(Nutrient nutrient, float amount) {
		float currentAmount = nutrition.get(nutrient);
		nutrition.put(nutrient, Floats.constrainToRange(currentAmount + amount, 0, 100));
	}

	public void add(List<Nutrient> nutrientData, float amount) {
		for (Nutrient nutrient : nutrientData)
			nutrition.put(nutrient, Floats.constrainToRange(nutrition.get(nutrient) + amount, 0, 100));
	}

	public void subtract(Nutrient nutrient, float amount) {
		float currentAmount = nutrition.get(nutrient);
		nutrition.put(nutrient, Floats.constrainToRange(currentAmount - amount, 0, 100));
	}

	public void subtract(List<Nutrient> nutrientData, float amount) {
		for (Nutrient nutrient : nutrientData)
			nutrition.put(nutrient, Floats.constrainToRange(nutrition.get(nutrient) - amount, 0, 100));
	}

	public void deathPenalty() {
		for (Nutrient nutrient : nutrition.keySet()) // Loop through player's nutrients
			if (Config.deathPenaltyReset || get(nutrient) > Config.deathPenaltyMin) // If reset is disabled, only reduce to cap when above its value
				set(nutrient, Math.max(Config.deathPenaltyMin, nutrition.get(nutrient) - Config.deathPenaltyLoss)); // Subtract death penalty from each nutrient, to cap
	}
}
