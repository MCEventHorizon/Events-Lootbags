package io.github.mceventhorizon.eventslootbags.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mceventhorizon.eventslootbags.config.EventsLootbagsConfig;
import io.github.mceventhorizon.eventslootbags.init.ItemInit;
import io.github.mceventhorizon.eventslootbags.items.ItemLootbag;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class AddBagsToMobDropsModifier extends LootModifier {

    private static final Map<List<String>, ItemLootbag> mobDropsConfig = new HashMap<>();

    public static final Supplier<Codec<AddBagsToMobDropsModifier>> CODEC = Suppliers.memoize(() ->
        RecordCodecBuilder.create(inst -> codecStart(inst).and(ForgeRegistries.ITEMS.getCodec()
                .fieldOf("item").forGetter(m -> m.item)).apply(inst, AddBagsToMobDropsModifier::new)));
    private final Item item;

    public AddBagsToMobDropsModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (LootItemCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return generatedLoot;
            }
        }
        if (EventsLootbagsConfig.PLAYER_KILL_BAG_DROP.get()) {
            try {
                Entity entity = context.getParam(LootContextParams.KILLER_ENTITY);
                if (entity instanceof Player) {
                    return getItemStacks(generatedLoot, context, EventsLootbagsConfig.AFFECTED_BY_LOOTING.get());
                }
            } catch (NoSuchElementException ignored) {
                return generatedLoot;
            }
        }
        return getItemStacks(generatedLoot, context, EventsLootbagsConfig.AFFECTED_BY_LOOTING.get());
    }

    @NotNull
    private ObjectArrayList<ItemStack> getItemStacks(
            ObjectArrayList<ItemStack> generatedLoot, LootContext context, Boolean useLooting)
    {
        String tableResource = context.getQueriedLootTableId().toString();
        for (Map.Entry<List<String>, ItemLootbag> entry : mobDropsConfig.entrySet()) {
            Random random = new Random();
            if(useLooting) {
                if (entry.getKey().contains(tableResource) && random.nextFloat() < getLootingModifier(context)) {
                    generatedLoot.add(new ItemStack(entry.getValue()));
                }
            } else {
                if (entry.getKey().contains(tableResource) &&
                        random.nextFloat() < EventsLootbagsConfig.BASE_DROP_RATE.get()) {
                    generatedLoot.add(new ItemStack(entry.getValue()));
                }
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

    private double getLootingModifier(LootContext context) {
        double baseDropRate = EventsLootbagsConfig.BASE_DROP_RATE.get();
        List<Double> lootingDropRates = (List<Double>) EventsLootbagsConfig.LOOTING_CHANCES.get();
        return switch (context.getLootingModifier()) {
            case 1 -> lootingDropRates.get(0);
            case 2 -> lootingDropRates.get(1);
            case 3 -> lootingDropRates.get(2);
            default -> baseDropRate;
        };
    }

    public static void addListsFromConfig() {
        mobDropsConfig.clear();
        mobDropsConfig.put((List<String>) EventsLootbagsConfig.COMMON_BAG_MOB_LIST.get(), ItemInit.COMMON_LOOTBAG.get());
        mobDropsConfig.put((List<String>) EventsLootbagsConfig.UNCOMMON_BAG_MOB_LIST.get(), ItemInit.UNCOMMON_LOOTBAG.get());
        mobDropsConfig.put((List<String>) EventsLootbagsConfig.RARE_BAG_MOB_LIST.get(), ItemInit.RARE_LOOTBAG.get());
        mobDropsConfig.put((List<String>) EventsLootbagsConfig.EPIC_BAG_MOB_LIST.get(), ItemInit.EPIC_LOOTBAG.get());
        mobDropsConfig.put((List<String>) EventsLootbagsConfig.LEGENDARY_BAG_MOB_LIST.get(), ItemInit.LEGENDARY_LOOTBAG.get());
    }

    public static void validateLootingRatesListValid() {
        if (EventsLootbagsConfig.LOOTING_CHANCES.get().size() != 3) {
            throw new RuntimeException("Looting chances list must contain 3 elements");
        }
    }
}
