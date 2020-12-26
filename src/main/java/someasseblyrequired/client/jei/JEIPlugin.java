package someasseblyrequired.client.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import someasseblyrequired.SomeAssemblyRequired;
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
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(findRecipes(RecipeTypes.CUTTING), CuttingRecipeCategory.ID);
        registration.addRecipes(findRecipes(RecipeTypes.TOASTING), ToastingRecipeCategory.ID);
        registration.addRecipes(findRecipes(IRecipeType.SMOKING).stream().filter((recipe) -> !recipe.getRecipeOutput().isEmpty()).collect(Collectors.toList()), ToastingRecipeCategory.ID);
    }
}
