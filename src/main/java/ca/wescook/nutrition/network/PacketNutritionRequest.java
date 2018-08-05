package ca.wescook.nutrition.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketNutritionRequest {
	// Message Subclass
	// Empty message just to request information
	public static class Message implements IMessage {
		public Message() {}

		@Override
		public void toBytes(ByteBuf buf) {}

		@Override
		public void fromBytes(ByteBuf buf) {}
	}

	// Message Handler Subclass
	// Handled on server
	public static class Handler implements IMessageHandler<Message, IMessage> {
		@Override
		public IMessage onMessage(final Message message, final MessageContext context) {
			FMLCommonHandler.instance().getWorldThread(context.netHandler).addScheduledTask(() -> {
				// Return message
				EntityPlayerMP player = context.getServerHandler().player; // Get Player on server
				ModPacketHandler.NETWORK_CHANNEL.sendTo(new PacketNutritionResponse.Message(player), player);
			});

			return null;
		}
	}
}
