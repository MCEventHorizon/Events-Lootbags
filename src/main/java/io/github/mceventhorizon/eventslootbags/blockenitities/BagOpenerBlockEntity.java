package io.github.mceventhorizon.eventslootbags.blockenitities;

import io.github.mceventhorizon.eventslootbags.init.BlockEntityInit;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class BagOpenerBlockEntity extends BlockEntity {

  public static final String ITEMS_TAG = "Inventory";

  public static int SLOT_COUNT = 1;
  public static int SLOT = 0;

  private final ItemStackHandler items = createItemHandler();

  private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> items);

  public BagOpenerBlockEntity(BlockPos pos, BlockState state) {
    super(BlockEntityInit.BAG_OPENER_BLOCK_ENTITY.get(), pos, state);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    itemHandler.invalidate();
  }

  private ItemStackHandler createItemHandler() {
    return new ItemStackHandler(SLOT_COUNT) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
      }
    };
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(
      @NotNull Capability<T> cap,@Nullable Direction side) {

    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return itemHandler.cast();
    } else {
      return super.getCapability(cap, side);
    }
  }

  public void tickServer() {
  }
}
