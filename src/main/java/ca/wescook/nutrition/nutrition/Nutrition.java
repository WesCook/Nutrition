package ca.wescook.nutrition.nutrition;

import java.util.HashMap;
import java.util.Map;

// Default implementation of Capability.  Contains logic for each method defined in the Interface.
public class Nutrition implements INutrition {
	// Map Nutrient type to value for that nutrient
	private Map<Nutrient, Float> playerNutrition = new HashMap<Nutrient, Float>();

	// Constants
	private final float STARTING_NUTRITION = 50;
	private final float DEATH_LOSS = 15;

	public Nutrition() {
		// Populate nutrient data with starting nutrition
		for (Nutrient nutrient : NutrientList.get())
			playerNutrition.put(nutrient, STARTING_NUTRITION);
	}

	public Map<Nutrient, Float> get() {
		return playerNutrition;
	}

	public Float get(Nutrient nutrient) {
		return playerNutrition.get(nutrient);
	}

	public void set(Map<Nutrient, Float> nutrientData) {
		this.playerNutrition = nutrientData;
	}

	public void add(Nutrient nutrient, float amount) {
		float currentAmount = playerNutrition.get(nutrient);
		playerNutrition.put(nutrient, Math.min(currentAmount + amount, 100));
	}

	public void subtract(Nutrient nutrient, float amount) {
		float currentAmount = playerNutrition.get(nutrient);
		playerNutrition.put(nutrient, Math.max(currentAmount - amount, 0));
	}

	public void deathPenalty() {
		for (Nutrient nutrient : playerNutrition.keySet()) // Loop through player's nutrients
			subtract(nutrient, DEATH_LOSS); // Subtract death penalty to each
	}
}
