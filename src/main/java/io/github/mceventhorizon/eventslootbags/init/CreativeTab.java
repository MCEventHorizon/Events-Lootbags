package io.github.mceventhorizon.eventslootbags.init;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import java.util.Locale;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EventsLootbags.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeTab {

  @SubscribeEvent
  public static void registerCreativeTab(CreativeModeTabEvent.Register event) {
    event.registerCreativeModeTab(
        new ResourceLocation(EventsLootbags.MODID, "items".toLowerCase(Locale.ROOT)),
        builder -> builder.title(Component.translatable("itemGroup.eventslootbags"))
            .icon(() -> new ItemStack(ItemInit.LEGENDARY_LOOTBAG.get()))
            .displayItems((parameters, output) -> {
              output.accept(ItemInit.COMMON_LOOTBAG.get());
              output.accept(ItemInit.UNCOMMON_LOOTBAG.get());
              output.accept(ItemInit.RARE_LOOTBAG.get());
              output.accept(ItemInit.EPIC_LOOTBAG.get());
              output.accept(ItemInit.LEGENDARY_LOOTBAG.get());
              output.accept(BlockInit.BAG_OPENER.get());
            }));
  }
}
