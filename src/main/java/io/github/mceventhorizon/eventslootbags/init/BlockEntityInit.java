package io.github.mceventhorizon.eventslootbags.init;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import io.github.mceventhorizon.eventslootbags.blockenitities.BagOpenerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {

  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
      ForgeRegistries.BLOCK_ENTITY_TYPES, EventsLootbags.MODID);

  public static final RegistryObject<BlockEntityType<BagOpenerBlockEntity>> BAG_OPENER_BLOCK_ENTITY
      = BLOCK_ENTITIES.register("bag_opener",
      () -> BlockEntityType.Builder.of(BagOpenerBlockEntity::new, BlockInit.BAG_OPENER.get()).build(null));
}
