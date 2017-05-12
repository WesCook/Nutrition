package ca.wescook.nutrition.nutrition;

import ca.wescook.nutrition.configs.Config;
import ca.wescook.nutrition.network.ModPacketHandler;
import ca.wescook.nutrition.network.PacketNutritionRequest;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;

import java.util.HashMap;
import java.util.Map;

// Default implementation of Capability.  Contains logic for each method defined in the Interface.
public class Nutrition implements INutrition {
	// Map Nutrient type to value for that nutrient
	private Map<Nutrient, Float> playerNutrition = new HashMap<>();

	public Nutrition() {
		// Populate nutrient data with starting nutrition
		for (Nutrient nutrient : NutrientList.get())
			playerNutrition.put(nutrient, (float) Config.startingNutrition);
	}

	public Map<Nutrient, Float> get() {
		return playerNutrition;
	}

	public Float get(Nutrient nutrient) {
		return playerNutrition.get(nutrient);
	}

	public void set(Map<Nutrient, Float> nutrientData) {
		this.playerNutrition = nutrientData;
		resync();
	}

	public void set(Nutrient nutrient, Float value) {
		playerNutrition.put(nutrient, value);
		resync();
	}

	public void add(Nutrient nutrient, float amount) {
		float currentAmount = playerNutrition.get(nutrient);
		playerNutrition.put(nutrient, Math.min(currentAmount + amount, 100));
		resync();
	}

	public void subtract(Nutrient nutrient, float amount) {
		float currentAmount = playerNutrition.get(nutrient);
		playerNutrition.put(nutrient, Math.max(currentAmount - amount, 0));
		resync();
	}

	public void deathPenalty() {
		for (Nutrient nutrient : playerNutrition.keySet()) // Loop through player's nutrients
			set(nutrient, Math.max(Config.deathPenaltyMin, playerNutrition.get(nutrient) - Config.deathPenaltyLoss)); // Subtract death penalty to each, with a bottom cap
		resync();
	}

	public void resync() {
		ModPacketHandler.NETWORK_CHANNEL.sendToServer(new PacketNutritionRequest.Message()); // Send nutrition sync request
	}
}
