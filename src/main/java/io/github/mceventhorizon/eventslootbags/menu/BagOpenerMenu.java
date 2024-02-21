package io.github.mceventhorizon.eventslootbags.menu;

import io.github.mceventhorizon.eventslootbags.blockentities.BagOpenerBlockEntity;
import io.github.mceventhorizon.eventslootbags.init.BlockInit;
import io.github.mceventhorizon.eventslootbags.init.MenuInit;
import io.github.mceventhorizon.eventslootbags.util.ModTags.Items;
import java.util.Objects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class BagOpenerMenu extends AbstractContainerMenu {

  private final BagOpenerBlockEntity bagOpenerBlockEntity;
  private final ContainerLevelAccess levelAccess;
  private final ContainerData data;

  public BagOpenerMenu (int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
    this(containerId, playerInventory, playerInventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
  }

  public BagOpenerMenu (int containerId, Inventory playerInventory, BlockEntity blockEntity, ContainerData data) {
    super(MenuInit.BAG_OPENER_MENU.get(), containerId);
    this.data = data;
    if (blockEntity instanceof BagOpenerBlockEntity be) {
      this.bagOpenerBlockEntity = be;
    } else {
      throw new IllegalStateException(
          "Incorrect block entity class (%s) passed into BagOpenerMenu"
              .formatted(blockEntity.getClass().getCanonicalName())
      );
    }
    this.levelAccess = ContainerLevelAccess.create(Objects.requireNonNull(blockEntity.getLevel()),
        blockEntity.getBlockPos());
    createPlayerHotbar(playerInventory);
    createPlayerInventory(playerInventory);
    createBlockEntityInventory(be);
    addDataSlots(data);
  }

  private void createBlockEntityInventory(BagOpenerBlockEntity be) {
    be.getInputOptional().ifPresent(inventory -> {
      addSlot(new SlotItemHandler(inventory, 0, 26, 34){
        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
          return stack.getTags().anyMatch(itemTagKey -> itemTagKey == Items.ITEM_LOOTBAG);
        }
      });
    });
    be.getOutputOptional().ifPresent(inventory -> {
      for (int row = 0; row < 3; row++) {
        for (int column = 0; column < 5; column++) {
          addSlot(new SlotItemHandler(inventory, column + (row * 5), 80 + (column * 18), 17 + (row * 18)) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
              return false;
            }
          });
        }
      }
    });
  }

  private void createPlayerInventory(Inventory playerInventory) {
    for (int row = 0; row < 3; row++) {
      for (int column = 0; column < 9; column++) {
        addSlot(new Slot(playerInventory, 9 + column + (row * 9), 8 + (column * 18), 84 + (row * 18)));
      }
    }
  }

  private void createPlayerHotbar(Inventory playerInventory) {
    for (int column = 0; column < 9; column++) {
      addSlot(new Slot(playerInventory, column, 8 + (column * 18), 142));
    }
  }

  public boolean isCrafting() {
    return data.get(0) > 0;
  }

  public int getScaledProgress() {
    int progress = this.data.get(0);
    int maxProgress = this.data.get(1);
    int progressArrowSize = 26;
    return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
  }

  @Override
  public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
    Slot fromSlot = getSlot(pIndex);
    ItemStack fromStack = fromSlot.getItem();

    if (fromStack.getCount() <= 0) {
      fromSlot.set(ItemStack.EMPTY);
    }

    if (!fromSlot.hasItem()) {
      return ItemStack.EMPTY;
    }

    ItemStack copyFromStack = fromStack.copy();

    if (pIndex < 36) {
      if (!moveItemStackTo(fromStack, 36, 52, false)) return ItemStack.EMPTY;
    } else if (pIndex < 52) {
      if (!moveItemStackTo(fromStack, 0, 36, false)) return ItemStack.EMPTY;
    } else {
        System.err.println("Invalid slot index: " + pIndex);
        return ItemStack.EMPTY;
    }

    fromSlot.setChanged();
    fromSlot.onTake(pPlayer, fromStack);

    return copyFromStack;
  }

  @Override
  public boolean stillValid(@NotNull Player pPlayer) {
    return stillValid(this.levelAccess, pPlayer, BlockInit.BAG_OPENER.get());
  }
}
