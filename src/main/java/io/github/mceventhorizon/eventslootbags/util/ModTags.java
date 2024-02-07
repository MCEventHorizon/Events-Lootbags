package io.github.mceventhorizon.eventslootbags.util;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
  public static class Items {

    public static final TagKey<Item> ITEM_LOOTBAG = tag("item_lootbag");

    private static TagKey<Item> tag(String name) {
      return ItemTags.create(new ResourceLocation(EventsLootbags.MODID, name));
    }

  }
}
