package ca.wescook.nutrition.capabilities;

import ca.wescook.nutrition.network.Sync;
import ca.wescook.nutrition.nutrients.Nutrient;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;
import java.util.Map;

// Nutrition's capability implementation
// Adds client-side prediction
public class NutritionImpl extends SimpleImpl {
	private EntityPlayer player;

	NutritionImpl(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public void set(Nutrient nutrient, Float value) {
		super.set(nutrient, value);
		sync();
	}

	@Override
	public void set(Map<Nutrient, Float> nutrientData) {
		super.set(nutrientData);
		sync();
	}

	@Override
	public void add(Nutrient nutrient, float amount) {
		super.add(nutrient, amount);
		sync();
	}

	@Override
	public void add(List<Nutrient> nutrientData, float amount) {
		super.add(nutrientData, amount);
		sync();
	}

	@Override
	public void subtract(Nutrient nutrient, float amount) {
		super.subtract(nutrient, amount);
		sync();
	}

	@Override
	public void subtract(List<Nutrient> nutrientData, float amount) {
		super.subtract(nutrientData, amount);
		sync();
	}

	@Override
	public void deathPenalty() {
		super.deathPenalty();
		sync();
	}

	/*****************************/

	// Updates client-side nutrition
	private void sync() {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[3]; // Three levels up, class where the method is called
		if (caller != null && !caller.getMethodName().equals("readNBT")) // If method being called is "readNBT" (triggers on world load), don't sync
			Sync.serverRequest(player);
	}
}
