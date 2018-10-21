package ca.wescook.nutrition.events;

import ca.wescook.nutrition.capabilities.INutrientManager;
import ca.wescook.nutrition.effects.EffectsManager;
import ca.wescook.nutrition.gui.ModGuiHandler;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.proxy.ClientProxy;
import ca.wescook.nutrition.utility.Config;
import com.google.common.primitives.Floats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class EventPlayerUpdate {
	@CapabilityInject(INutrientManager.class)
	private static final Capability<INutrientManager> NUTRITION_CAPABILITY = null;

	private Map<Pair<EntityPlayer, Boolean>, Integer> playerFoodLevels = new HashMap<>(); // Track food level between ticks
	private int potionCounter = 0; // Count ticks to reapply potion effects

	@SubscribeEvent
	public void PlayerTickEvent(TickEvent.PlayerTickEvent event) {
		// Only run during end phase (post-vanilla)
		if (event.phase != TickEvent.Phase.END)
			return;

		EntityPlayer player = event.player;

		// Apply decay check each tick
		if (Config.enableDecay)
			nutritionDecay(player);

		// Reapply potion effects every 5 seconds
		if (!event.player.getEntityWorld().isRemote) // Server only
			potionTicking(player);
	}

	private void nutritionDecay(EntityPlayer player) {
		// To prevent client/server conflicts, we use a unique ID that stores both the player and their side
		Pair<EntityPlayer, Boolean> playerSidedID = new ImmutablePair<>(player, player.getEntityWorld().isRemote);

		// Get player food levels
		int foodLevelNew = player.getFoodStats().getFoodLevel(); // Current food level
		Integer foodLevelOld = playerFoodLevels.get(playerSidedID); // Food level last tick

		// If food level has reduced, also lower nutrition
		if (foodLevelOld != null && foodLevelNew < foodLevelOld) {
			int difference = foodLevelOld - foodLevelNew; // Difference in food level

			// Server
			Map<Nutrient, Float> playerNutrition;
			if (!player.getEntityWorld().isRemote) {
				playerNutrition = player.getCapability(NUTRITION_CAPABILITY, null).get();
				playerNutrition = calculateDecay(playerNutrition, difference);
				player.getCapability(NUTRITION_CAPABILITY, null).set(playerNutrition);
			}
			// Client
			else {
				playerNutrition = ClientProxy.localNutrition.get();
				playerNutrition = calculateDecay(playerNutrition, difference);
				ClientProxy.localNutrition.set(playerNutrition);

				// If Nutrition GUI is open, update GUI
				GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
				if (currentScreen != null && currentScreen.equals(ModGuiHandler.nutritionGui))
					ModGuiHandler.nutritionGui.redrawLabels();
			}
		}

		// Update for the next pass
		playerFoodLevels.put(playerSidedID, foodLevelNew);
	}

	private Map<Nutrient, Float> calculateDecay(Map<Nutrient, Float> playerNutrition, int difference) {
		for (Map.Entry<Nutrient, Float> entry : playerNutrition.entrySet()) {
			float decay = (float) (difference * 0.075 * entry.getKey().decay); // Lower number for reasonable starting point, then apply multiplier from config
			entry.setValue(Floats.constrainToRange(entry.getValue() - decay, 0, 100)); // Subtract decay from nutrient
		}
		return playerNutrition;
	}

	private void potionTicking(EntityPlayer player) {
		if (potionCounter > 110) {
			EffectsManager.reapplyEffects(player);
			potionCounter = 0;
		}
		potionCounter++;
	}
}
