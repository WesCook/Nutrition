package ca.wescook.nutrition.effects;

import ca.wescook.nutrition.nutrients.Nutrient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

// This class represents cleaned up and parsed potion effects
public class Effect {
	public String name;
	public Potion potion;
	public PotionEffect potionEffect;
	public int minimum;
	public int maximum;
	public String detect;
	public Nutrient nutrient;
	public boolean hidden;
}
