package io.github.mceventhorizon.eventslootbags.items;

import java.util.List;

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
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class ItemLootbag extends Item{

	private final ResourceLocation COMMON_LOOTBAG_LOCATION = new ResourceLocation("eventslootbags:eventslootbags/common_lootbag");
	private final ResourceLocation UNCOMMON_LOOTBAG_LOCATION = new ResourceLocation("eventslootbags:eventslootbags/uncommon_lootbag");
	private final ResourceLocation RARE_LOOTBAG_LOCATION = new ResourceLocation("eventslootbags:eventslootbags/rare_lootbag");
	private final ResourceLocation EPIC_LOOTBAG_LOCATION = new ResourceLocation("eventslootbags:eventslootbags/epic_lootbag");
	private final ResourceLocation LEGENDARY_LOOTBAG_LOCATION = new ResourceLocation("eventslootbags:eventslootbags/legendary_lootbag");
	
	public ItemLootbag(Properties properties) {
		super(properties);
	}
	
	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
		
		if(!level.isClientSide) {

			switch (Objects.requireNonNull(
					ForgeRegistries.ITEMS.getKey(player.getItemInHand(hand).getItem())).toString()) {
				case "eventslootbags:common_lootbag" ->
						createLootTable(level, player, hand, COMMON_LOOTBAG_LOCATION);
				case "eventslootbags:uncommon_lootbag" ->
						createLootTable(level, player, hand, UNCOMMON_LOOTBAG_LOCATION);
				case "eventslootbags:rare_lootbag" ->
						createLootTable(level, player, hand, RARE_LOOTBAG_LOCATION);
				case "eventslootbags:epic_lootbag" ->
						createLootTable(level, player, hand, EPIC_LOOTBAG_LOCATION);
				case "eventslootbags:legendary_lootbag" ->
						createLootTable(level, player, hand, LEGENDARY_LOOTBAG_LOCATION);
			}
			
		}
		return InteractionResultHolder.fail(player.getItemInHand(hand));
				
	}
	
	private void createLootTable (Level level, Player player, InteractionHand hand, ResourceLocation location) {
		
		LootTable table = Objects.requireNonNull(level.getServer()).getLootTables().get(location);
		
		LootContext context = new LootContext.Builder((ServerLevel) level)
				.withLuck(player.getLuck())
				.withRandom(level.getRandom())
				.create(LootContextParamSets.EMPTY);
		
		List<ItemStack> stacks = table.getRandomItems(context);
		
		for (ItemStack item : stacks) {
			ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), item);
			itemEntity.setNoPickUpDelay();
			level.addFreshEntity(itemEntity);
		}
		
		if(!player.isCreative()) {
			player.getItemInHand(hand).shrink(1);
		}
		player.getItemInHand(hand);

	}

	


}
