package ca.wescook.nutrition.events;

import ca.wescook.nutrition.capabilities.INutrientManager;
import ca.wescook.nutrition.effects.EffectsManager;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientUtils;
import ca.wescook.nutrition.proxy.ClientProxy;
import ca.wescook.nutrition.utility.Config;
import net.minecraft.block.BlockCake;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class EventEatFood {
	@CapabilityInject(INutrientManager.class)
	private static final Capability<INutrientManager> NUTRITION_CAPABILITY = null;

	// Detect eating cake
	@SubscribeEvent
	public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		EntityPlayer player = (EntityPlayer) event.getEntity();

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
			if (!player.getEntityWorld().isRemote) // Server
				player.getCapability(NUTRITION_CAPABILITY, null).add(foundNutrients, nutritionValue);
			else // Client
				ClientProxy.localNutrition.add(foundNutrients, nutritionValue);

			// If full but over-eating, simulate cake eating
			if (!player.getEntityWorld().isRemote && !player.canEat(false) && Config.allowOverEating) {
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

	// Calculate nutrition after finishing eating and reapply effects if appropriate
	@SubscribeEvent
	public void finishUsingItem(LivingEntityUseItemEvent.Finish event) {
		// Only check against players
		if (!(event.getEntity() instanceof EntityPlayer))
			return;

		// Get ItemStack of eaten food
		ItemStack itemStack = event.getItem();
		int stackSize = itemStack.getCount();
		itemStack.setCount(1); // Temporarily setting stack size to 1 so .copy works for stack sizes of 0
		ItemStack dummyStack = itemStack.copy(); // Create dummy copy to not affect original item
		itemStack.setCount(stackSize); // Restore original stack size

		// Apply actions to item
		EntityPlayer player = (EntityPlayer) event.getEntity();
		applyNutrition(player, dummyStack);
		reapplyEffectsFromMilk(player, dummyStack);
	}

	// Add found nutrients to player
	private void applyNutrition(EntityPlayer player, ItemStack itemStack) {
		// Get out if not food item
		if (!(itemStack.getItem() instanceof ItemFood || itemStack.getItem() instanceof ItemBucketMilk))
			return;

		// Calculate nutrition
		List<Nutrient> foundNutrients = NutrientUtils.getFoodNutrients(itemStack); // Nutrient list for that food
		float nutritionValue = NutrientUtils.calculateNutrition(itemStack, foundNutrients); // Nutrition value for that food

		// Add to each nutrient
		if (!player.getEntityWorld().isRemote) // Server
			player.getCapability(NUTRITION_CAPABILITY, null).add(foundNutrients, nutritionValue);
		else // Client
			ClientProxy.localNutrition.add(foundNutrients, nutritionValue);
	}

	// If milk clears effects, reapply immediately
	private void reapplyEffectsFromMilk(EntityPlayer player, ItemStack itemStack) {
		// Server only
		if (player.getEntityWorld().isRemote)
			return;

		// Only continue if milk bucket (curative item)
		if (!(itemStack.getItem() instanceof ItemBucketMilk))
			return;

		// Reapply effects
		EffectsManager.reapplyEffects(player);
	}
}
