package ca.wescook.nutrition.nutrition;

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
		HashMap<Nutrient, Float> clientNutrients = new HashMap<Nutrient, Float>(); // Create new map
		for (Nutrient nutrient : NutrientList.get()) {
			Float value = ((NBTTagCompound) nbt).getFloat(nutrient.name); // Read values from packet
			clientNutrients.put(nutrient, value); // Add to map
		}
		instance.set(clientNutrients); // Replace nutrient data wih map
	}
}
