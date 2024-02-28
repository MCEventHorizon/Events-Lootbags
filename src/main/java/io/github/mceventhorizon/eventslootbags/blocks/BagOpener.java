package io.github.mceventhorizon.eventslootbags.blocks;

import io.github.mceventhorizon.eventslootbags.blockentities.BagOpenerBlockEntity;
import io.github.mceventhorizon.eventslootbags.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BagOpener extends Block implements EntityBlock {

  public BagOpener() {
    super(BlockBehaviour.Properties.of()
        .strength(2.0f)
        .mapColor(MapColor.TERRACOTTA_YELLOW)
        .sound(SoundType.WOOD));
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return BlockEntityInit.BAG_OPENER_BLOCK_ENTITY.get().create(pos, state);
  }

  @Override
  public @NotNull InteractionResult use(@NotNull BlockState state, Level level,
      @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand,
      @NotNull BlockHitResult hit) {

    BlockEntity be = level.getBlockEntity(pos);
    if (!(be instanceof BagOpenerBlockEntity bagOpenerBlockEntity)) {
      return InteractionResult.PASS;
    }

    if (level.isClientSide) {
      return InteractionResult.SUCCESS;
    }

    if (player instanceof ServerPlayer sPlayer) {
      sPlayer.openMenu(bagOpenerBlockEntity, pos);
    }

    return InteractionResult.CONSUME;
  }

  @Override
  public void onRemove(@NotNull BlockState state, Level level, @NotNull BlockPos pos,
      @NotNull BlockState newState, boolean movedByPiston) {

    if(!level.isClientSide()) {
      BlockEntity be = level.getBlockEntity(pos);
      if (be instanceof BagOpenerBlockEntity blockEntity) {
        blockEntity.getInputOptional().ifPresent(inventory -> Block.popResource(level, pos, inventory.getStackInSlot(0)));
        blockEntity.getOutputOptional().ifPresent(inventory -> {
          for (int index = 0; index < inventory.getSlots(); index++) {
            Block.popResource(level, pos, inventory.getStackInSlot(index));
          }
        });
        var bufferItems = blockEntity.getItemBuffer();
        bufferItems.forEach(item -> Block.popResource(level, pos, item));
      }
      super.onRemove(state, level, pos, newState, movedByPiston);
    }
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel,
      @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
    return pLevel.isClientSide() ? null : ((lvl, pos, state, be) ->
        ((BagOpenerBlockEntity)be).tickServer(lvl));
  }
}
