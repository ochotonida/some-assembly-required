package someassemblyrequired.data;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.util.Util;
import someassemblyrequired.data.recipe.SandwichSpoutingRecipeBuilder;
import someassemblyrequired.data.recipe.farmersdelight.CuttingRecipeBuilder;
import someassemblyrequired.integration.ModCompat;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        addShapedRecipes(consumer);
        addCookingRecipes(consumer);
        if (ModCompat.isFarmersDelightLoaded()) {
            CuttingRecipeBuilder.addCuttingRecipes(consumer);
        }
        if (ModCompat.isCreateLoaded()) {
            SandwichSpoutingRecipeBuilder.addFillingRecipes(consumer);
        }
    }

    private void addShapedRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(ModBlocks.SANDWICHING_STATION.get())
                .pattern("AA")
                .pattern("BB")
                .define('A', Items.SMOOTH_STONE)
                .define('B', ItemTags.PLANKS)
                .unlockedBy("has_smooth_stone", createItemCriterion(Items.SMOOTH_STONE))
                .save(consumer, getRecipeLocation(ModBlocks.SANDWICHING_STATION.get(), "crafting_shaped"));
    }

    private void addCookingRecipes(Consumer<FinishedRecipe> consumer) {
        addBreadCookingRecipe(consumer, 200, RecipeSerializer.SMELTING_RECIPE, "smelting");
        addBreadCookingRecipe(consumer, 100, RecipeSerializer.SMOKING_RECIPE, "smoking");
        addBreadCookingRecipe(consumer, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, "campfire_cooking");
    }

    private void addBreadCookingRecipe(Consumer<FinishedRecipe> consumer, int time, SimpleCookingSerializer<?> serializer, String type) {
        SimpleCookingRecipeBuilder
                .cooking(Ingredient.of(ModItems.BREAD_SLICE.get()), ModItems.TOASTED_BREAD_SLICE.get(), 0.35F, time, serializer)
                .unlockedBy("has_bread", createItemCriterion(ModItems.BREAD_SLICE.get()))
                .save(consumer, getRecipeLocation(ModItems.TOASTED_BREAD_SLICE.get(), type));
    }

    private ResourceLocation getRecipeLocation(ItemLike result, String location) {
        // noinspection ConstantConditions
        return Util.id(location + "/" + ForgeRegistries.ITEMS.getKey(result.asItem()).getPath());
    }

    private InventoryChangeTrigger.TriggerInstance createItemCriterion(ItemLike itemProvider) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(itemProvider);
    }
}
