package ca.wescook.nutrition.utility;

import ca.wescook.nutrition.capabilities.INutrientManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientData {
	public static INutrientManager localNutrition; // Holds local copy of data/methods for client-side prediction
	public static KeyBinding keyNutritionGui;
}
