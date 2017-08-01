package ca.wescook.nutrition.events;

import ca.wescook.nutrition.potions.ModPotions;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventRegistry {
	@SubscribeEvent
	public void registerPotions(RegistryEvent.Register<Potion> event) {
		event.getRegistry().register(ModPotions.toughness);
		event.getRegistry().register(ModPotions.nourished);
		event.getRegistry().register(ModPotions.malnourished);
	}
}
