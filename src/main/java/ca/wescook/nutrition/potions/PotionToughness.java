package ca.wescook.nutrition.potions;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;

public class PotionToughness extends PotionCustom {

	PotionToughness(boolean visibility, ResourceLocation icon) {
		super(visibility, icon);
	}

	// Multiply effects based on amplifier
	@Override
	public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier) {
		// Multiply health
		if (modifier.getID().equals(ModPotions.TOUGHNESS_HEALTH))
			return (amplifier + 1) * 4D; // 4 = two hearts

		// Multiply armor toughness
		if (modifier.getID().equals(ModPotions.TOUGHNESS_ARMOR))
			return (amplifier + 1) * 2D;

		// Multiply attack speed
		if (modifier.getID().equals(ModPotions.TOUGHNESS_ATTACK_SPEED))
			return (amplifier + 1) * 0.1D;

		return 0D;
	}
}
