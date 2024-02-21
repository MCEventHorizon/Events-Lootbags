package io.github.mceventhorizon.eventslootbags.init;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import io.github.mceventhorizon.eventslootbags.menu.BagOpenerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuInit {
  public static final DeferredRegister<MenuType<?>> MENU_TYPES =
      DeferredRegister.create(ForgeRegistries.MENU_TYPES, EventsLootbags.MODID);

  public static final RegistryObject<MenuType<BagOpenerMenu>> BAG_OPENER_MENU =
      MENU_TYPES.register("bag_opener_menu", () -> IForgeMenuType.create(BagOpenerMenu::new));
}
