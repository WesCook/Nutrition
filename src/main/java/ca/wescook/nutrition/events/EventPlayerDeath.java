package ca.wescook.nutrition.events;

import ca.wescook.nutrition.capabilities.INutrientManager;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.utility.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public class EventPlayerDeath {
	@CapabilityInject(INutrientManager.class)
	private static final Capability<INutrientManager> NUTRITION_CAPABILITY = null;

	// Copy player nutrition when "cloned" (death, teleport from End)
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		PlayerEntity player = event.getPlayer();

		// Duplicate nutrition capability data on server
		// TODO: Need to find way to update this code
		INutrientManager nutritionOld = event.getOriginal().getCapability(NUTRITION_CAPABILITY); // Get old nutrition
		INutrientManager nutritionNew = player.getCapability(NUTRITION_CAPABILITY); // Get new nutrition
		nutritionNew.set(nutritionOld.get()); // Overwrite nutrition

		// On death, apply nutrition penalty
		// This is synced automatically in EventPlayerJoinWorld#EntityJoinWorldEvent
		if (event.isWasDeath()) {
			Map<Nutrient, Float> nutrition = player.getCapability(NUTRITION_CAPABILITY, null).get();
			for (Nutrient nutrient : nutrition.keySet())
				if (Config.deathPenaltyReset || nutrition.get(nutrient) > Config.deathPenaltyMin) // If reset is disabled, only reduce to cap when above its value
					player.getCapability(NUTRITION_CAPABILITY).ifPresent(cap -> cap.set(nutrient, Math.max(Config.deathPenaltyMin, nutrition.get(nutrient) - Config.deathPenaltyLoss))); // Subtract death penalty from each nutrient, to cap
		}
	}
}
