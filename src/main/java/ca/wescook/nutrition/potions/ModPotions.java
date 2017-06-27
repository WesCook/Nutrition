package ca.wescook.nutrition.potions;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.UUID;

// A simple potion implementation to avoid visible rendering in the inventory screen
public class ModPotions {
	public static PotionToughness toughness;
	public static PotionMalnourished malnourished;
	static final UUID TOUGHNESS_HEALTH = UUID.fromString("d80b5ec3-8cf9-4b74-bc0d-6f3ef7b48b2e");
	static final UUID TOUGHNESS_ARMOR = UUID.fromString("f42431e4-8efc-44d2-8249-fea2a2cb418e");
	static final UUID TOUGHNESS_ATTACK_SPEED = UUID.fromString("10d42c27-f160-4909-a523-9b2553e14eac");
	static final UUID MALNOURISHMENT_HEALTH = UUID.fromString("ea9cebf7-7c7a-4a89-a04f-221dab8ffdf7");
	static final UUID MALNOURISHMENT_ATTACK_SPEED = UUID.fromString("a5db5ff0-3c49-4804-888b-6932430298d2");

	public static void registerPotions() {
		// Toughness
		toughness = new PotionToughness(true, new ResourceLocation("nutrition", "textures/potions/toughness.png"));
		toughness.setPotionName("Toughness");
		toughness.setRegistryName("toughness");
		toughness.setBeneficial();
		toughness.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, TOUGHNESS_HEALTH.toString(), 0D, 0);
		toughness.registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR_TOUGHNESS, TOUGHNESS_ARMOR.toString(), 0D, 0);
		toughness.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, TOUGHNESS_ATTACK_SPEED.toString(), 0D, 0);
		GameRegistry.register(toughness);

		// Malnourished
		malnourished = new PotionMalnourished(true, new ResourceLocation("nutrition", "textures/potions/malnourished.png"));
		malnourished.setPotionName("Malnourished");
		malnourished.setRegistryName("malnourished");
		malnourished.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, MALNOURISHMENT_HEALTH.toString(), 0D, 0);
		malnourished.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, MALNOURISHMENT_ATTACK_SPEED.toString(), 0D, 0);
		GameRegistry.register(malnourished);
	}
}
