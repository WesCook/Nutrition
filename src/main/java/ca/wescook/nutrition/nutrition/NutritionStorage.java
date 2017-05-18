package ca.wescook.nutrition.nutrition;

import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.utility.Config;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.util.HashMap;

// Saves and loads serialized data from disk
public class NutritionStorage implements Capability.IStorage<INutrition> {
	// Save serialized data to disk
	@Override
	public NBTBase writeNBT(Capability<INutrition> capability, INutrition instance, EnumFacing side) {
		NBTTagCompound playerData = new NBTTagCompound();
		for (Nutrient nutrient : NutrientList.get())
			playerData.setFloat(nutrient.name, instance.get(nutrient));
		return playerData;
	}

	// Load serialized data from disk
	@Override
	public void readNBT(Capability<INutrition> capability, INutrition instance, EnumFacing side, NBTBase nbt) {
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
		instance.set(clientNutrients);
	}
}
