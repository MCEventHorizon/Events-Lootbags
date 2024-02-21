package io.github.mceventhorizon.eventslootbags.blockentities;

import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import io.github.mceventhorizon.eventslootbags.init.BlockEntityInit;
import io.github.mceventhorizon.eventslootbags.menu.BagOpenerMenu;
import io.github.mceventhorizon.eventslootbags.util.ModTags.Items;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class BagOpenerBlockEntity extends BlockEntity implements MenuProvider {

  //TODO make a util class later

  private static final ResourceLocation COMMON_LOOTBAG_LOCATION = new ResourceLocation("eventslootbags:eventslootbags/common_lootbag");
  private static final ResourceLocation UNCOMMON_LOOTBAG_LOCATION = new ResourceLocation("eventslootbags:eventslootbags/uncommon_lootbag");
  private static final ResourceLocation RARE_LOOTBAG_LOCATION = new ResourceLocation("eventslootbags:eventslootbags/rare_lootbag");
  private static final ResourceLocation EPIC_LOOTBAG_LOCATION = new ResourceLocation("eventslootbags:eventslootbags/epic_lootbag");
  private static final ResourceLocation LEGENDARY_LOOTBAG_LOCATION = new ResourceLocation("eventslootbags:eventslootbags/legendary_lootbag");

  private static final Map<String, ResourceLocation> resourceMap = new HashMap<>();

  static {
    resourceMap.put("eventslootbags:common_lootbag", COMMON_LOOTBAG_LOCATION);
    resourceMap.put("eventslootbags:uncommon_lootbag", UNCOMMON_LOOTBAG_LOCATION);
    resourceMap.put("eventslootbags:rare_lootbag", RARE_LOOTBAG_LOCATION);
    resourceMap.put("eventslootbags:epic_lootbag", EPIC_LOOTBAG_LOCATION);
    resourceMap.put("eventslootbags:legendary_lootbag", LEGENDARY_LOOTBAG_LOCATION);
  }

  public static final String INPUT_TAG = "InputInventory";
  public static final String OUTPUT_TAG = "OutputInventory";
  public static final String PROGRESS_TAG = "Progress";

  private final ItemStackHandler inventoryInput = createItemHandler(1);
  private final ItemStackHandler inventoryOutput = createItemHandler(15);

  private final LazyOptional<ItemStackHandler> inputOptional = LazyOptional.of(() -> inventoryInput);
  private final LazyOptional<ItemStackHandler> outputOptional = LazyOptional.of(() -> inventoryOutput);

  protected final ContainerData data;
  private int progress = 0;
  private int maxProgress = 78;

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
    progress = nbt.getInt(PROGRESS_TAG);
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag nbt) {
    super.saveAdditional(nbt);
    var lootbagModData = new CompoundTag();
    lootbagModData.put(INPUT_TAG, this.inventoryInput.serializeNBT());
    lootbagModData.put(OUTPUT_TAG, this.inventoryOutput.serializeNBT());
    lootbagModData.putInt(PROGRESS_TAG, progress);
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

  public void tickServer(Level level, BlockPos pos, BlockState blockState) {
    if(hasLootbagItemInInputSlot()) {
      increaseCraftingProgress();
      setChanged();

      if(hasProgressFinished()) {
        craftItem(level, ForgeRegistries.ITEMS.getKey(inventoryInput.getStackInSlot(0).getItem()).toString());
      }
    } else {
      resetProgress();
      setChanged();
    }
  }

  //add to check if inv full
  private boolean hasLootbagItemInInputSlot() {
    return this.inventoryInput.getStackInSlot(0).is(Items.ITEM_LOOTBAG);
  }

  private void increaseCraftingProgress() {
    progress++;
  }

  private boolean hasProgressFinished() {
    return progress >= maxProgress;
  }

  private void craftItem(Level level, String location) {
    List<ItemStack> lootItems = generateLootItems(level, resourceMap.get(location));
    if (lootItems.size() < getAvailableSlots().size()) {
      ArrayList<Integer> slots = getAvailableSlots();

      for (int i = 0; i < lootItems.size(); i++) {
        inventoryOutput.insertItem(slots.get(i), lootItems.get(i), false);
      }
      inventoryInput.extractItem(0, 1, false);
      resetProgress();
    } else {
      progress = maxProgress;
    }
  }

  private void resetProgress() {
    progress = 0;
  }

  public LazyOptional<ItemStackHandler> getInputOptional () {
    return inputOptional;
  }

  public LazyOptional<ItemStackHandler> getOutputOptional () {
    return outputOptional;
  }

  private List<ItemStack> generateLootItems (Level level, ResourceLocation location) {

    LootTable table = Objects.requireNonNull(level.getServer()).getLootData().getLootTable(location);

    LootParams.Builder lootParamsBuilder = new LootParams.Builder((ServerLevel) level);

    LootParams lootParams = lootParamsBuilder.create(LootContextParamSets.EMPTY);

    return table.getRandomItems(lootParams);
  }

  private ArrayList<Integer> getAvailableSlots () {
    ArrayList<Integer> indexArrayList = new ArrayList<>();
    for (int index = 0; index < inventoryOutput.getSlots(); index++) {
      if (inventoryOutput.getStackInSlot(index).isEmpty()) {
        indexArrayList.add(index);
      }
    }
    return indexArrayList;
  }
}
