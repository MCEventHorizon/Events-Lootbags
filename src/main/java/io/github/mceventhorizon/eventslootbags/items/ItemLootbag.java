package io.github.mceventhorizon.eventslootbags.items;


import io.github.mceventhorizon.eventslootbags.util.LootBagLootGenerator;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ItemLootbag extends Item{

	public ItemLootbag(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(
			Level level, @NotNull Player player, @NotNull InteractionHand hand
	) {
		if(!level.isClientSide) {
			String itemId = Objects.requireNonNull(
					ForgeRegistries.ITEMS.getKey(player.getItemInHand(hand).getItem())
			).toString();
			LootBagLootGenerator.generateLootItems(
					level, player, hand, LootBagLootGenerator.getResourceMap().get(itemId)
			);
			player.getCooldowns().addCooldown(this, 20);
		}
		return InteractionResultHolder.pass(player.getItemInHand(hand));
	}
}
