package ca.wescook.nutrition.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// TODO: Still in use?

public class PotionCustom extends Effect {
	private boolean visibility;
	private ResourceLocation icon;

	PotionCustom(EffectType typeIn, int liquidColorIn) {
		super(typeIn, liquidColorIn);
	}

	@Override
	public boolean shouldRender(EffectInstance effect) {
		return visibility;
	}

	// Inventory potion rendering
	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderInventoryEffect(EffectInstance effect, Gui gui, int x, int y, float z) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.currentScreen != null) {
			mc.getTextureManager().bindTexture(icon);
			Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
		}
	}

	// On-screen HUD rendering
	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderHUDEffect(EffectInstance effect, Gui gui, int x, int y, float z, float alpha) {
		Minecraft.getInstance().getTextureManager().bindTexture(icon);
		Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
	}
}
