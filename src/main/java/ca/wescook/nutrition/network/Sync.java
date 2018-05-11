package ca.wescook.nutrition.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class Sync {
	// Server sends a nutrition update to client
	// Only call from server
	public static void serverRequest(EntityPlayer player) {
		if (!player.world.isRemote) // Server-only
			ModPacketHandler.NETWORK_CHANNEL.sendTo(new PacketNutritionResponse.Message(player), (EntityPlayerMP) player);
	}

	// Client requests a nutrition update from server
	// Only call from client
	public static void clientRequest() {
		ModPacketHandler.NETWORK_CHANNEL.sendToServer(new PacketNutritionRequest.Message());
	}
}
