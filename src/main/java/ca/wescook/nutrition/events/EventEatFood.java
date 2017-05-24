package ca.wescook.nutrition.events;

import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientUtils;
import ca.wescook.nutrition.nutrition.NutritionProvider;
import ca.wescook.nutrition.utility.Config;
import net.minecraft.block.BlockCake;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class EventEatFood {
	// Detect eating cake
	@SubscribeEvent
	public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		// Only run on server
		EntityPlayer player = (EntityPlayer) event.getEntity();
		if (player.getEntityWorld().isRemote)
			return;

		// Get info
		World world = event.getWorld();
		IBlockState blockState = world.getBlockState(event.getPos());

		// Get out if not cake
		if (!(blockState.getBlock() instanceof BlockCake)) {
			return;
		}

		// Should we let them eat cake?
		if (player.canEat(false) || Config.allowOverEating) {
			// Calculate nutrition
			Item item = Item.getByNameOrId(blockState.getBlock().getRegistryName().toString()); // Get cake Item from registry name
			ItemStack itemStack = new ItemStack(item);
			List<Nutrient> foundNutrients = NutrientUtils.getFoodNutrients(itemStack);
			float nutritionValue = NutrientUtils.calculateNutrition(itemStack, foundNutrients);

			// Add to each nutrient
			for (Nutrient nutrient : foundNutrients)
				player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).add(nutrient, nutritionValue);

			// If full but over-eating, simulate cake eating
			if (!player.canEat(false) && Config.allowOverEating) {
				int cakeBites = blockState.getValue(BlockCake.BITES);
				if (cakeBites < 6)
					world.setBlockState(event.getPos(), blockState.withProperty(BlockCake.BITES, cakeBites + 1), 3);
				else
					world.setBlockToAir(event.getPos());
			}
		}
	}

	// Allow food to be consumed regardless of hunger level
	@SubscribeEvent
	public void startUsingItem(PlayerInteractEvent.RightClickItem event) {
		// Only run on server
		EntityPlayer player = (EntityPlayer) event.getEntity();
		if (player.getEntityWorld().isRemote)
			return;

		// Interacting with item?
		ItemStack itemStack = event.getItemStack();
		if (itemStack == null)
			return;

		// Is item food?
		Item item = itemStack.getItem();
		if (!(item instanceof ItemFood))
			return;

		// If config allows, mark food as edible
		if (Config.allowOverEating)
			((ItemFood) item).setAlwaysEdible();
	}

	// Calculate nutrition after finishing eating
	@SubscribeEvent
	public void finishUsingItem(LivingEntityUseItemEvent.Finish event) {
		checkFoodEaten(event);
	}

	// Checks which food has been eaten, and updates nutrition
	private void checkFoodEaten(LivingEntityUseItemEvent.Finish event) {
		// Only run on server
		EntityPlayer player = (EntityPlayer) event.getEntity();
		if (player.getEntityWorld().isRemote)
			return;

		// Get out if not food item
		ItemStack itemStack = event.getItem();
		if (!(itemStack.getItem() instanceof ItemFood))
			return;

		// Calculate nutrition
		List<Nutrient> foundNutrients = NutrientUtils.getFoodNutrients(itemStack); // Nutrient list for that food
		float nutritionValue = NutrientUtils.calculateNutrition(itemStack, foundNutrients); // Nutrition value for that food

		// Add to each nutrient
		for (Nutrient nutrient : foundNutrients) {
			player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).add(nutrient, nutritionValue);
		}
	}
}
