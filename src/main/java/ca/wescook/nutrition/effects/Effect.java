package ca.wescook.nutrition.effects;

import ca.wescook.nutrition.nutrients.Nutrient;
import net.minecraft.potion.Potion;

import java.util.ArrayList;
import java.util.List;

// This class represents cleaned up and parsed potion effects
public class Effect {
	public String name;
	public Potion potion;
	public int amplifier;
	public int minimum;
	public int maximum;
	public String detect;
	public List<Nutrient> nutrients = new ArrayList<>();
	public int cumulativeModifier;
	public Enum<ParticleVisibility> particles;

	public enum ParticleVisibility {
		OPAQUE,
		TRANSLUCENT,
		TRANSPARENT
	}
}
