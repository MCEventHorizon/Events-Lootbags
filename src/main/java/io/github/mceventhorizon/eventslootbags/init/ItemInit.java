package io.github.mceventhorizon.eventslootbags.init;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import io.github.mceventhorizon.eventslootbags.items.ItemLootbag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EventsLootbags.MODID);
	
	public static final RegistryObject<ItemLootbag> COMMON_LOOTBAG = ITEMS.register("common_lootbag",
			() -> new ItemLootbag(new Properties()));
	
	public static final RegistryObject<ItemLootbag> UNCOMMON_LOOTBAG = ITEMS.register("uncommon_lootbag",
			() -> new ItemLootbag(new Item.Properties()));
	
	public static final RegistryObject<ItemLootbag> RARE_LOOTBAG = ITEMS.register("rare_lootbag",
			() -> new ItemLootbag(new Item.Properties()));
	
	public static final RegistryObject<ItemLootbag> EPIC_LOOTBAG = ITEMS.register("epic_lootbag",
			() -> new ItemLootbag(new Item.Properties()));
	
	public static final RegistryObject<ItemLootbag> LEGENDARY_LOOTBAG = ITEMS.register("legendary_lootbag",
			() -> new ItemLootbag(new Item.Properties()));
	
}
