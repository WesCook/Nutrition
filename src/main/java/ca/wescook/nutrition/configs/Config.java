package ca.wescook.nutrition.configs;

import ca.wescook.nutrition.Nutrition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class Config {
	public static boolean testBool;

	public static void registerConfigs(File configDirectory) {
		registerPrimaryConfig(configDirectory); // Main nutrition.cfg file
		try {
			registerFoodGroupsDirectory(configDirectory); // /nutrition/ directory
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void registerPrimaryConfig(File configDirectory) {
		// Create or load from file
		Configuration configFile = new Configuration(new File(configDirectory.getPath(), Nutrition.MODID + ".cfg"));
		configFile.load();

		// Get Values
		testBool = configFile.getBoolean("TestBool", CATEGORY_GENERAL, true, "Test boolean");

		// Update file
		if (configFile.hasChanged())
			configFile.save();
	}

	private static void registerFoodGroupsDirectory(File configDirectory) throws IOException {
		// Specify /nutrition directory
		File nutritionDirectory = new File(configDirectory, Nutrition.MODID);

		// Make no changes if directory already exists
		if (nutritionDirectory.exists())
			return;

		// Create config directory
		nutritionDirectory.mkdir();

		// Set up our objects
		ClassLoader loader = Thread.currentThread().getContextClassLoader(); // Can access resources via class loader
		StringWriter outputWriter = new StringWriter(); // Create writer object

		// Get directory content
		String[] fileList;
		try (InputStream inputStream = loader.getResourceAsStream("assets/nutrition/foodgroups")) { // Get input stream of directory contents
			IOUtils.copy(inputStream, outputWriter); // Copy stream to writer
			fileList = outputWriter.toString().split("\n"); // Read as string and split into file list
			outputWriter.getBuffer().setLength(0); // Clear writer data
			outputWriter.close(); // Close file
		}

		// Copy each file over
		for (String file : fileList) {
			try (InputStream inputStream = loader.getResourceAsStream("assets/nutrition/foodgroups/" + file)) { // Get input stream of file
				IOUtils.copy(inputStream, outputWriter); // Copy stream to writer
				FileUtils.writeStringToFile(new File(nutritionDirectory + "/" + file), outputWriter.toString()); // Write writer to file
				outputWriter.getBuffer().setLength(0); // Clear writer data
				outputWriter.close(); // Close file
			}
		}
	}
}
