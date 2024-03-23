package io.github.mceventhorizon.eventslootbags.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class EventsLootbagsConfig {

    private static final List<String> EMPTY_LIST = new ArrayList<>();

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue PLAYER_KILL_BAG_DROP;
    public static final ForgeConfigSpec.DoubleValue BASE_DROP_RATE;
    public static final ForgeConfigSpec.BooleanValue AFFECTED_BY_LOOTING;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Double>> LOOTING_CHANCES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> COMMON_BAG_MOB_LIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> UNCOMMON_BAG_MOB_LIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> RARE_BAG_MOB_LIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EPIC_BAG_MOB_LIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> LEGENDARY_BAG_MOB_LIST;


    static {
        BUILDER.push("Config for Event's Lootbags");
        PLAYER_KILL_BAG_DROP = BUILDER
                .comment("Set to true if mobs only drop bags when killed by players")
                .define("Mobs drop lootbag when killed by player", true);
        BASE_DROP_RATE = BUILDER
                .comment("Set the base drop rate for bags. Range 0 - 1, example: 0.25 is 25%")
                .comment("Default: 25%")
                .defineInRange("Base drop rate:", 0.25d, 0d, 1d);
        AFFECTED_BY_LOOTING = BUILDER
                .comment("Set to true if chance for a bag being dropped by mobs is affected by looting")
                .define("Looting affects bag drops:", true);
        LOOTING_CHANCES = BUILDER
                .comment("Set the drop rate based on looting levels")
                .comment("Doesn't work if affected by looting is disabled")
                .comment("Example: [0.5, 0.75, 1] means 50% for Looting I, 75% for Looting II and 100% for Looting III")
                .comment("Default: 50%, 75% and 100%")
                .defineList("Looting chances", List.of(0.5d, 0.75d, 1d), num -> (Double)num >= 0 && (Double)num <= 1);
        COMMON_BAG_MOB_LIST = BUILDER.comment("Add mobs to the list that will have a lootbag added to their loot drops")
                .comment("Format: \"modname:entities/mob_name\"")
                .comment("Example: [\"minecraft:entities/creeper\",\"minecraft:entities/skeleton\"]")
                .comment("Default: []")
                .comment("Common lootbag mob drops:")
                .defineListAllowEmpty("Mobs that have Common Lootbag added to their loot drops", EMPTY_LIST,
                        obj -> obj instanceof String);
        UNCOMMON_BAG_MOB_LIST = BUILDER
                .comment("Uncommon lootbag mob drops:")
                .defineListAllowEmpty("Mobs that have Uncommon Lootbag added to their loot drops", EMPTY_LIST,
                        obj -> obj instanceof String);
        RARE_BAG_MOB_LIST = BUILDER
                .comment("Rare lootbag mob drops:")
                .defineListAllowEmpty("Mobs that have Rare Lootbag added to their loot drops", EMPTY_LIST,
                        obj -> obj instanceof String);
        EPIC_BAG_MOB_LIST = BUILDER
                .comment("Epic lootbag mob drops:")
                .defineListAllowEmpty("Mobs that have Epic Lootbag added to their loot drops", EMPTY_LIST,
                        obj -> obj instanceof String);
        LEGENDARY_BAG_MOB_LIST = BUILDER
                .comment("Legendary lootbag mob drops:")
                .defineListAllowEmpty("Mobs that have Legendary Lootbag added to their loot drops", EMPTY_LIST,
                        obj -> obj instanceof String);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
