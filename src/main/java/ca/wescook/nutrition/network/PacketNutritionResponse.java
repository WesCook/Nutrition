package ca.wescook.nutrition.network;

import ca.wescook.nutrition.gui.ModGuiHandler;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.nutrition.NutritionProvider;
import ca.wescook.nutrition.proxy.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.HashMap;
import java.util.Map;

public class PacketNutritionResponse {
	// Message Subclass
	public static class Message implements IMessage {
		// Server vars only
		EntityPlayer serverPlayer;

		// Client vars only
		Map<Nutrient, Float> clientNutrients;

		public Message() {}

		// Message data is passed along from server
		public Message(EntityPlayer player) {
			serverPlayer = player; // Get server player
		}

		// Then serialized into bytes (on server)
		@Override
		public void toBytes(ByteBuf buf) {
			// Loop through nutrients and add to buffer
			for (Nutrient nutrient : NutrientList.get())
				buf.writeFloat(serverPlayer.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).get(nutrient));
		}

		// Then deserialized (on the client)
		@Override
		public void fromBytes(ByteBuf buf) {
			// Loop through nutrients to build accurate list from data
			clientNutrients = new HashMap<Nutrient, Float>();
			for (Nutrient nutrient : NutrientList.get())
				clientNutrients.put(nutrient, buf.readFloat());
		}
	}

	// Message Handler Subclass
	// This is the client response to the information
	public static class Handler implements IMessageHandler<Message, IMessage> {
		@Override
		public IMessage onMessage(final Message message, final MessageContext context) {
			FMLCommonHandler.instance().getWorldThread(context.netHandler).addScheduledTask(() -> {
				// Update local dummy nutrition data
				ClientProxy.nutrientData = message.clientNutrients;

				// If GUI is still open
				GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
				if (currentScreen == null || !currentScreen.equals(ModGuiHandler.nutritionGui)) {
					return;
				}

				// Update GUI information
				ModGuiHandler.nutritionGui.redrawLabels();
			});
			return null;
		}
	}
}
