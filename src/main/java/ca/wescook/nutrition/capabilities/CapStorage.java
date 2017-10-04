package ca.wescook.nutrition.capabilities;

import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.utility.Config;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.util.HashMap;

// Saves and loads serialized data from disk
public class CapStorage implements Capability.IStorage<CapInterface> {
	private static final String DRINK_PENALTY_STRING = "drinkPenalty";
	
	// Save serialized data to disk
	@Override
	public NBTBase writeNBT(Capability<CapInterface> capability, CapInterface instance, EnumFacing side) {
		NBTTagCompound playerData = new NBTTagCompound();
		for (Nutrient nutrient : NutrientList.get())
			playerData.setFloat(nutrient.name, instance.get(nutrient));
		playerData.setFloat(DRINK_PENALTY_STRING, instance.getDrinkPenalty());
		return playerData;
	}

	// Load serialized data from disk
	@Override
	public void readNBT(Capability<CapInterface> capability, CapInterface instance, EnumFacing side, NBTBase nbt) {
		HashMap<Nutrient, Float> clientNutrients = new HashMap<Nutrient, Float>();
		Float value;

		// Read in nutrients from file
		for (Nutrient nutrient : NutrientList.get()) { // For each nutrient
			if (((NBTTagCompound) nbt).hasKey(nutrient.name)) // If it's found in player file
				value = ((NBTTagCompound) nbt).getFloat(nutrient.name); // Read value in
			else
				value = (float) Config.startingNutrition; // Set to default

			// Add to map
			clientNutrients.put(nutrient, value);
		}

		// Replace nutrient data with map
		// Note: Syncing throws network errors at this stage
		instance.set(clientNutrients, false);

		// Read drink penalty from file
		if (((NBTTagCompound) nbt).hasKey(DRINK_PENALTY_STRING))
			value = ((NBTTagCompound) nbt).getFloat(DRINK_PENALTY_STRING);
		else
			value = (float) 0; //TODO use config for default
		instance.setDrinkPenalty(value, false);
	}
}
