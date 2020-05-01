package ca.wescook.nutrition.events;

import ca.wescook.nutrition.potions.ModPotions;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventRegisterPotions {
	@SubscribeEvent
	public void registerPotions(RegistryEvent.Register<Potion> event) {
		ModPotions.createPotions();
		event.getRegistry().register(new Potion(new EffectInstance(ModPotions.toughness)));
		event.getRegistry().register(new Potion(new EffectInstance(ModPotions.nourished)));
		event.getRegistry().register(new Potion(new EffectInstance(ModPotions.malnourished)));
	}
}
