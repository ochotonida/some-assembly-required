package someassemblyrequired.data;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.util.Util;
import someassemblyrequired.data.recipe.ToastingRecipeBuilder;
import someassemblyrequired.data.recipe.farmersdelight.CuttingRecipeBuilder;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        addShapedRecipes(consumer);
        addShapelessRecipes(consumer);
        ToastingRecipeBuilder.addToastingRecipes(consumer);
        CuttingRecipeBuilder.addCuttingRecipes(consumer);
    }

    private void addShapedRecipes(Consumer<FinishedRecipe> consumer) {
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
    }

    private void addShapelessRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(ModItems.SWEET_BERRY_JAM_BOTTLE.get())
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.SWEET_BERRIES, 2)
                .requires(Items.SUGAR)
                .unlockedBy("has_sweet_berries", createItemCriterion(Items.SWEET_BERRIES))
                .save(consumer, getRecipeLocation(ModItems.SWEET_BERRY_JAM_BOTTLE.get(), "crafting_shapeless"));

        ShapelessRecipeBuilder.shapeless(Items.CHARCOAL)
                .requires(ModItems.CHARRED_FOOD.get(), 4)
                .unlockedBy("has_charred_food", createItemCriterion(ModItems.CHARRED_FOOD.get()))
                .save(consumer, getRecipeLocation(ModItems.CHARRED_FOOD.get(), "crafting_shapeless"));
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

    private ResourceLocation getRecipeLocation(ItemLike result, String location) {
        // noinspection ConstantConditions
        return Util.id(location + "/" + result.asItem().getRegistryName().getPath());
    }

    private InventoryChangeTrigger.TriggerInstance createItemCriterion(ItemLike itemProvider) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(itemProvider);
    }
}
