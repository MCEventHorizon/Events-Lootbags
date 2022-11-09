package io.github.mceventhorizon.eventslootbags.init;

import com.google.common.base.Supplier;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EventsLootbags.MODID);
	
	public static final RegistryObject<Block> BAG_OPENER = register("bag_opener",
			() -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0f).requiresCorrectToolForDrops()),
					new Item.Properties().tab(CreativeModeTab.TAB_MISC));
	
	private static <T extends Block> RegistryObject<T> register (String name, Supplier<T> supplier, Item.Properties properties) {
		RegistryObject<T> block = BLOCKS.register(name, supplier);
		ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
		return block;
	}
}
