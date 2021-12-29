package someassemblyrequired.client.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModRecipeTypes;
import someassemblyrequired.common.util.Util;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin {

    private static final ResourceLocation ID = Util.prefix("main");

    private static List<Recipe<?>> findRecipes(RecipeType<?> recipeType) {
        // noinspection ConstantConditions
        return Minecraft.getInstance().level.getRecipeManager().getRecipes().stream().filter(r -> r.getType() == recipeType).collect(Collectors.toList());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CuttingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ToastingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (Block cuttingBoard : ModBlocks.getCuttingBoards()) {
            registration.addRecipeCatalyst(new ItemStack(cuttingBoard), CuttingRecipeCategory.ID);
        }
        for (Block toaster : ModBlocks.getToasters()) {
            registration.addRecipeCatalyst(new ItemStack(toaster), ToastingRecipeCategory.ID);
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(findRecipes(ModRecipeTypes.CUTTING), CuttingRecipeCategory.ID);
        registration.addRecipes(findRecipes(ModRecipeTypes.TOASTING), ToastingRecipeCategory.ID);
        // recipes with more than one possible result have an empty output, ignore these (for JEI only)
        registration.addRecipes(findRecipes(RecipeType.SMOKING).stream().filter((recipe) -> !recipe.getResultItem().isEmpty()).collect(Collectors.toList()), ToastingRecipeCategory.ID);

        for (Block cuttingBoard : ModBlocks.getCuttingBoards()) {
            registration.addIngredientInfo(new ItemStack(cuttingBoard), VanillaTypes.ITEM, "description.someassemblyrequired.cutting_board");
        }
        for (Block assemblyTable : ModBlocks.getSandwichAssemblyTables()) {
            registration.addIngredientInfo(new ItemStack(assemblyTable), VanillaTypes.ITEM, "description.someassemblyrequired.sandwich_assembly_table");
        }
        for (ItemLike item : new ItemLike[]{
                ModBlocks.REDSTONE_TOASTER.get(),
                ModBlocks.STICKY_REDSTONE_TOASTER.get(),
                ModItems.KITCHEN_KNIFE.get(),
                ModItems.BREAD_SLICE.get(),
                ModItems.TOASTED_BREAD_SLICE.get(),
                ModItems.CHARRED_BREAD_SLICE.get(),
                ModItems.CHARRED_FOOD.get(),
                ModItems.APPLE_SLICES.get(),
                ModItems.GOLDEN_APPLE_SLICES.get(),
                ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(),
                ModItems.CHOPPED_CARROT.get(),
                ModItems.CHOPPED_GOLDEN_CARROT.get(),
                ModItems.CHOPPED_BEETROOT.get(),
                ModItems.PORK_CUTS.get(),
                ModItems.BACON_STRIPS.get(),
                ModItems.TOASTED_CRIMSON_FUNGUS.get(),
                ModItems.SLICED_TOASTED_CRIMSON_FUNGUS.get(),
                ModItems.TOASTED_WARPED_FUNGUS.get(),
                ModItems.SLICED_TOASTED_WARPED_FUNGUS.get(),
                ModItems.TOMATO.get(),
                ModItems.TOMATO_SLICES.get(),
                ModItems.LETTUCE_HEAD.get(),
                ModItems.LETTUCE_LEAF.get(),
                ModItems.MAYONNAISE_BOTTLE.get(),
                ModItems.KETCHUP_BOTTLE.get(),
                ModItems.SWEET_BERRY_JAM_BOTTLE.get()
        }) {
            // noinspection ConstantConditions
            registration.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM, String.format("description.someassemblyrequired.%s", item.asItem().getRegistryName().getPath()));
        }
    }
}
