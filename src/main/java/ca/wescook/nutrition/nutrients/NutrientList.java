package ca.wescook.nutrition.nutrients;

import java.util.ArrayList;
import java.util.List;

// Maintains information about nutrients (name, color, icon)
// Stored client and server-side
public class NutrientList {
	public static List<JsonNutrient> jsonNutrients = new ArrayList<>(); // Raw deserialized data from JSON
	public static List<Nutrient> nutrients = new ArrayList<>(); // Parsed nutrients list

	// Register list of JSON objects
	public static void register(List<JsonNutrient> jsonNutrientsIn) {
		jsonNutrients.clear();
		jsonNutrients.addAll(jsonNutrientsIn);
	}

	// Return all parsed nutrients
	public static List<Nutrient> get() {
		return nutrients;
	}

	// Return nutrient by name (null if not found)
	public static Nutrient getByName(String name) {
		for (Nutrient nutrient : nutrients) {
			if (nutrient.name.equals(name))
				return nutrient;
		}
		return null;
	}
}
