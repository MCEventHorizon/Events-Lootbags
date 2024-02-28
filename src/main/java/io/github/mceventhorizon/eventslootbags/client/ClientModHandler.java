package io.github.mceventhorizon.eventslootbags.client;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import io.github.mceventhorizon.eventslootbags.init.MenuInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = EventsLootbags.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientModHandler {
  @SubscribeEvent
  public static void clientSetup(FMLClientSetupEvent event) {
    event.enqueueWork(() -> MenuScreens.register(MenuInit.BAG_OPENER_MENU.get(), BagOpenerMenuScreen::new));
  }
}
