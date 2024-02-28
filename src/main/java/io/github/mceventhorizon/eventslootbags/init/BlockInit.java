package io.github.mceventhorizon.eventslootbags.init;

import com.google.common.base.Supplier;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import io.github.mceventhorizon.eventslootbags.blocks.BagOpener;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EventsLootbags.MODID);
	
	public static final RegistryObject<BagOpener> BAG_OPENER = register("bag_opener", BagOpener::new,
					new Item.Properties());
	
	private static <T extends Block> RegistryObject<T> register (String name, Supplier<T> supplier, Item.Properties properties) {
		RegistryObject<T> block = BLOCKS.register(name, supplier);
		ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
		return block;
	}
}
