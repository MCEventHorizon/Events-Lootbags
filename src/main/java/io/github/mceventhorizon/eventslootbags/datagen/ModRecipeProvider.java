package io.github.mceventhorizon.eventslootbags.datagen;

import io.github.mceventhorizon.eventslootbags.init.BlockInit;
import io.github.mceventhorizon.eventslootbags.init.ItemInit;
import io.github.mceventhorizon.eventslootbags.util.ModTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

  public ModRecipeProvider(PackOutput pOutput) {
    super(pOutput);
  }

  @Override
  protected void buildRecipes(RecipeOutput pRecipeOutput) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.BAG_OPENER.get())
        .pattern("IPI")
        .pattern("PBP")
        .pattern("IPI")
        .define('I', Items.IRON_INGOT)
        .define('P', Items.OAK_PLANKS)
        .define('B', ModTags.Items.ITEM_LOOTBAG)
        .unlockedBy(getHasName(ItemInit.COMMON_LOOTBAG.get()), has(ModTags.Items.ITEM_LOOTBAG))
        .save(pRecipeOutput);
  }
}
