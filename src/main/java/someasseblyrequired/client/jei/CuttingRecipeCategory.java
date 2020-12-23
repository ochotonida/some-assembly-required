package someasseblyrequired.client.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import someasseblyrequired.SomeAssemblyRequired;
import someasseblyrequired.common.init.Blocks;
import someasseblyrequired.common.init.Tags;
import someasseblyrequired.common.recipe.CuttingRecipe;

import java.util.Arrays;
import java.util.List;

public class CuttingRecipeCategory implements IRecipeCategory<CuttingRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(SomeAssemblyRequired.MODID, "cutting");

    private static final Ingredient KNIFE = Ingredient.fromTag(Tags.KNIVES);

    private final IDrawable background;
    private final IDrawable icon;
    private final String title;

    public CuttingRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(new ResourceLocation(SomeAssemblyRequired.MODID, "textures/gui/cutting_recipe.png"), 0, 0, 95, 39);
        icon = guiHelper.createDrawableIngredient(new ItemStack(Blocks.OAK_CUTTING_BOARD));
        title = I18n.format("recipecategory." + SomeAssemblyRequired.MODID + ".cutting");
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends CuttingRecipe> getRecipeClass() {
        return CuttingRecipe.class;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(CuttingRecipe recipe, IIngredients ingredients) {
        List<Ingredient> list = recipe.getIngredients();
        list.add(KNIFE);
        ingredients.setInputIngredients(list);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout layout, CuttingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = layout.getItemStacks();

        stacks.init(0, true, 12, 9);
        stacks.set(0, Arrays.asList(recipe.getIngredients().get(0).getMatchingStacks()));

        stacks.init(1, true, 49, 9);
        stacks.set(1, Arrays.asList(KNIFE.getMatchingStacks()));

        stacks.init(2, true, 75, 11);
        stacks.set(2, recipe.getRecipeOutput());
    }
}
