package someassemblyrequired.data;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
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
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        addShapedRecipes(consumer);
        addShapelessRecipes(consumer);
        addCookingRecipes(consumer);
        CuttingRecipeBuilder.addCuttingRecipes(consumer);
        ToastingRecipeBuilder.addToastingRecipes(consumer);
    }

    private void addShapedRecipes(Consumer<IFinishedRecipe> consumer) {
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

        ShapedRecipeBuilder.shapedRecipe(ModItems.KITCHEN_KNIFE.get())
                .patternLine(" BA")
                .patternLine(" A ")
                .patternLine("C  ")
                .key('A', Tags.Items.INGOTS_IRON)
                .key('B', Tags.Items.NUGGETS_IRON)
                .key('C', Tags.Items.RODS_WOODEN)
                .addCriterion("has_iron_ingot", createTagCriterion(Tags.Items.INGOTS_IRON))
                .build(consumer, getRecipeLocation(ModItems.KITCHEN_KNIFE.get(), "crafting_shaped"));
    }

    private void addShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.SWEET_BERRY_JAM_BOTTLE.get())
                .addIngredient(Items.GLASS_BOTTLE)
                .addIngredient(Items.SWEET_BERRIES, 2)
                .addIngredient(Items.SUGAR)
                .addCriterion("has_sweet_berries", createItemCriterion(Items.SWEET_BERRIES))
                .build(consumer, getRecipeLocation(ModItems.SWEET_BERRY_JAM_BOTTLE.get(), "crafting_shapeless"));

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.KETCHUP_BOTTLE.get())
                .addIngredient(Items.GLASS_BOTTLE)
                .addIngredient(ModTags.CROPS_TOMATOES)
                .addIngredient(ModTags.CROPS_TOMATOES)
                .addCriterion("has_tomato", createTagCriterion(ModTags.CROPS_TOMATOES))
                .build(consumer, getRecipeLocation(ModItems.KETCHUP_BOTTLE.get(), "crafting_shapeless"));

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.CHARRED_FOOD.get())
                .addIngredient(ModItems.CHARRED_MORSEL.get(), 9)
                .addCriterion("has_charred_morsel", createItemCriterion(ModItems.CHARRED_FOOD.get()))
                .build(consumer, getRecipeLocation(ModItems.CHARRED_MORSEL.get(), "crafting_shapeless"));

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.TOMATO_SEEDS.get(), 2)
                .addIngredient(Ingredient.fromItems(ModItems.TOMATO.get(), ModItems.TOMATO_SLICES.get()))
                .addCriterion("has_tomato", createItemCriterion(ModItems.TOMATO.get()))
                .build(consumer, getRecipeLocation(ModItems.TOMATO_SEEDS.get(), "crafting_shapeless"));
    }

    private void addCookingRecipes(Consumer<IFinishedRecipe> consumer) {
        addFoodCookingRecipes(ModItems.PORK_CUTS.get(), ModItems.BACON_STRIPS.get(), consumer);
    }

    private void addCuttingBoard(Block cuttingBoard, Block strippedLog, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(cuttingBoard)
                .patternLine("AA")
                .key('A', strippedLog)
                .addCriterion("has_stripped_log", createItemCriterion(strippedLog))
                .build(consumer, getRecipeLocation(cuttingBoard, "crafting_shaped/cutting_board"));
    }

    private void addSandwichAssemblyTable(Block sandwichAssemblyTable, Block planks, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(sandwichAssemblyTable)
                .patternLine("AA")
                .patternLine("BB")
                .key('A', Blocks.SMOOTH_STONE)
                .key('B', planks)
                .addCriterion("has_planks", createItemCriterion(planks))
                .build(consumer, getRecipeLocation(sandwichAssemblyTable, "crafting_shaped/sandwich_assembly_table"));
    }

    private void addToaster(Block toaster, Block piston, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(toaster)
                .patternLine(" A ")
                .patternLine("BEB")
                .patternLine("CDC")
                .key('A', Items.LEVER)
                .key('B', Tags.Items.INGOTS_IRON)
                .key('C', ItemTags.PLANKS)
                .key('D', piston)
                .key('E', Items.REDSTONE)
                .addCriterion("has_piston", createItemCriterion(piston))
                .build(consumer, getRecipeLocation(toaster, "crafting_shaped/toaster"));
    }

    private void addFoodCookingRecipes(IItemProvider ingredient, IItemProvider result, Consumer<IFinishedRecipe> consumer) {
        addCookingRecipe(ingredient, result, IRecipeSerializer.SMELTING, "smelting", 200, consumer);
        addCookingRecipe(ingredient, result, IRecipeSerializer.SMOKING, "smoking", 100, consumer);
        addCookingRecipe(ingredient, result, IRecipeSerializer.CAMPFIRE_COOKING, "campfire_cooking", 600, consumer);
    }

    private void addCookingRecipe(IItemProvider ingredient, IItemProvider result, CookingRecipeSerializer<?> serializer, String cookingMethod, int cookingTime, Consumer<IFinishedRecipe> consumer) {
        //noinspection ConstantConditions
        CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(ingredient), result, 0.25F, cookingTime, serializer)
                .addCriterion("has_ingredient", createItemCriterion(ingredient))
                .build(consumer, Util.prefix(cookingMethod).toString() + "/" + ingredient.asItem().getRegistryName().getPath());
    }

    private ResourceLocation getRecipeLocation(IItemProvider result, String location) {
        // noinspection ConstantConditions
        return Util.prefix(location + "/" + result.asItem().getRegistryName().getPath());
    }

    private InventoryChangeTrigger.Instance createItemCriterion(IItemProvider itemProvider) {
        return InventoryChangeTrigger.Instance.forItems(itemProvider);
    }

    private InventoryChangeTrigger.Instance createTagCriterion(ITag<Item> tag) {
        return InventoryChangeTrigger.Instance.forItems(ItemPredicate.Builder.create().tag(tag).build());
    }
}
