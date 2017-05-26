package ca.wescook.nutrition.events;

import ca.wescook.nutrition.nutrition.NutritionProvider;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class EventPlayerLogin {
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		// Only run on server
		if (event.player.world.isRemote)
			return;

		// Sync nutrition data from server
		event.player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).resync();
	}
}
