package io.github.mceventhorizon.eventslootbags.init;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EventsLootbags.MODID);
	
	public static final RegistryObject<Item> COMMON_LOOTBAG = ITEMS.register("common_lootbag", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	
	public static final RegistryObject<Item> UNCOMMON_LOOTBAG = ITEMS.register("uncommon_lootbag", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	
}
