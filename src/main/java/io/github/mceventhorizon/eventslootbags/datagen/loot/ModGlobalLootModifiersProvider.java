package io.github.mceventhorizon.eventslootbags.datagen.loot;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import io.github.mceventhorizon.eventslootbags.init.ItemInit;
import io.github.mceventhorizon.eventslootbags.loot.AddBagsToMobDropsModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;


public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {

    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, EventsLootbags.MODID);
    }

    @Override
    protected void start() {
        add("bag_from_mobs", new AddBagsToMobDropsModifier(new LootItemCondition[]{
                LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS).build()
        }, ItemInit.COMMON_LOOTBAG.get()));
    }
}
