package ca.wescook.nutrition.events;

import ca.wescook.nutrition.capabilities.INutrientManager;
import ca.wescook.nutrition.effects.EffectsManager;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientUtils;
import ca.wescook.nutrition.utility.ClientData;
import ca.wescook.nutrition.utility.Config;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class EventEatFood {
	@CapabilityInject(INutrientManager.class)
	private static final Capability<INutrientManager> NUTRITION_CAPABILITY = null;

	// Detect eating cake
	@SubscribeEvent
	public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		PlayerEntity player = (PlayerEntity) event.getEntity();

		// Get info
		World world = event.getWorld();
		BlockState blockState = world.getBlockState(event.getPos());

		// Get out if not cake
		if (!(blockState.getBlock() instanceof CakeBlock)) {
			return;
		}

		// Should we let them eat cake?
		if (player.canEat(false) || Config.allowOverEating) {
			// Calculate nutrition
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(blockState.getBlock().getRegistryName().toString())); // Get cake Item from registry name
			ItemStack itemStack = new ItemStack(item);
			List<Nutrient> foundNutrients = NutrientUtils.getFoodNutrients(itemStack);
			float nutritionValue = NutrientUtils.calculateNutrition(itemStack, foundNutrients);

			// Add to each nutrient
			if (!player.getEntityWorld().isRemote) // Server
				player.getCapability(NUTRITION_CAPABILITY).ifPresent(cap -> cap.add(foundNutrients, nutritionValue));
			else // Client
				ClientData.localNutrition.add(foundNutrients, nutritionValue);

			// If full but over-eating, simulate cake eating
			if (!player.getEntityWorld().isRemote && !player.canEat(false) && Config.allowOverEating) {
				int cakeBites = blockState.get(CakeBlock.BITES);
				if (cakeBites < 6)
					world.setBlockState(event.getPos(), blockState.with(CakeBlock.BITES, cakeBites + 1), 3);
				else
					world.destroyBlock(event.getPos(), false);
			}
		}
	}

	// Allow food to be consumed regardless of hunger level
	@SubscribeEvent
	public void startUsingItem(PlayerInteractEvent.RightClickItem event) {
		// Only run on server
		PlayerEntity player = (PlayerEntity) event.getEntity();
		if (player.getEntityWorld().isRemote)
			return;

		// Interacting with item?
		ItemStack itemStack = event.getItemStack();
		if (itemStack == null)
			return;

		// Is item food?
		Item item = itemStack.getItem();
		if (!item.isFood())
			return;

		// If config allows, mark food as edible
		// TODO: Requires fixing
		//if (Config.allowOverEating)
			//item.getFood().setAlwaysEdible();
	}

	// Calculate nutrition after finishing eating and reapply effects if appropriate
	@SubscribeEvent
	public void finishUsingItem(LivingEntityUseItemEvent.Finish event) {
		// Only check against players
		if (!(event.getEntity() instanceof PlayerEntity))
			return;

		// Get ItemStack of eaten food
		ItemStack itemStack = event.getItem();
		int stackSize = itemStack.getCount();
		itemStack.setCount(1); // Temporarily setting stack size to 1 so .copy works for stack sizes of 0
		ItemStack dummyStack = itemStack.copy(); // Create dummy copy to not affect original item
		itemStack.setCount(stackSize); // Restore original stack size

		// Apply actions to item
		PlayerEntity player = (PlayerEntity) event.getEntity();
		applyNutrition(player, dummyStack);
		reapplyEffectsFromMilk(player, dummyStack);
	}

	// Add found nutrients to player
	private void applyNutrition(PlayerEntity player, ItemStack itemStack) {
		// Get out if not food item
		if (!(itemStack.getItem().isFood() || itemStack.getItem() instanceof MilkBucketItem))
			return;

		// Calculate nutrition
		List<Nutrient> foundNutrients = NutrientUtils.getFoodNutrients(itemStack); // Nutrient list for that food
		float nutritionValue = NutrientUtils.calculateNutrition(itemStack, foundNutrients); // Nutrition value for that food

		// Add to each nutrient
		if (!player.getEntityWorld().isRemote) // Server
			player.getCapability(NUTRITION_CAPABILITY).ifPresent(cap -> cap.add(foundNutrients, nutritionValue));
		else // Client
			ClientData.localNutrition.add(foundNutrients, nutritionValue);
	}

	// If milk clears effects, reapply immediately
	private void reapplyEffectsFromMilk(PlayerEntity player, ItemStack itemStack) {
		// Server only
		if (player.getEntityWorld().isRemote)
			return;

		// Only continue if milk bucket (curative item)
		if (!(itemStack.getItem() instanceof MilkBucketItem))
			return;

		// Reapply effects
		EffectsManager.reapplyEffects(player);
	}
}
