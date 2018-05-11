package ca.wescook.nutrition.capabilities;

import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.proxy.ClientProxy;

import java.util.List;
import java.util.Map;

// Nutrition's capability implementation
// Adds client-side prediction
public class NutritionImpl extends SimpleImpl {
	@Override
	public void set(Nutrient nutrient, Float value) {
		super.set(nutrient, value);
		updatePrediction();
	}

	@Override
	public void set(Map<Nutrient, Float> nutrientData) {
		super.set(nutrientData);
		updatePrediction();
	}

	@Override
	public void add(Nutrient nutrient, float amount) {
		super.add(nutrient, amount);
		updatePrediction();
	}

	@Override
	public void add(List<Nutrient> nutrientData, float amount) {
		super.add(nutrientData, amount);
		updatePrediction();
	}

	@Override
	public void subtract(Nutrient nutrient, float amount) {
		super.subtract(nutrient, amount);
		updatePrediction();
	}

	@Override
	public void subtract(List<Nutrient> nutrientData, float amount) {
		super.subtract(nutrientData, amount);
		updatePrediction();
	}

	@Override
	public void deathPenalty() {
		super.deathPenalty();
		updatePrediction();
	}

	/*****************************/

	// Updates client-side nutrition prediction
	private void updatePrediction() {
		ClientProxy.nutrientData = nutrition;
	}
}
