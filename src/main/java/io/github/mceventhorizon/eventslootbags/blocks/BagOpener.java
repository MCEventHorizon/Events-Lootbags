package io.github.mceventhorizon.eventslootbags.blocks;

import io.github.mceventhorizon.eventslootbags.blockenitities.BagOpenerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BagOpener extends Block implements EntityBlock {

  public BagOpener() {
    super(BlockBehaviour.Properties.of()
        .strength(2.0f)
        .sound(SoundType.WOOD));
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new BagOpenerBlockEntity(pos, state);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState,
      BlockEntityType<T> pBlockEntityType) {

    if (pLevel.isClientSide) {
      return null;
    } else {
      return (lvl, pos, st, blockEntity) -> {
        if (blockEntity instanceof BagOpenerBlockEntity be) {
          be.tickServer();
        }
      };
    }

  }
}
