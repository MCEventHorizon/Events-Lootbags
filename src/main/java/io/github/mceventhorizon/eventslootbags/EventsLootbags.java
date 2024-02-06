package io.github.mceventhorizon.eventslootbags;

import io.github.mceventhorizon.eventslootbags.init.BlockEntityInit;
import io.github.mceventhorizon.eventslootbags.init.BlockInit;
import io.github.mceventhorizon.eventslootbags.init.CreativeTab;
import io.github.mceventhorizon.eventslootbags.init.ItemInit;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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
	}


}
