package io.github.mceventhorizon.eventslootbags;

import io.github.mceventhorizon.eventslootbags.init.BlockInit;
import io.github.mceventhorizon.eventslootbags.init.ItemInit;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EventsLootbags.MODID)
public class EventsLootbags {
	
	public static final String MODID = "eventslootbags";
	
	public EventsLootbags() {
		IEventBus bus  = FMLJavaModLoadingContext.get().getModEventBus();
		
		BlockInit.BLOCKS.register(bus);
		ItemInit.ITEMS.register(bus);
	}
	
	public static final CreativeModeTab TAB = new CreativeModeTab(MODID) {
		@Override
		public ItemStack makeIcon() {
			return ItemInit.LEGENDARY_LOOTBAG.get().getDefaultInstance();
		}
	};
}
