package someassemblyrequired.data.providers;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.data.providers.recipe.SandwichSpoutingRecipeBuilder;
import someassemblyrequired.data.providers.recipe.farmersdelight.CuttingRecipeBuilder;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.registry.ModBlocks;
import someassemblyrequired.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        addCraftingRecipes(consumer);
        addCookingRecipes(consumer);
        if (ModCompat.isFarmersDelightLoaded()) {
            CuttingRecipeBuilder.addCuttingRecipes(consumer);
        }
        if (ModCompat.isCreateLoaded()) {
            SandwichSpoutingRecipeBuilder.addFillingRecipes(consumer);
        }
    }

    private void addCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.SANDWICHING_STATION.get())
                .pattern("AA")
                .pattern("BB")
                .define('A', Items.SMOOTH_STONE)
                .define('B', ItemTags.PLANKS)
                .unlockedBy("has_smooth_stone", createItemCriterion(Items.SMOOTH_STONE))
                .save(consumer, getRecipeLocation(ModBlocks.SANDWICHING_STATION.get(), "crafting_shaped"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.BURGER_BUN.get())
                .requires(Ingredient.of(ForgeTags.GRAIN_WHEAT), 2)
                .requires(Ingredient.of(ForgeTags.SEEDS))
                .unlockedBy("has_wheat", createItemCriterion(Items.WHEAT))
                .save(consumer, getRecipeLocation(ModItems.BURGER_BUN.get(), "crafting_shapeless"));
    }

    private void addCookingRecipes(Consumer<FinishedRecipe> consumer) {
        addBreadCookingRecipe(consumer, 200, RecipeSerializer.SMELTING_RECIPE, "smelting");
        addBreadCookingRecipe(consumer, 100, RecipeSerializer.SMOKING_RECIPE, "smoking");
        addBreadCookingRecipe(consumer, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, "campfire_cooking");
    }

    private void addBreadCookingRecipe(Consumer<FinishedRecipe> consumer, int time, RecipeSerializer<? extends AbstractCookingRecipe> serializer, String type) {
        SimpleCookingRecipeBuilder
                .generic(Ingredient.of(ModItems.BREAD_SLICE.get()), RecipeCategory.FOOD, ModItems.TOASTED_BREAD_SLICE.get(), 0.35F, time, serializer)
                .unlockedBy("has_bread", createItemCriterion(ModItems.BREAD_SLICE.get()))
                .save(consumer, getRecipeLocation(ModItems.TOASTED_BREAD_SLICE.get(), type));
    }

    private ResourceLocation getRecipeLocation(ItemLike result, String location) {
        // noinspection ConstantConditions
        return SomeAssemblyRequired.id(location + "/" + ForgeRegistries.ITEMS.getKey(result.asItem()).getPath());
    }

    private InventoryChangeTrigger.TriggerInstance createItemCriterion(ItemLike itemProvider) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(itemProvider);
    }
}
