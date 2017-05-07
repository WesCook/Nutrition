package ca.wescook.nutrition.configs;

import ca.wescook.nutrition.Nutrition;
import ca.wescook.nutrition.nutrients.NutrientJson;
import ca.wescook.nutrition.nutrients.NutrientList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Config {
	private static final Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();

	// Public config values
	public static boolean enableDecay;
	public static int decayRate;
	public static int decayHungerLevel;
	public static int deathPenaltyMin;
	public static int deathPenaltyLoss;
	public static boolean enableLogging;

	// Categories
	private static final String CATEGORY_DECAY = "Nutrition Decay";
	private static final String CATEGORY_DEATH_PENALTY = "Death Penalty";
	private static final String CATEGORY_LOGGING = "Logging";

	public static void registerConfigs(File configDirectory) {
		registerPrimaryConfig(configDirectory); // Main nutrition.cfg file
		createFoodGroupsDirectory(configDirectory); // Create /nutrition directory
		readFoodGroupsDirectory(configDirectory);
	}

	private static void registerPrimaryConfig(File configDirectory) {
		// Create or load from file
		Configuration configFile = new Configuration(new File(configDirectory.getPath(), Nutrition.MODID + ".cfg"));
		configFile.load();

		// Get Values
		enableDecay = configFile.getBoolean("EnableDecay", CATEGORY_DECAY, true, "Enable nutrition decay.");
		decayRate = configFile.getInt("DecayRate", CATEGORY_DECAY, 400, 100, 1000, "The speed that nutrition decays in game ticks (lower is faster).");
		decayHungerLevel = configFile.getInt("DecayHungerLevel", CATEGORY_DECAY, 10, 0, 20, "The hunger level at which nutrition decay begins taking effect (one value is half a drumstick).");
		deathPenaltyMin = configFile.getInt("DeathPenaltyMin", CATEGORY_DEATH_PENALTY, 30, 0, 100, "The minimum nutrition value that the death penalty may reduce to.");
		deathPenaltyLoss = configFile.getInt("DeathPenaltyLoss", CATEGORY_DEATH_PENALTY, 15, 0, 100, "The nutrition value subtracted from each nutrient upon death.");
		enableLogging = configFile.getBoolean("EnableLogging", CATEGORY_LOGGING, false, "Enable logging of missing or invalid foods.");

		// Update file
		if (configFile.hasChanged())
			configFile.save();
	}

	private static void createFoodGroupsDirectory(File configDirectory) {
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
			try (InputStream inputStream = loader.getResourceAsStream("assets/nutrition/nutrients/" + file)) { // Get input stream of file
				Files.copy(inputStream, new File(nutritionDirectory + "/" + file).toPath()); // Create files from stream
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void readFoodGroupsDirectory(File configDirectory) {
		File[] files = new File(configDirectory, Nutrition.MODID).listFiles(); // List json files
		for (File file : files) {
			try {
				JsonReader jsonReader = new JsonReader(new FileReader(file)); // Read in JSON
				NutrientList.registerNutrientJson(gson.fromJson(jsonReader, NutrientJson.class)); // Deserialize with GSON and store for later processing
			} catch (IOException | com.google.gson.JsonSyntaxException e) {
				System.out.println("The file " + file.getName() + " has invalid JSON and could not be loaded.");
				e.printStackTrace();
			}
		}
	}
}
