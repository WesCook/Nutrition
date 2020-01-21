package ca.wescook.nutrition.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionCustom extends Potion {
	private boolean visibility;
	private ResourceLocation icon;

	PotionCustom(boolean visibility, ResourceLocation icon) {
		super(false, 0);
		this.visibility = visibility;
		this.icon = icon;
	}

	@Override
	public boolean shouldRender(PotionEffect effect) {
		return visibility;
	}

	// Inventory potion rendering
	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.currentScreen != null) {
			mc.getTextureManager().bindTexture(icon);
			Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
		}
	}

	// On-screen HUD rendering
	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(icon);
		Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
	}
}
