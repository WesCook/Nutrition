package ca.wescook.nutrition.events;

import ca.wescook.nutrition.capabilities.CapInterface;
import ca.wescook.nutrition.capabilities.CapProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventPlayerClone {
	// Copy player nutrition when "cloned" (death, teleport from End)
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		// Only run on server
		EntityPlayer player = event.getEntityPlayer();
		if (player.world.isRemote)
			return;

		// Duplicate player nutrition on warp/death
		CapInterface nutritionOld = event.getOriginal().getCapability(CapProvider.NUTRITION_CAPABILITY, null); // Get old nutrition
		CapInterface nutritionNew = player.getCapability(CapProvider.NUTRITION_CAPABILITY, null); // Get new nutrition
		nutritionNew.set(nutritionOld.get(), true); // Overwrite nutrition

		// On death, apply nutrition penalty
		if (event.isWasDeath())
			player.getCapability(CapProvider.NUTRITION_CAPABILITY, null).deathPenalty();
	}
}
