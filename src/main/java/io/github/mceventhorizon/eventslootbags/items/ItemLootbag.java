package io.github.mceventhorizon.eventslootbags.items;

import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemLootbag extends Item{

	private static final ResourceLocation COMMON_LOOTBAG_LOCATION = new ResourceLocation("eventslootbags:eventslootbags/common_lootbag");
	private static final ResourceLocation UNCOMMON_LOOTBAG_LOCATION = new ResourceLocation("eventslootbags:eventslootbags/uncommon_lootbag");
	private static final ResourceLocation RARE_LOOTBAG_LOCATION = new ResourceLocation("eventslootbags:eventslootbags/rare_lootbag");
	private static final ResourceLocation EPIC_LOOTBAG_LOCATION = new ResourceLocation("eventslootbags:eventslootbags/epic_lootbag");
	private static final ResourceLocation LEGENDARY_LOOTBAG_LOCATION = new ResourceLocation("eventslootbags:eventslootbags/legendary_lootbag");

	private static final Map<String, ResourceLocation> resourceMap = new HashMap<>();

	static {
		resourceMap.put("eventslootbags:common_lootbag", COMMON_LOOTBAG_LOCATION);
		resourceMap.put("eventslootbags:uncommon_lootbag", UNCOMMON_LOOTBAG_LOCATION);
		resourceMap.put("eventslootbags:rare_lootbag", RARE_LOOTBAG_LOCATION);
		resourceMap.put("eventslootbags:epic_lootbag", EPIC_LOOTBAG_LOCATION);
		resourceMap.put("eventslootbags:legendary_lootbag", LEGENDARY_LOOTBAG_LOCATION);
	}
	public ItemLootbag(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if(!level.isClientSide) {
			String itemId = ForgeRegistries.ITEMS.getKey(player.getItemInHand(hand).getItem()).toString();
			createLootTable(level, player, hand, resourceMap.get(itemId));
			player.getCooldowns().addCooldown(this, 20);
		}
		return InteractionResultHolder.pass(player.getItemInHand(hand));
	}
	
	private void createLootTable (Level level, Player player, InteractionHand hand, ResourceLocation location) {
		
		LootTable table = Objects.requireNonNull(level.getServer()).getLootData().getLootTable(location);

		LootParams.Builder lootParamsBuilder = new LootParams.Builder((ServerLevel) level);

		LootParams lootParams = lootParamsBuilder
				.withLuck(player.getLuck())
				.create(LootContextParamSets.EMPTY);
		
		List<ItemStack> stacks = table.getRandomItems(lootParams);
		
		for (ItemStack item : stacks) {
			ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), item);
			itemEntity.setNoPickUpDelay();
			level.addFreshEntity(itemEntity);
		}
		
		if(!player.isCreative()) {
			player.getItemInHand(hand).shrink(1);
		}
	}
}
