package someasseblyrequired.client.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import someasseblyrequired.SomeAssemblyRequired;
import someasseblyrequired.common.init.Blocks;
import someasseblyrequired.common.init.Items;
import someasseblyrequired.common.init.RecipeTypes;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin {

    private final ResourceLocation id = new ResourceLocation(SomeAssemblyRequired.MODID, "main");

    private static List<IRecipe<?>> findRecipes(IRecipeType<?> recipeType) {
        // noinspection ConstantConditions
        return Minecraft.getInstance().world.getRecipeManager().getRecipes().stream().filter(r -> r.getType() == recipeType).collect(Collectors.toList());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return id;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CuttingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ToastingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (Block cuttingBoard : Blocks.getCuttingBoards()) {
            registration.addRecipeCatalyst(new ItemStack(cuttingBoard), CuttingRecipeCategory.ID);
        }
        for (Block toaster : Blocks.getToasters()) {
            registration.addRecipeCatalyst(new ItemStack(toaster), ToastingRecipeCategory.ID);
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(findRecipes(RecipeTypes.CUTTING), CuttingRecipeCategory.ID);
        registration.addRecipes(findRecipes(RecipeTypes.TOASTING), ToastingRecipeCategory.ID);
        // recipes with more than one possible result have an empty output, ignore these (for JEI only)
        registration.addRecipes(findRecipes(IRecipeType.SMOKING).stream().filter((recipe) -> !recipe.getRecipeOutput().isEmpty()).collect(Collectors.toList()), ToastingRecipeCategory.ID);

        for (Block cuttingBoard : Blocks.getCuttingBoards()) {
            registration.addIngredientInfo(new ItemStack(cuttingBoard), VanillaTypes.ITEM, "block.someassemblyrequired.cutting_board.description");
        }
        for (Block assemblyTable : Blocks.getSandwichAssemblyTables()) {
            registration.addIngredientInfo(new ItemStack(assemblyTable), VanillaTypes.ITEM, "block.someassemblyrequired.sandwich_assembly_table.description");
        }
        for (IItemProvider item : new IItemProvider[]{
                Blocks.REDSTONE_TOASTER.get(),
                Blocks.STICKY_REDSTONE_TOASTER.get(),
                Items.KITCHEN_KNIFE.get(),
                Items.BREAD_SLICE.get(),
                Items.TOASTED_BREAD_SLICE.get(),
                Items.CHARRED_BREAD_SLICE.get(),
                Items.CHARRED_FOOD.get(),
                Items.APPLE_SLICES.get(),
                Items.GOLDEN_APPLE_SLICES.get(),
                Items.ENCHANTED_GOLDEN_APPLE_SLICES.get(),
                Items.CHOPPED_CARROT.get(),
                Items.CHOPPED_GOLDEN_CARROT.get(),
                Items.CHOPPED_BEETROOT.get(),
                Items.PORK_CUTS.get(),
                Items.BACON_STRIPS.get(),
                Items.TOASTED_CRIMSON_FUNGUS.get(),
                Items.SLICED_TOASTED_CRIMSON_FUNGUS.get(),
                Items.TOASTED_WARPED_FUNGUS.get(),
                Items.SLICED_TOASTED_WARPED_FUNGUS.get(),
                Items.TOMATO.get(),
                Items.TOMATO_SLICES.get(),
                Items.LETTUCE_HEAD.get(),
                Items.LETTUCE_LEAF.get(),
                Items.MAYONNAISE_BOTTLE.get(),
                Items.KETCHUP_BOTTLE.get(),
                Items.SWEET_BERRY_JAM_BOTTLE.get()
        }) {
            registration.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM, item.asItem().getTranslationKey() + ".description");
        }
    }
}
