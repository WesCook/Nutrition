package ca.wescook.nutrition.events;

import ca.wescook.nutrition.Nutrition;
import ca.wescook.nutrition.nutrition.NutritionProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventPlayerAttachCapability {
	@SubscribeEvent
	public void AttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();

		// Only run on server
		if (entity.getEntityWorld().isRemote)
			return;

		// Attach capability to player
		if (entity instanceof EntityPlayer)
			event.addCapability(new ResourceLocation(Nutrition.MODID, "nutrition"), new NutritionProvider());
	}
}
