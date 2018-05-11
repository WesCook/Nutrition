package ca.wescook.nutrition.events;

import ca.wescook.nutrition.capabilities.INutrientManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventPlayerClone {
	@CapabilityInject(INutrientManager.class)
	private final Capability<INutrientManager> NUTRITION_CAPABILITY = null;

	// Copy player nutrition when "cloned" (death, teleport from End)
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		// Only run on server
		EntityPlayer player = event.getEntityPlayer();
		if (player.world.isRemote)
			return;

		// Duplicate player nutrition on warp/death
		INutrientManager nutritionOld = event.getOriginal().getCapability(NUTRITION_CAPABILITY, null); // Get old nutrition
		INutrientManager nutritionNew = player.getCapability(NUTRITION_CAPABILITY, null); // Get new nutrition
		nutritionNew.set(nutritionOld.get()); // Overwrite nutrition

		// On death, apply nutrition penalty
		if (event.isWasDeath())
			player.getCapability(NUTRITION_CAPABILITY, null).deathPenalty();
	}
}
