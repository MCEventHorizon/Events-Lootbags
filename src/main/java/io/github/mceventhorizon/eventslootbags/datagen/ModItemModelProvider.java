package io.github.mceventhorizon.eventslootbags.datagen;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import io.github.mceventhorizon.eventslootbags.init.ItemInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {

  public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, EventsLootbags.MODID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    simpleItem(ItemInit.COMMON_LOOTBAG);
    simpleItem(ItemInit.UNCOMMON_LOOTBAG);
    simpleItem(ItemInit.RARE_LOOTBAG);
    simpleItem(ItemInit.EPIC_LOOTBAG);
    simpleItem(ItemInit.LEGENDARY_LOOTBAG);

  }

  private <T extends Item> ItemModelBuilder simpleItem(RegistryObject<T> item) {
    return withExistingParent(item.getId().getPath(),
        new ResourceLocation("item/generated")).texture("layer0",
        new ResourceLocation(EventsLootbags.MODID, "item/" + item.getId().getPath())
    );
  }
}
