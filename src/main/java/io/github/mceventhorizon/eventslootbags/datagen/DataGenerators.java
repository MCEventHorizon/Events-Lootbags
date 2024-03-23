package io.github.mceventhorizon.eventslootbags.datagen;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import java.util.concurrent.CompletableFuture;

import io.github.mceventhorizon.eventslootbags.datagen.loot.ModGlobalLootModifiersProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = EventsLootbags.MODID, bus = Bus.MOD)
public class DataGenerators {

  @SubscribeEvent
  public static void gatherDate(GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    PackOutput packOutput = generator.getPackOutput();
    ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
    CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

    generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput));
    generator.addProvider(event.includeServer(), ModLootTableProvider.create(packOutput));
    generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
    generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));

    ModBlockTagProvider tagProvider = generator.addProvider(event.includeServer(),
        new ModBlockTagProvider(packOutput, lookupProvider, existingFileHelper));

    generator.addProvider(event.includeServer(), new ModItemTagProvider(packOutput,
        lookupProvider, tagProvider.contentsGetter(), existingFileHelper));
    generator.addProvider(event.includeServer(), new ModGlobalLootModifiersProvider(packOutput));
  }
}
