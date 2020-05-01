package ca.wescook.nutrition.potions;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;

public class PotionNourished extends PotionCustom {

	PotionNourished(boolean visibility, ResourceLocation icon) {
		super(EffectType.BENEFICIAL, 0);
	}

	// Multiply effect based on amplifier
	@Override
	public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier) {
		// Increase health
		if (modifier.getID().equals(ModPotions.NOURISHMENT_HEALTH))
			return (amplifier + 1); // Half-heart per level

		return 0D;
	}
}
