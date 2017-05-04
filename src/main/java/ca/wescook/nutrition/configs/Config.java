package ca.wescook.nutrition.configs;

import ca.wescook.nutrition.Nutrition;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class Config {
	public static boolean testBool;
	public static int nutritionDecay;
	public static int nutritionHunger;

	public static void registerConfigs(File configDirectory) {
		registerPrimaryConfig(configDirectory); // Main nutrition.cfg file
		registerFoodGroupsDirectory(configDirectory); // /nutrition/ directory
	}

	private static void registerPrimaryConfig(File configDirectory) {
		// Create or load from file
		Configuration configFile = new Configuration(new File(configDirectory.getPath(), Nutrition.MODID + ".cfg"));
		configFile.load();

		// Get Values
		testBool = configFile.getBoolean("TestBool", CATEGORY_GENERAL, true, "Test boolean");
		nutritionDecay = configFile.getInt("NutritionDecayDelay", CATEGORY_GENERAL, 400, 100, 1000,"The delay in game ticks before the next decay check is made.");
		nutritionHunger = configFile.getInt("NutritionDecayHunger", CATEGORY_GENERAL, 10,0,20,"The hunger level you need to be down to before decay occurs.");

		// Update file
		if (configFile.hasChanged())
			configFile.save();
	}

	private static void registerFoodGroupsDirectory(File configDirectory) {
		// Specify /nutrition directory
		File nutritionDirectory = new File(configDirectory, Nutrition.MODID);

		// Make no changes if directory already exists
		if (nutritionDirectory.exists())
			return;

		// Create config directory
		nutritionDirectory.mkdir();

		// Default json files list
		List<String> files = new ArrayList<>();
		files.add("dairy.json");
		files.add("fruit.json");
		files.add("grain.json");
		files.add("protein.json");
		files.add("vegetable.json");

		// Copy each file over
		ClassLoader loader = Thread.currentThread().getContextClassLoader(); // Can access resources via class loader
		for (String file : files) {
			try (InputStream inputStream = loader.getResourceAsStream("assets/nutrition/foodgroups/" + file)) { // Get input stream of file
				Files.copy(inputStream, new File(nutritionDirectory + "/" + file).toPath()); // Create files from stream
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
