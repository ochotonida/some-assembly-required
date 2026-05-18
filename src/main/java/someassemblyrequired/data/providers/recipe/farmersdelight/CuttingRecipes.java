package someassemblyrequired.data.providers.recipe.farmersdelight;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class CuttingRecipes {

    private static final Set<CuttingBoardRecipeBuilder> RECIPES = new HashSet<>();

    public static void addCuttingRecipes(RecipeOutput output) {
        RECIPES.clear();

        cut(Items.APPLE, ModItems.APPLE_SLICES.get(), 2);
        cut(Items.BREAD, ModItems.BREAD_SLICE.get(), 4);
        cut(Items.BEETROOT, ModItems.CHOPPED_BEETROOT.get(), 2);
        cut(Items.CARROT, ModItems.CHOPPED_CARROT.get(), 2);
        cut(Items.GOLDEN_CARROT, ModItems.CHOPPED_GOLDEN_CARROT.get(), 2);
        cut(Items.ENCHANTED_GOLDEN_APPLE, ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(), 2);
        cut(Items.GOLDEN_APPLE, ModItems.GOLDEN_APPLE_SLICES.get(), 2);

        cut(ModItems.BURGER_BUN.get(), ModItems.BURGER_BUN_BOTTOM.get(), 1).addResult(ModItems.BURGER_BUN_TOP.get());

        for (CuttingBoardRecipeBuilder recipe : new HashSet<>(RECIPES)) {
            save(output, recipe);
        }

        cut(CommonTags.Items.CROPS_TOMATO, ModItems.TOMATO_SLICES.get(), 2)
                .save(output, SomeAssemblyRequired.id("%s/%s/tomato".formatted("cutting", ModCompat.FARMERSDELIGHT)));
        cut(CommonTags.Items.CROPS_ONION, ModItems.SLICED_ONION.get(), 2)
                .save(output, SomeAssemblyRequired.id("%s/%s/onion".formatted("cutting", ModCompat.FARMERSDELIGHT)));
    }

    public static CuttingBoardRecipeBuilder cut(ItemLike ingredient, ItemLike mainResult, int count) {
        return cut(Ingredient.of(ingredient), mainResult, count);
    }

    public static CuttingBoardRecipeBuilder cut(TagKey<Item> ingredient, ItemLike mainResult, int count) {
        return cut(Ingredient.of(ingredient), mainResult, count);
    }

    public static CuttingBoardRecipeBuilder cut(Ingredient ingredient, ItemLike mainResult, int count) {
        CuttingBoardRecipeBuilder builder = CuttingBoardRecipeBuilder.cuttingRecipe(ingredient, Ingredient.of(CommonTags.Items.TOOLS_KNIFE), mainResult, count);
        RECIPES.add(builder);
        return builder;
    }

    public static void save(RecipeOutput recipeOutput, CuttingBoardRecipeBuilder builder) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(builder.getResult());
        builder.build(recipeOutput, SomeAssemblyRequired.id("%s/%s/%s".formatted("cutting", ModCompat.FARMERSDELIGHT, id.getPath())));
    }
}
