package someassemblyrequired.data.providers;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.data.providers.recipe.SandwichSpoutingRecipeBuilder;
import someassemblyrequired.data.providers.recipe.create.CuttingRecipeGenerator;
import someassemblyrequired.data.providers.recipe.create.PressingRecipeGenerator;
import someassemblyrequired.data.providers.recipe.farmersdelight.CuttingRecipes;
import someassemblyrequired.registry.ModBlocks;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModTags;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Recipes extends RecipeProvider {

    // See CreateRecipeProvider
    static final List<ProcessingRecipeGen<?, ?, ?>> GENERATORS = new ArrayList<>();

    public Recipes(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        addCraftingRecipes(output);
        addCookingRecipes(output);
        CuttingRecipes.addCuttingRecipes(output);
        SandwichSpoutingRecipeBuilder.addFillingRecipes(output);
    }

    public static void registerAllProcessing(DataGenerator gen, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        GENERATORS.add(new CuttingRecipeGenerator(output, registries));
        GENERATORS.add(new PressingRecipeGenerator(output, registries));

        gen.addProvider(true, new DataProvider() {

            @Override
            public String getName() {
                return "Processing Recipes";
            }

            @Override
            public CompletableFuture<?> run(CachedOutput dc) {
                return CompletableFuture.allOf(GENERATORS.stream()
                        .map(gen -> gen.run(dc))
                        .toArray(CompletableFuture[]::new));
            }
        });
    }

    private void addCraftingRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.SANDWICHING_STATION.get())
                .pattern("AA")
                .pattern("BB")
                .define('A', Items.SMOOTH_STONE)
                .define('B', ItemTags.PLANKS)
                .unlockedBy("has_smooth_stone", createItemCriterion(Items.SMOOTH_STONE))
                .save(output, getRecipeLocation(ModBlocks.SANDWICHING_STATION.get(), "crafting_shaped"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.RAW_BURGER_BUN.get())
                .requires(Ingredient.fromValues(Stream.of(new Ingredient.TagValue(ModTags.DOUGH), new Ingredient.TagValue(ModTags.DOUGHS))))
                .requires(Ingredient.of(Tags.Items.SEEDS))
                .unlockedBy("has_seeds", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(Tags.Items.SEEDS)
                ))
                .save(output, getRecipeLocation(ModItems.RAW_BURGER_BUN.get(), "crafting_shapeless"));
    }

    private void addCookingRecipes(RecipeOutput output) {
        addCookingRecipes(output, 200, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, "smelting");
        addCookingRecipes(output, 100, RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, "smoking");
        addCookingRecipes(output, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, "campfire_cooking");
    }

    private <T extends AbstractCookingRecipe> void addCookingRecipes(RecipeOutput output, int time, RecipeSerializer<T> serializer, AbstractCookingRecipe.Factory<T> factory, String type) {
        addCookingRecipe(ModItems.BREAD_SLICE, ModItems.TOASTED_BREAD_SLICE, output, time, serializer, factory, type);
        addCookingRecipe(ModItems.RAW_BURGER_BUN, ModItems.BURGER_BUN, output, time, serializer, factory, type);
    }

    private <T extends AbstractCookingRecipe> void addCookingRecipe(Supplier<Item> input, Supplier<Item> result, RecipeOutput output, int time, RecipeSerializer<T> serializer, AbstractCookingRecipe.Factory<T> factory, String type) {
        SimpleCookingRecipeBuilder
                .generic(Ingredient.of(input.get()), RecipeCategory.FOOD, result.get(), 0.35F, time, serializer, factory)
                .unlockedBy("has_ingredient", createItemCriterion(input.get()))
                .save(output, getRecipeLocation(result.get(), type));
    }

    private ResourceLocation getRecipeLocation(ItemLike result, String location) {
        return SomeAssemblyRequired.id(location + "/" + BuiltInRegistries.ITEM.getKey(result.asItem()).getPath());
    }

    private Criterion<InventoryChangeTrigger.TriggerInstance> createItemCriterion(ItemLike itemProvider) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(itemProvider);
    }
}
