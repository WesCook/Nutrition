package ca.wescook.nutrition.utility;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
	// Config path - populated in FMLPreInitializationEvent
	// Held onto because this is only given during pre-init, and we may need to access later if reloading changes
	public static File configDirectory;

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
	public static boolean enableGui;
	public static boolean enableGuiButton;
	public static boolean enableTooltips;
	public static int buttonXPosition;
	public static int buttonYPosition;
	public static String buttonOrigin;
	public static String buttonAnchor;
	public static boolean logMissingFood;
	public static boolean logMissingNutrients;

	// Categories
	private static final String CATEGORY_NUTRITION = "Nutrition";
	private static final String CATEGORY_DECAY = "Nutrition Decay";
	private static final String CATEGORY_DEATH_PENALTY = "Death Penalty";
	private static final String CATEGORY_GUI = "Gui";
	private static final String CATEGORY_LOGGING = "Logging";

	public static void registerConfigs(File configDirectory) {
		// Update config path for later reference
		Config.configDirectory = configDirectory;

		// Create or load from file
		Configuration configFile = new Configuration(new File(configDirectory.getPath() + "/nutrition/nutrition.cfg"));
		configFile.load();

		// Get Values
		enableDecay = configFile.getBoolean("EnableDecay", CATEGORY_DECAY, true, "Enable nutrition decay when hunger drains.");
		decayMultiplier = configFile.getFloat("DecayMultiplier", CATEGORY_DECAY, 1, -100, 100, "Global value to multiply decay rate by (eg. 0.5 halves the rate, 2.0 doubles it).  This can also be set per-nutrient.");

		deathPenaltyMin = configFile.getInt("DeathPenaltyMin", CATEGORY_DEATH_PENALTY, 30, 0, 100, "The minimum nutrition value that the death penalty may reduce to.");
		deathPenaltyReset = configFile.getBoolean("DeathPenaltyReset", CATEGORY_DEATH_PENALTY, true, "On death, should nutrition be reset to DeathPenaltyMin if it's fallen below that value?\n" +
			"This is recommended to prevent death loops caused by negative effects.");
		deathPenaltyLoss = configFile.getInt("DeathPenaltyLoss", CATEGORY_DEATH_PENALTY, 15, 0, 100, "The nutrition value subtracted from each nutrient upon death.");

		nutritionMultiplier = configFile.getFloat("NutritionMultiplier", CATEGORY_NUTRITION, 1, 0, 100, "Value to multiply base nutrition by for each food (eg. 0.5 to halve nutrition gain).");
		startingNutrition = configFile.getInt("StartingNutrition", CATEGORY_NUTRITION, 50, 0, 100, "The starting nutrition level for new players.");
		lossPerNutrient = configFile.getInt("LossPerNutrient", CATEGORY_NUTRITION, 15, 0, 100,
			"The nutrition value subtracted from foods per additional nutrient, as a percentage.\n" +
			"This is to prevent large, complex foods from being too powerful.\n" +
			"(eg. 1 nutrient = 0% loss, 2 nutrients = 15% loss, 3 nutrients = 30% loss)");
		allowOverEating = configFile.getBoolean("AllowOverEating", CATEGORY_NUTRITION, false, "Allow player to continue eating even while full.\n" +
			"This setting may upset balance (and tummies), but is necessary for playing in peaceful mode.");

		enableGui = configFile.getBoolean("EnableGui", CATEGORY_GUI, true, "If the nutrition GUI should be enabled");
		enableGuiButton = configFile.getBoolean("EnableGuiButton", CATEGORY_GUI, true, "If the nutrition button should be shown on player inventory (hotkey will still function).");
		enableTooltips = configFile.getBoolean("EnableTooltips", CATEGORY_GUI, true, "If foods should show their nutrients on hover.");
		buttonOrigin = configFile.getString("ButtonOrigin", CATEGORY_GUI, "gui", "The origin defines the object which the nutrition button will be placed relative to.\n" +
			"Accepted values: gui, screen");
		buttonAnchor = configFile.getString("ButtonAnchor", CATEGORY_GUI, "top-left", "The anchor defines which side of the origin to position the button against.\n" +
			"Accepted values: top, right, bottom, left, top-left, top-right, bottom-right, bottom-left, center");
		buttonXPosition = configFile.getInt("ButtonXPosition", CATEGORY_GUI, 134, -1000, 1000, "The nutrition button's X position, relative to its anchor point.");
		buttonYPosition = configFile.getInt("ButtonYPosition", CATEGORY_GUI, 61, -1000, 1000, "The nutrition button's Y position, relative to its anchor point.");

		logMissingFood = configFile.getBoolean("LogMissingFood", CATEGORY_LOGGING, false, "Log foods which cannot be found but are still listed in nutrients file.");
		logMissingNutrients = configFile.getBoolean("LogMissingNutrients", CATEGORY_LOGGING, false, "Log foods which have been found but do not have any associated nutrients.");

		// Update file
		if (configFile.hasChanged())
			configFile.save();
	}
}
