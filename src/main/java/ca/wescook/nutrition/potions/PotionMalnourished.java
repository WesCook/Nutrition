package ca.wescook.nutrition.potions;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;

public class PotionMalnourished extends PotionCustom {

	PotionMalnourished(boolean visibility, ResourceLocation icon) {
		super(visibility, icon);
	}

	// Multiply effects based on amplifier
	@Override
	public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier) {
		// Reduce health
		if (modifier.getID().equals(ModPotions.MALNOURISHMENT_HEALTH))
			return 0 - (amplifier + 1) * 2D; // 2 = one heart

		// Reduce attack speed
		if (modifier.getID().equals(ModPotions.MALNOURISHMENT_ATTACK_SPEED))
			return 0 - (amplifier + 1) * 0.1D;

		return 0D;
	}
}
