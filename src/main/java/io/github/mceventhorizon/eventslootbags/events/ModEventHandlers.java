package io.github.mceventhorizon.eventslootbags.events;

import io.github.mceventhorizon.eventslootbags.loot.AddBagsToMobDropsModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class ModEventHandlers {

    @SubscribeEvent
    public void configReloadingEvent(ModConfigEvent.Reloading event) {
        AddBagsToMobDropsModifier.addListsFromConfig();
        AddBagsToMobDropsModifier.validateLootingRatesListValid();
    }
    @SubscribeEvent
    public void configLoadingEvent(ModConfigEvent.Loading event) {
        AddBagsToMobDropsModifier.addListsFromConfig();
        AddBagsToMobDropsModifier.validateLootingRatesListValid();

    }
}
