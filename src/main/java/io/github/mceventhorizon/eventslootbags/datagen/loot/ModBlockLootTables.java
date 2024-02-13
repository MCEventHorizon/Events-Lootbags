package io.github.mceventhorizon.eventslootbags.datagen.loot;

import io.github.mceventhorizon.eventslootbags.init.BlockInit;
import java.util.Set;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockLootTables extends BlockLootSubProvider {

  public ModBlockLootTables() {
    super(Set.of(), FeatureFlags.REGISTRY.allFlags());
  }

  @Override
  protected void generate() {
    this.dropSelf(BlockInit.BAG_OPENER.get());
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    return BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
  }
}
