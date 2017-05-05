package ca.wescook.nutrition.configs;

import java.util.ArrayList;
import java.util.List;

// This class mimics the layout of the json files
public class FoodGroup {
	public String name;
	public String icon;
	public String color;
	Food food = new Food();

	public class Food {
		List<String> oredict = new ArrayList<>();
		List<String> items = new ArrayList<>();
	}
}
