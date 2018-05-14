package ca.wescook.nutrition.events;

import ca.wescook.nutrition.capabilities.INutrientManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventPlayerDeath {
	@CapabilityInject(INutrientManager.class)
	private static final Capability<INutrientManager> NUTRITION_CAPABILITY = null;

	// Copy player nutrition when "cloned" (death, teleport from End)
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		EntityPlayer player = event.getEntityPlayer();

		// Duplicate nutrition capability data on server
		INutrientManager nutritionOld = event.getOriginal().getCapability(NUTRITION_CAPABILITY, null); // Get old nutrition
		INutrientManager nutritionNew = player.getCapability(NUTRITION_CAPABILITY, null); // Get new nutrition
		nutritionNew.set(nutritionOld.get()); // Overwrite nutrition

		// On death, apply nutrition penalty
		// This is synced automatically in EventPlayerJoinWorld#EntityJoinWorldEvent
		if (event.isWasDeath())
			player.getCapability(NUTRITION_CAPABILITY, null).deathPenalty();
	}
}
