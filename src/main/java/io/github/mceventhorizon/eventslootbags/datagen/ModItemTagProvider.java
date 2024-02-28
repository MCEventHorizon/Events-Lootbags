package io.github.mceventhorizon.eventslootbags.datagen;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import io.github.mceventhorizon.eventslootbags.init.ItemInit;
import io.github.mceventhorizon.eventslootbags.util.ModTags.Items;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModItemTagProvider extends ItemTagsProvider {


  public ModItemTagProvider(PackOutput pOutput, CompletableFuture<Provider> pLookupProvider,
      CompletableFuture<TagLookup<Block>> pBlockTags,
      @Nullable ExistingFileHelper existingFileHelper) {
    super(pOutput, pLookupProvider, pBlockTags, EventsLootbags.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(Provider pProvider) {
    this.tag(Items.ITEM_LOOTBAG).add(
        ItemInit.COMMON_LOOTBAG.get(),
        ItemInit.UNCOMMON_LOOTBAG.get(),
        ItemInit.RARE_LOOTBAG.get(),
        ItemInit.EPIC_LOOTBAG.get(),
        ItemInit.LEGENDARY_LOOTBAG.get()
    );
  }
}
