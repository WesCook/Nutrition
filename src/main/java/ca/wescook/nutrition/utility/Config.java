package ca.wescook.nutrition.utility;

public class Config {
	// This file exists just to get us compiling
	// Then we can figure out the newfangled config system
	public static boolean enableDecay = true;
	public static float decayMultiplier = 1;
	public static int deathPenaltyMin = 30;
	public static boolean deathPenaltyReset = true;
	public static int deathPenaltyLoss = 15;
	public static boolean allowOverEating = true;
	public static int lossPerNutrient = 15;
	public static float nutritionMultiplier = 1;
	public static int startingNutrition = 50;
	public static boolean enableGui = true;
	public static boolean enableGuiButton = true;
	public static boolean enableTooltips = true;
	public static int buttonXPosition = 134;
	public static int buttonYPosition = 61;
	public static String buttonOrigin = "gui";
	public static String buttonAnchor = "top-left";
	public static boolean logMissingFood = true;
	public static boolean logMissingNutrients = true;
}
