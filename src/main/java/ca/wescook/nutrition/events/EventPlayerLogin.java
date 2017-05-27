package ca.wescook.nutrition.events;

import ca.wescook.nutrition.capabilities.CapProvider;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class EventPlayerLogin {
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		// Only run on server
		if (event.player.worldObj.isRemote)
			return;

		// Sync nutrition data from server
		event.player.getCapability(CapProvider.NUTRITION_CAPABILITY, null).resync();
	}
}
