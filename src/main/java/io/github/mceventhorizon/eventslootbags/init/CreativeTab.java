package io.github.mceventhorizon.eventslootbags.init;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = EventsLootbags.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeTab {

  public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS = DeferredRegister.create(
      Registries.CREATIVE_MODE_TAB, EventsLootbags.MODID);

  public static final RegistryObject<CreativeModeTab> LOOTBAG_TAB =
      CREATIVE_MOD_TABS.register("lootbag_tab", () -> CreativeModeTab.builder()
          .title(Component.translatable("itemGroup.eventslootbags"))
          .icon(() -> ItemInit.LEGENDARY_LOOTBAG.get().getDefaultInstance())
          .displayItems((parameters, output) -> {
        output.accept(ItemInit.COMMON_LOOTBAG.get());
        output.accept(ItemInit.UNCOMMON_LOOTBAG.get());
        output.accept(ItemInit.RARE_LOOTBAG.get());
        output.accept(ItemInit.EPIC_LOOTBAG.get());
        output.accept(ItemInit.LEGENDARY_LOOTBAG.get());
        output.accept(BlockInit.BAG_OPENER.get());
      }).build());
}
