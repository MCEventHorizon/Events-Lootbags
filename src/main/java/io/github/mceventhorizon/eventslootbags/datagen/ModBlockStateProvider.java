package io.github.mceventhorizon.eventslootbags.datagen;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import io.github.mceventhorizon.eventslootbags.init.BlockInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

  public ModBlockStateProvider(PackOutput output,
      ExistingFileHelper exFileHelper) {
    super(output, EventsLootbags.MODID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    simpleBlockWithItem(BlockInit.BAG_OPENER.get(), models().cubeBottomTop(
        "bag_opener",
        new ResourceLocation(EventsLootbags.MODID, "block/" + BlockInit.BAG_OPENER.getId().getPath() + "_side"),
        new ResourceLocation(EventsLootbags.MODID, "block/" + BlockInit.BAG_OPENER.getId().getPath() + "_bottom"),
        new ResourceLocation(EventsLootbags.MODID, "block/" + BlockInit.BAG_OPENER.getId().getPath() + "_top")
    ));
  }
}
