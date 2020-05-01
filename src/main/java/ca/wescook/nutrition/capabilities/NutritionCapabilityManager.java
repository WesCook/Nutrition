package ca.wescook.nutrition.capabilities;

import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.utility.Config;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.HashMap;

public class NutritionCapabilityManager {
	@CapabilityInject(INutrientManager.class)
	private static final Capability<INutrientManager> NUTRITION_CAPABILITY = null;

	// Capability Provider
	// Created when attaching to the player.  Handles serialization before saving, and holds instance.
	// Specific to our implementation
	public static class Provider implements ICapabilitySerializable<INBT> {
		private INutrientManager instance;

		public Provider() {
			instance = new SimpleImpl(); // Default implementation
		}

		// Return capability instance
		@Override
		public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
			if (capability == NUTRITION_CAPABILITY)
				//return instance.cast(instance);
				return LazyOptional.of(() -> NUTRITION_CAPABILITY).cast(); // TODO: Is this right?
			return LazyOptional.empty();
		}

		// Serialize and write NBT
		@Override
		public INBT serializeNBT() {
			return NUTRITION_CAPABILITY.getStorage().writeNBT(NUTRITION_CAPABILITY, instance, null);
		}

		// Deserialize and read NBT
		@Override
		public void deserializeNBT(INBT nbt) {
			NUTRITION_CAPABILITY.getStorage().readNBT(NUTRITION_CAPABILITY, instance, null, nbt);
		}
	}


	// Capability Storage
	// Saves and loads serialized data from disk
	// Should work for default and custom implementations
	public static class Storage implements Capability.IStorage<INutrientManager> {
		// Save serialized data to disk
		@Override
		public INBT writeNBT(Capability<INutrientManager> capability, INutrientManager instance, Direction direction) {
			CompoundNBT playerData = new CompoundNBT();
			for (Nutrient nutrient : NutrientList.get())
				if (instance.get(nutrient) != null)
					playerData.putFloat(nutrient.name, instance.get(nutrient));
			return playerData;
		}

		// Load serialized data from disk
		@Override
		public void readNBT(Capability<INutrientManager> capability, INutrientManager instance, Direction direction, INBT nbt) {
			HashMap<Nutrient, Float> clientNutrients = new HashMap<>();
			float value;

			// Read in nutrients from file
			for (Nutrient nutrient : NutrientList.get()) { // For each nutrient
				if (((CompoundNBT) nbt).contains(nutrient.name)) // If it's found in player file
					value = ((CompoundNBT) nbt).getFloat(nutrient.name); // Read value in
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
