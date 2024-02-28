package io.github.mceventhorizon.eventslootbags.blockentities;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import io.github.mceventhorizon.eventslootbags.init.BlockEntityInit;
import io.github.mceventhorizon.eventslootbags.menu.BagOpenerMenu;
import io.github.mceventhorizon.eventslootbags.util.LootBagLootGenerator;
import io.github.mceventhorizon.eventslootbags.util.ModTags.Items;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class BagOpenerBlockEntity extends BlockEntity implements MenuProvider {

  public static final String INPUT_TAG = "InputInventory";
  public static final String OUTPUT_TAG = "OutputInventory";
  public static final String PROGRESS_TAG = "Progress";
  public static final String BUFFER_TAG = "Buffer";

  private final ItemStackHandler inventoryInput = createItemHandler(1);
  private final ItemStackHandler inventoryOutput = createItemHandler(15);

  private final LazyOptional<ItemStackHandler> inputOptional = LazyOptional.of(() -> inventoryInput);
  private final LazyOptional<ItemStackHandler> outputOptional = LazyOptional.of(() -> inventoryOutput);

  protected final ContainerData data;
  private int progress = 0;
  private int maxProgress = 200;

  private final List<ItemStack> itemBuffer = new ArrayList<>();

  public BagOpenerBlockEntity(BlockPos pos, BlockState state) {
    super(BlockEntityInit.BAG_OPENER_BLOCK_ENTITY.get(), pos, state);
    this.data = new ContainerData() {
      @Override
      public int get(int pIndex) {
        return switch (pIndex) {
          case 0 -> BagOpenerBlockEntity.this.progress;
          case 1 -> BagOpenerBlockEntity.this.maxProgress;
          default -> 0;
        };
      }

      @Override
      public void set(int pIndex, int pValue) {
        switch (pIndex) {
          case 0 -> BagOpenerBlockEntity.this.progress = pValue;
          case 1 -> BagOpenerBlockEntity.this.maxProgress = pValue;
        }
      }

      @Override
      public int getCount() {
        return 2;
      }
    };
  }

  @Override
  public void load(@NotNull CompoundTag nbt) {
    super.load(nbt);
    CompoundTag lootbagModData = nbt.getCompound(EventsLootbags.MODID);
    this.inventoryInput.deserializeNBT(lootbagModData.getCompound(INPUT_TAG));
    this.inventoryOutput.deserializeNBT(lootbagModData.getCompound(OUTPUT_TAG));
    deserializeBufferNBT(lootbagModData.getCompound(BUFFER_TAG));
    progress = nbt.getInt(PROGRESS_TAG);
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag nbt) {
    super.saveAdditional(nbt);
    var lootbagModData = new CompoundTag();
    lootbagModData.put(INPUT_TAG, this.inventoryInput.serializeNBT());
    lootbagModData.put(OUTPUT_TAG, this.inventoryOutput.serializeNBT());
    lootbagModData.put(BUFFER_TAG, serializeBufferNBT());

    nbt.putInt(PROGRESS_TAG, progress);
    nbt.put(EventsLootbags.MODID, lootbagModData);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    inputOptional.invalidate();
    outputOptional.invalidate();
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(
      @NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      if (side == Direction.UP) {
        return this.inputOptional.cast();
      }
      if (side == Direction.DOWN) {
        return this.outputOptional.cast();
      }
    }

    return super.getCapability(cap, side);
  }

  @Override
  public @NotNull Component getDisplayName() {
    return Component.translatable("block.eventslootbags.bag_opener");
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory,
      @NotNull Player pPlayer) {
    return new BagOpenerMenu(pContainerId, pPlayerInventory, this, this.data);
  }

  private ItemStackHandler createItemHandler(int slots) {
    return new ItemStackHandler(slots) {
      @Override
      protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        setChanged();
      }
    };
  }

  public void tickServer(Level level) {
    if(canCraft()) {
      if(hasProgressFinished()) {
        if(bufferIsEmpty()) {
          craftItem(
                  level,
                  Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(inventoryInput.getStackInSlot(0).getItem())).toString()
          );
          setChanged();
        }
      } else {
        increaseCraftingProgress();
      }
      setChanged();
    } else {
      resetProgress();
    }
    tryToEmptyBuffer();
  }

  private boolean bufferIsEmpty() {
    return itemBuffer.isEmpty();
  }

  private void tryToEmptyBuffer() {
    for (int i = itemBuffer.size() - 1; i >= 0; i--) {
      ItemStack insertedItem = ItemHandlerHelper.insertItem(inventoryOutput, itemBuffer.get(i), false);
      if(insertedItem.isEmpty()) {
        itemBuffer.remove(i);
      }
    }
  }

  private boolean canCraft () {
    return this.inventoryInput.getStackInSlot(0).is(Items.ITEM_LOOTBAG);
  }

  private void increaseCraftingProgress() {
    progress++;
  }

  private boolean hasProgressFinished() {
    return progress >= maxProgress;
  }

  private void craftItem(Level level, String location) {
    List<ItemStack> lootItems = LootBagLootGenerator.generateLootItems(
        level, LootBagLootGenerator.getResourceMap().get(location)
    );

    lootItems.forEach(itemStack -> {
      if (!ItemHandlerHelper.insertItem(inventoryOutput, itemStack, false).isEmpty()) {
        itemBuffer.add(itemStack);
      }
    });
    inventoryInput.extractItem(0, 1, false);
    resetProgress();
  }

  private void resetProgress() {
    progress = 0;
  }

  private CompoundTag serializeBufferNBT()
  {
    ListTag nbtTagList = new ListTag();
    for (int i = 0; i < itemBuffer.size(); i++)
    {
      if (!itemBuffer.get(i).isEmpty())
      {
        CompoundTag itemTag = new CompoundTag();
        itemTag.putInt("Slot", i);
        itemBuffer.get(i).save(itemTag);
        nbtTagList.add(itemTag);
      }
    }
    CompoundTag nbt = new CompoundTag();
    nbt.put("Items", nbtTagList);
    nbt.putInt("Size", itemBuffer.size());
    return nbt;
  }

  private void deserializeBufferNBT(CompoundTag nbt)
  {
    NonNullList.withSize(nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : itemBuffer.size(), ItemStack.EMPTY);
    ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
    for (int i = 0; i < tagList.size(); i++)
    {
      CompoundTag itemTags = tagList.getCompound(i);
        itemBuffer.add(ItemStack.of(itemTags));
    }
  }

  public LazyOptional<ItemStackHandler> getInputOptional () {
    return inputOptional;
  }

  public LazyOptional<ItemStackHandler> getOutputOptional () {
    return outputOptional;
  }

  public List<ItemStack> getItemBuffer() {
    return itemBuffer;
  }

}
