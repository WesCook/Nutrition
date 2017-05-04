package ca.wescook.nutrition.events;

import ca.wescook.nutrition.configs.Config;
import ca.wescook.nutrition.nutrition.Nutrient;
import ca.wescook.nutrition.nutrition.NutrientList;
import ca.wescook.nutrition.nutrition.NutritionProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventNutritionDecay {
    private int Inc = 0;
    @SubscribeEvent
    public void PlayerTickEvent(TickEvent.PlayerTickEvent event) {

        // Only run on server
        EntityPlayer player = event.player;
        if (player.getEntityWorld().isRemote) { return; }

        int nutritionDecay = Config.nutritionDecay;
        int nutritionHunger = Config.nutritionHunger;

        if (Inc>=nutritionDecay) {
            if (player.getFoodStats().getFoodLevel()<=nutritionHunger) {
                for (Nutrient nutrient : NutrientList.get()) {
                    player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).subtract(nutrient, 0.1F); // Update player nutrition
                }
            }

            Inc = 0;
        }
        Inc++;
    }
}
