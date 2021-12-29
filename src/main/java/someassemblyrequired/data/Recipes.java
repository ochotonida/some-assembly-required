package someassemblyrequired.data;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;
import someassemblyrequired.common.util.Util;
import someassemblyrequired.data.builder.CuttingRecipeBuilder;
import someassemblyrequired.data.builder.ToastingRecipeBuilder;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<FinishedRecipe> consumer) {
        addShapedRecipes(consumer);
        addShapelessRecipes(consumer);
        addCookingRecipes(consumer);
        CuttingRecipeBuilder.addCuttingRecipes(consumer);
        ToastingRecipeBuilder.addToastingRecipes(consumer);
    }

    private void addShapedRecipes(Consumer<FinishedRecipe> consumer) {
        addCuttingBoard(ModBlocks.OAK_CUTTING_BOARD.get(), Blocks.STRIPPED_OAK_LOG, consumer);
        addCuttingBoard(ModBlocks.SPRUCE_CUTTING_BOARD.get(), Blocks.STRIPPED_SPRUCE_LOG, consumer);
        addCuttingBoard(ModBlocks.BIRCH_CUTTING_BOARD.get(), Blocks.STRIPPED_BIRCH_LOG, consumer);
        addCuttingBoard(ModBlocks.JUNGLE_CUTTING_BOARD.get(), Blocks.STRIPPED_JUNGLE_LOG, consumer);
        addCuttingBoard(ModBlocks.ACACIA_CUTTING_BOARD.get(), Blocks.STRIPPED_ACACIA_LOG, consumer);
        addCuttingBoard(ModBlocks.DARK_OAK_CUTTING_BOARD.get(), Blocks.STRIPPED_DARK_OAK_LOG, consumer);
        addCuttingBoard(ModBlocks.CRIMSON_CUTTING_BOARD.get(), Blocks.STRIPPED_CRIMSON_STEM, consumer);
        addCuttingBoard(ModBlocks.WARPED_CUTTING_BOARD.get(), Blocks.STRIPPED_WARPED_STEM, consumer);

        addSandwichAssemblyTable(ModBlocks.OAK_SANDWICH_ASSEMBLY_TABLE.get(), Blocks.OAK_PLANKS, consumer);
        addSandwichAssemblyTable(ModBlocks.SPRUCE_SANDWICH_ASSEMBLY_TABLE.get(), Blocks.SPRUCE_PLANKS, consumer);
        addSandwichAssemblyTable(ModBlocks.BIRCH_SANDWICH_ASSEMBLY_TABLE.get(), Blocks.BIRCH_PLANKS, consumer);
        addSandwichAssemblyTable(ModBlocks.JUNGLE_SANDWICH_ASSEMBLY_TABLE.get(), Blocks.JUNGLE_PLANKS, consumer);
        addSandwichAssemblyTable(ModBlocks.ACACIA_SANDWICH_ASSEMBLY_TABLE.get(), Blocks.ACACIA_PLANKS, consumer);
        addSandwichAssemblyTable(ModBlocks.DARK_OAK_SANDWICH_ASSEMBLY_TABLE.get(), Blocks.DARK_OAK_PLANKS, consumer);
        addSandwichAssemblyTable(ModBlocks.CRIMSON_SANDWICH_ASSEMBLY_TABLE.get(), Blocks.CRIMSON_PLANKS, consumer);
        addSandwichAssemblyTable(ModBlocks.WARPED_SANDWICH_ASSEMBLY_TABLE.get(), Blocks.WARPED_PLANKS, consumer);

        addToaster(ModBlocks.REDSTONE_TOASTER.get(), Blocks.PISTON, consumer);
        addToaster(ModBlocks.STICKY_REDSTONE_TOASTER.get(), Blocks.STICKY_PISTON, consumer);

        ShapedRecipeBuilder.shaped(ModItems.KITCHEN_KNIFE.get())
                .pattern(" BA")
                .pattern(" A ")
                .pattern("C  ")
                .define('A', Tags.Items.INGOTS_IRON)
                .define('B', Tags.Items.NUGGETS_IRON)
                .define('C', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_iron_ingot", createTagCriterion(Tags.Items.INGOTS_IRON))
                .save(consumer, getRecipeLocation(ModItems.KITCHEN_KNIFE.get(), "crafting_shaped"));
    }

    private void addShapelessRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(ModItems.SWEET_BERRY_JAM_BOTTLE.get())
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.SWEET_BERRIES, 2)
                .requires(Items.SUGAR)
                .unlockedBy("has_sweet_berries", createItemCriterion(Items.SWEET_BERRIES))
                .save(consumer, getRecipeLocation(ModItems.SWEET_BERRY_JAM_BOTTLE.get(), "crafting_shapeless"));

        ShapelessRecipeBuilder.shapeless(ModItems.KETCHUP_BOTTLE.get())
                .requires(Items.GLASS_BOTTLE)
                .requires(ModTags.CROPS_TOMATOES)
                .requires(ModTags.CROPS_TOMATOES)
                .unlockedBy("has_tomato", createTagCriterion(ModTags.CROPS_TOMATOES))
                .save(consumer, getRecipeLocation(ModItems.KETCHUP_BOTTLE.get(), "crafting_shapeless"));

        ShapelessRecipeBuilder.shapeless(ModItems.CHARRED_FOOD.get())
                .requires(ModItems.CHARRED_MORSEL.get(), 9)
                .unlockedBy("has_charred_morsel", createItemCriterion(ModItems.CHARRED_FOOD.get()))
                .save(consumer, getRecipeLocation(ModItems.CHARRED_MORSEL.get(), "crafting_shapeless"));

        ShapelessRecipeBuilder.shapeless(ModItems.TOMATO_SEEDS.get(), 2)
                .requires(Ingredient.of(ModItems.TOMATO.get(), ModItems.TOMATO_SLICES.get()))
                .unlockedBy("has_tomato", createItemCriterion(ModItems.TOMATO.get()))
                .save(consumer, getRecipeLocation(ModItems.TOMATO_SEEDS.get(), "crafting_shapeless"));
    }

    private void addCookingRecipes(Consumer<FinishedRecipe> consumer) {
        addFoodCookingRecipes(ModItems.PORK_CUTS.get(), ModItems.BACON_STRIPS.get(), consumer);
    }

    private void addCuttingBoard(Block cuttingBoard, Block strippedLog, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(cuttingBoard)
                .pattern("AA")
                .define('A', strippedLog)
                .unlockedBy("has_stripped_log", createItemCriterion(strippedLog))
                .save(consumer, getRecipeLocation(cuttingBoard, "crafting_shaped/cutting_board"));
    }

    private void addSandwichAssemblyTable(Block sandwichAssemblyTable, Block planks, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(sandwichAssemblyTable)
                .pattern("AA")
                .pattern("BB")
                .define('A', Blocks.SMOOTH_STONE)
                .define('B', planks)
                .unlockedBy("has_planks", createItemCriterion(planks))
                .save(consumer, getRecipeLocation(sandwichAssemblyTable, "crafting_shaped/sandwich_assembly_table"));
    }

    private void addToaster(Block toaster, Block piston, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(toaster)
                .pattern(" A ")
                .pattern("BEB")
                .pattern("CDC")
                .define('A', Items.LEVER)
                .define('B', Tags.Items.INGOTS_IRON)
                .define('C', ItemTags.PLANKS)
                .define('D', piston)
                .define('E', Items.REDSTONE)
                .unlockedBy("has_piston", createItemCriterion(piston))
                .save(consumer, getRecipeLocation(toaster, "crafting_shaped/toaster"));
    }

    private void addFoodCookingRecipes(ItemLike ingredient, ItemLike result, Consumer<FinishedRecipe> consumer) {
        addCookingRecipe(ingredient, result, RecipeSerializer.SMELTING_RECIPE, "smelting", 200, consumer);
        addCookingRecipe(ingredient, result, RecipeSerializer.SMOKING_RECIPE, "smoking", 100, consumer);
        addCookingRecipe(ingredient, result, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, "campfire_cooking", 600, consumer);
    }

    private void addCookingRecipe(ItemLike ingredient, ItemLike result, SimpleCookingSerializer<?> serializer, String cookingMethod, int cookingTime, Consumer<FinishedRecipe> consumer) {
        //noinspection ConstantConditions
        SimpleCookingRecipeBuilder.cooking(Ingredient.of(ingredient), result, 0.25F, cookingTime, serializer)
                .unlockedBy("has_ingredient", createItemCriterion(ingredient))
                .save(consumer, Util.prefix(cookingMethod) + "/" + ingredient.asItem().getRegistryName().getPath());
    }

    private ResourceLocation getRecipeLocation(ItemLike result, String location) {
        // noinspection ConstantConditions
        return Util.prefix(location + "/" + result.asItem().getRegistryName().getPath());
    }

    private InventoryChangeTrigger.TriggerInstance createItemCriterion(ItemLike itemProvider) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(itemProvider);
    }

    private InventoryChangeTrigger.TriggerInstance createTagCriterion(Tag<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
    }
}
