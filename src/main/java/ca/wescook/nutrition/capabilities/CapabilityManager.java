package ca.wescook.nutrition.capabilities;

import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.utility.Config;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;
import java.util.HashMap;

public class CapabilityManager {
	@CapabilityInject(INutrientManager.class)
	private static final Capability<INutrientManager> NUTRITION_CAPABILITY = null;

	// Registers default capability implementation
	public static void register() {
		net.minecraftforge.common.capabilities.CapabilityManager.INSTANCE.register(INutrientManager.class, new Storage(), SimpleImpl::new);
	}


	// Capability Provider
	// Created when attaching to the player.  Handles serialization before saving, and holds instance.
	// Specific to our implementation
	public static class Provider implements ICapabilitySerializable<NBTBase> {
		private INutrientManager instance;

		public Provider() {
			instance = new SimpleImpl(); // Default implementation
		}

		// Check if capability exists
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			if (capability == NUTRITION_CAPABILITY)
				return true;
			return false;
		}

		// Return capability instance
		@Override
		public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
			if (capability == NUTRITION_CAPABILITY)
				return NUTRITION_CAPABILITY.cast(instance);
			return null;
		}

		// Serialize and write NBT
		@Override
		public NBTBase serializeNBT() {
			return NUTRITION_CAPABILITY.getStorage().writeNBT(NUTRITION_CAPABILITY, instance, null);
		}

		// Deserialize and read NBT
		@Override
		public void deserializeNBT(NBTBase nbt) {
			NUTRITION_CAPABILITY.getStorage().readNBT(NUTRITION_CAPABILITY, instance, null, nbt);
		}
	}


	// Capability Storage
	// Saves and loads serialized data from disk
	// Should work for default and custom implementations
	public static class Storage implements Capability.IStorage<INutrientManager> {
		// Save serialized data to disk
		@Override
		public NBTBase writeNBT(Capability<INutrientManager> capability, INutrientManager instance, EnumFacing side) {
			NBTTagCompound playerData = new NBTTagCompound();
			for (Nutrient nutrient : NutrientList.get())
				if (instance.get(nutrient) != null)
					playerData.setFloat(nutrient.name, instance.get(nutrient));
			return playerData;
		}

		// Load serialized data from disk
		@Override
		public void readNBT(Capability<INutrientManager> capability, INutrientManager instance, EnumFacing side, NBTBase nbt) {
			HashMap<Nutrient, Float> clientNutrients = new HashMap<>();
			float value;

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
			// Note: Triggers primarily on initial world load
			// Don't send network packets (syncing) yet because it throws an error
			instance.set(clientNutrients);
		}
	}
}
