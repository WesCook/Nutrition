package ca.wescook.nutrition.utility;

import ca.wescook.nutrition.Nutrition;
import ca.wescook.nutrition.effects.EffectsList;
import ca.wescook.nutrition.effects.JsonEffect;
import ca.wescook.nutrition.nutrients.JsonNutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.io.FilenameUtils;

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
	public static float decayMultiplier;
	public static int deathPenaltyMin;
	public static boolean deathPenaltyReset;
	public static int deathPenaltyLoss;
	public static boolean allowOverEating;
	public static int lossPerNutrient;
	public static float nutritionMultiplier;
	public static int startingNutrition;
	public static int maxDrinkFoodValue;
	public static int maxDrinkPenalty;
	public static int startingDrinkPenalty;
	public static boolean enableGui;
	public static boolean enableGuiButton;
	public static boolean enableTooltips;
	public static int buttonXPosition;
	public static int buttonYPosition;
	public static boolean logMissingFood;
	public static boolean logMissingNutrients;

	// Categories
	private static final String CATEGORY_NUTRITION = "Nutrition";
	private static final String CATEGORY_DECAY = "Nutrition Decay";
	private static final String CATEGORY_DEATH_PENALTY = "Death Penalty";
	private static final String CATEGORY_GUI = "Gui";
	private static final String CATEGORY_LOGGING = "Logging";

	public static void registerConfigs(File configDirectory) {
		// Register primary config
		registerPrimaryConfig(configDirectory);

		// Nutrients
		List<String> nutrientFiles = Lists.newArrayList("dairy.json", "example.json", "fruit.json", "grain.json", "protein.json", "vegetable.json");
		File nutrientDirectory = new File(configDirectory, Nutrition.MODID + "/nutrients");
		createConfigurationDirectory("assets/nutrition/configs/nutrients", nutrientDirectory, nutrientFiles);
		NutrientList.register(readConfigurationDirectory(JsonNutrient.class, nutrientDirectory));

		// Effects
		List<String> effectsFiles = Lists.newArrayList("example.json", "mining_fatigue.json", "resistance.json", "strength.json", "toughness.json", "weakness.json");
		File effectsDirectory = new File(configDirectory, Nutrition.MODID + "/effects");
		createConfigurationDirectory("assets/nutrition/configs/effects", effectsDirectory, effectsFiles);
		EffectsList.register(readConfigurationDirectory(JsonEffect.class, effectsDirectory));
	}

	private static void registerPrimaryConfig(File configDirectory) {
		// Create or load from file
		Configuration configFile = new Configuration(new File(configDirectory.getPath() + "/nutrition/nutrition.cfg"));
		configFile.load();

		// Get Values
		enableDecay = configFile.getBoolean("EnableDecay", CATEGORY_DECAY, true, "Enable nutrition decay when hunger drains.");
		decayMultiplier = configFile.getFloat("DecayMultiplier", CATEGORY_DECAY, 1, -100, 100, "Global value to multiply decay rate by (eg. 0.5 halves the rate, 2.0 doubles it).  This can also be set per-nutrient.");

		deathPenaltyMin = configFile.getInt("DeathPenaltyMin", CATEGORY_DEATH_PENALTY, 30, 0, 100, "The minimum nutrition value that the death penalty may reduce to.");
		deathPenaltyReset = configFile.getBoolean("DeathPenaltyReset", CATEGORY_DEATH_PENALTY, true, "On death, should nutrition be reset to DeathPenaltyMin if it's fallen below that value?  This is recommended to prevent death loops caused by negative effects.");
		deathPenaltyLoss = configFile.getInt("DeathPenaltyLoss", CATEGORY_DEATH_PENALTY, 15, 0, 100, "The nutrition value subtracted from each nutrient upon death.");

		nutritionMultiplier = configFile.getFloat("NutritionMultiplier", CATEGORY_NUTRITION, 1, 0, 100, "Value to multiply base nutrition by for each food (eg. 0.5 to halve nutrition gain).");
		startingNutrition = configFile.getInt("StartingNutrition", CATEGORY_NUTRITION, 50, 0, 100, "The starting nutrition level for new players.");
		lossPerNutrient = configFile.getInt("LossPerNutrient", CATEGORY_NUTRITION, 15, 0, 100,
			"The nutrition value subtracted from foods per additional nutrient, as a percentage.\n" +
			"This is to prevent large, complex foods from being too powerful.\n" +
			"(eg. 1 nutrient = 0% loss, 2 nutrients = 15% loss, 3 nutrients = 30% loss)");
		allowOverEating = configFile.getBoolean("AllowOverEating", CATEGORY_NUTRITION, false, "Allow player to continue eating even while full.\n" +
			"This setting may upset balance, but is necessary for playing in peaceful mode.");

		maxDrinkFoodValue = configFile.getInt("MaxDrinkFoodValue", CATEGORY_NUTRITION, 8, 0, 100, "Base nutrition value for drinkable items that do not grant food haunches (eg. Milk). This value is reduced after a drink is consumed and gradually returns to full value after a delay. Also affected by NutritionMultiplier.");
		maxDrinkPenalty = configFile.getInt("MaxDrinkPenalty", CATEGORY_NUTRITION, 12000, 0, 24000*30, "Number of game ticks players should wait to get maximum food value from drinks, where 24000 is one Minecraft day (bigger values make drinks worse for nutrition).");
		startingDrinkPenalty = configFile.getInt("startingDrinkPenalty", CATEGORY_NUTRITION, 0, 0, 24000*30, "The starting drink penalty for players, in game ticks.");
		
		enableGui = configFile.getBoolean("EnableGui", CATEGORY_GUI, true, "If the nutrition GUI should be enabled");
		enableGuiButton = configFile.getBoolean("EnableGuiButton", CATEGORY_GUI, true, "If the nutrition button should be shown on player inventory (hotkey will still function).");
		enableTooltips = configFile.getBoolean("EnableTooltips", CATEGORY_GUI, true, "If foods should show their nutrients on hover.");
		buttonXPosition = configFile.getInt("ButtonXPosition", CATEGORY_GUI, 134, 0, 500, "The nutrition button's X position.");
		buttonYPosition = configFile.getInt("ButtonYPosition", CATEGORY_GUI, 61, 0, 500, "The nutrition button's Y position.");

		logMissingFood = configFile.getBoolean("LogMissingFood", CATEGORY_LOGGING, false, "Log foods which have not been registered but are still listed in nutrients.");
		logMissingNutrients = configFile.getBoolean("LogMissingNutrients", CATEGORY_LOGGING, false, "Log foods which are registered but do not have any nutrients.");

		// Update file
		if (configFile.hasChanged())
			configFile.save();
	}

	// Copies files from internal resources to external files.  Accepts an input resource path, output directory, and list of files
	private static void createConfigurationDirectory(String inputDirectory, File outputDirectory, List<String> files) {
		// Make no changes if directory already exists
		if (outputDirectory.exists())
			return;

		// Create config directory
		outputDirectory.mkdir();

		// Copy each file over
		ClassLoader loader = Thread.currentThread().getContextClassLoader(); // Can access resources via class loader
		for (String file : files) {
			try (InputStream inputStream = loader.getResourceAsStream(inputDirectory + "/" + file)) { // Get input stream of resource
				Files.copy(inputStream, new File(outputDirectory + "/" + file).toPath()); // Create files from stream
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Reads in JSON as objects.  Accepts object to serialize into, and directory to read json files.  Returns array of JSON objects.
	private static <T> List<T> readConfigurationDirectory(Class<T> classImport, File configDirectory) {
		File[] files = configDirectory.listFiles(); // List json files
		List<T> jsonObjectList = new ArrayList<>(); // List json objects

		for (File file : files) {
			if (FilenameUtils.isExtension(file.getName(), "json")) {
				try {
					JsonReader jsonReader = new JsonReader(new FileReader(file)); // Read in JSON
					jsonObjectList.add(gson.fromJson(jsonReader, classImport)); // Deserialize with GSON and store for later processing
				} catch (IOException | com.google.gson.JsonSyntaxException e) {
					Log.fatal("The file " + file.getName() + " has invalid JSON and could not be loaded.");
					throw new IllegalArgumentException("Unable to load " + file.getName() + ".  Is the JSON valid?", e);
				}
			}
		}

		return jsonObjectList;
	}
}
