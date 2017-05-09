package ca.wescook.nutrition.nutrients;

import java.util.ArrayList;
import java.util.List;

// This class mimics the layout of the effect json files
public class JsonNutrient {
	public String name;
	public String icon;
	public String color;
	public Food food = new Food();

	public class Food {
		public List<String> oredict = new ArrayList<>();
		public List<String> items = new ArrayList<>();
	}
}
