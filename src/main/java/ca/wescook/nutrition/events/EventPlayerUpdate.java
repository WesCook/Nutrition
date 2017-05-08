package ca.wescook.nutrition.events;

import ca.wescook.nutrition.configs.Config;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.nutrition.NutritionProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventPlayerUpdate {
	// Tracking how often these events trigger
	private int decayCounter = 0;
	private int potionCounter = 0;

	private boolean weaknessActive;

	@SubscribeEvent
	public void PlayerTickEvent(TickEvent.PlayerTickEvent event) {
		// Only run on server
		EntityPlayer player = event.player;
		if (player.getEntityWorld().isRemote)
			return;

		if (Config.enableDecay)
			nutritionDecay(player);
		potionCheck(player);
	}

	private void nutritionDecay(EntityPlayer player) {
		if (decayCounter >= Config.decayRate) { // When the elapsed tick count reaches the configured value, trigger payload
			if (player.getFoodStats().getFoodLevel() <= Config.decayHungerLevel) { // When the food level of the player is below the threshold
				for (Nutrient nutrient : NutrientList.get()) // Cycle through nutrient list
					player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).subtract(nutrient, 0.1F); // And update player nutrition
				decayCounter = 0;
			}
		}
		decayCounter++;
	}

	private void potionCheck(EntityPlayer player) {
		// Run every 10 seconds
		if (potionCounter > 200) { // 10 seconds
			// Should effect be active?
			Float vegetable = player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).get(NutrientList.get("vegetable"));
			weaknessActive = (vegetable < 40); // TODO: Test, checks if veggie nutrition is below 40%

			// Apply effect
			if (weaknessActive)
				player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, Integer.MAX_VALUE, 0, true, true));
			else {
				if (player.isPotionActive(MobEffects.WEAKNESS))
					player.removePotionEffect(MobEffects.WEAKNESS);
			}

			potionCounter = 0;
		}
		potionCounter++;
	}
}
