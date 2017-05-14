package ca.wescook.nutrition.effects;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionCustom extends Potion {
	private boolean visible;

	public PotionCustom(boolean visible) {
		super(false, 0);
		this.visible = visible;
	}

	@Override
	public boolean shouldRender(PotionEffect effect) {
		return visible;
	}
}
