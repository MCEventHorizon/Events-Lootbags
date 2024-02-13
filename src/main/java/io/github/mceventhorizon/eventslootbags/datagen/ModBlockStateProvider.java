package io.github.mceventhorizon.eventslootbags.datagen;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import io.github.mceventhorizon.eventslootbags.init.BlockInit;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {

  public ModBlockStateProvider(PackOutput output,
      ExistingFileHelper exFileHelper) {
    super(output, EventsLootbags.MODID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    blockWithItem(BlockInit.BAG_OPENER);
  }

  private <T extends Block> void blockWithItem (RegistryObject<T> blockRegistryObject) {
    simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
  }
}
