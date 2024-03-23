package io.github.mceventhorizon.eventslootbags;

import io.github.mceventhorizon.eventslootbags.config.EventsLootbagsConfig;
import io.github.mceventhorizon.eventslootbags.events.ModEventHandlers;
import io.github.mceventhorizon.eventslootbags.init.BlockEntityInit;
import io.github.mceventhorizon.eventslootbags.init.BlockInit;
import io.github.mceventhorizon.eventslootbags.init.CreativeTab;
import io.github.mceventhorizon.eventslootbags.init.ItemInit;
import io.github.mceventhorizon.eventslootbags.init.MenuInit;
import io.github.mceventhorizon.eventslootbags.loot.LootModifiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EventsLootbags.MODID)
public class EventsLootbags {
	
	public static final String MODID = "eventslootbags";
	
	public EventsLootbags() {
		IEventBus bus  = FMLJavaModLoadingContext.get().getModEventBus();
		BlockInit.BLOCKS.register(bus);
		BlockEntityInit.BLOCK_ENTITIES.register(bus);
		ItemInit.ITEMS.register(bus);
		CreativeTab.CREATIVE_MOD_TABS.register(bus);
		MenuInit.MENU_TYPES.register(bus);
		LootModifiers.LOOT_MODIFIER_SERIALIZER.register(bus);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EventsLootbagsConfig.SPEC, "events_lootbags.toml");
		FMLJavaModLoadingContext.get().getModEventBus().register(new ModEventHandlers());
	}


}
