package ca.wescook.nutrition.capabilities;

import ca.wescook.nutrition.network.ModPacketHandler;
import ca.wescook.nutrition.network.PacketNutritionResponse;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.utility.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Real implementation of Capability.  Contains logic for each method defined in the Interface.
public class CapImplementation implements CapInterface {
	// Map Nutrient type to value for that nutrient
	private Map<Nutrient, Float> playerNutrition = new HashMap<>();
	private EntityPlayer player;

	CapImplementation(EntityPlayer player) {
		// Store player
		this.player = player;

		// Populate nutrient data with starting nutrition
		for (Nutrient nutrient : NutrientList.get())
			playerNutrition.put(nutrient, (float) Config.startingNutrition);
	}

	public Map<Nutrient, Float> get() {
		return playerNutrition;
	}

	public Float get(Nutrient nutrient) {
		return playerNutrition.get(nutrient);
	}

	public void set(Nutrient nutrient, Float value, boolean sync) {
		playerNutrition.put(nutrient, value);
		if (sync) resync();
	}

	public void set(Map<Nutrient, Float> nutrientData, boolean sync) {
		for (Map.Entry<Nutrient, Float> entry : nutrientData.entrySet())
			this.playerNutrition.put(entry.getKey(), entry.getValue());
		if (sync) resync();
	}

	public void add(Nutrient nutrient, float amount, boolean sync) {
		float currentAmount = playerNutrition.get(nutrient);
		playerNutrition.put(nutrient, Math.min(currentAmount + amount, 100));
		if (sync) resync();
	}

	public void add(List<Nutrient> nutrientData, float amount, boolean sync) {
		for (Nutrient nutrient : nutrientData)
			playerNutrition.put(nutrient, Math.min(playerNutrition.get(nutrient) + amount, 100));
		if (sync) resync();
	}

	public void subtract(Nutrient nutrient, float amount, boolean sync) {
		float currentAmount = playerNutrition.get(nutrient);
		playerNutrition.put(nutrient, Math.max(currentAmount - amount, 0));
		if (sync) resync();
	}

	public void subtract(List<Nutrient> nutrientData, float amount, boolean sync) {
		for (Nutrient nutrient : nutrientData)
			playerNutrition.put(nutrient, Math.max(playerNutrition.get(nutrient) - amount, 0));
		if (sync) resync();
	}

	public void deathPenalty() {
		for (Nutrient nutrient : playerNutrition.keySet()) // Loop through player's nutrients
			if (Config.deathPenaltyReset || get(nutrient) > Config.deathPenaltyMin) // If reset is disabled, only reduce to cap when above its value
				set(nutrient, Math.max(Config.deathPenaltyMin, playerNutrition.get(nutrient) - Config.deathPenaltyLoss), false); // Subtract death penalty from each nutrient, to cap
		resync();
	}

	public void resync() {
		if (!player.world.isRemote)
			ModPacketHandler.NETWORK_CHANNEL.sendTo(new PacketNutritionResponse.Message(player), (EntityPlayerMP) player);
	}
}
