package io.github.mceventhorizon.eventslootbags.datagen;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import io.github.mceventhorizon.eventslootbags.init.BlockInit;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModBlockTagProvider extends BlockTagsProvider {

  public ModBlockTagProvider(PackOutput output,
      CompletableFuture<Provider> lookupProvider,
      @Nullable ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, EventsLootbags.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(Provider pProvider) {
    this.tag(BlockTags.NEEDS_STONE_TOOL).add(BlockInit.BAG_OPENER.get());
    this.tag(BlockTags.MINEABLE_WITH_AXE).add(BlockInit.BAG_OPENER.get());
  }
}
