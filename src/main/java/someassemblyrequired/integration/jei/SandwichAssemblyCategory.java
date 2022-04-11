package someassemblyrequired.integration.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.item.sandwich.SandwichItem;
import someassemblyrequired.common.util.Util;

import java.util.Collections;
import java.util.List;

public class SandwichAssemblyCategory implements IRecipeCategory<SandwichAssemblyCategory.Recipe> {

    public static final ResourceLocation ID = Util.id("sandwich_assembly");
    private final Component title = Util.translate("jei.sandwich_assembly");
    private final IDrawable background;
    private final IDrawable icon;
    private final ItemStack breadSlice;
    private final ItemStack sandwich;

    public SandwichAssemblyCategory(IGuiHelper helper) {
        background = helper.createDrawable(Util.id("textures/jei/sandwich_assembly.png"), 0, 0, 96, 64);
        breadSlice = new ItemStack(ModItems.BREAD_SLICE.get());
        sandwich = SandwichItem.makeSandwich(
                vectorwing.farmersdelight.common.registry.ModItems.BEEF_PATTY.get(),
                someassemblyrequired.common.init.ModItems.TOMATO_SLICES.get(),
                vectorwing.farmersdelight.common.registry.ModItems.FRIED_EGG.get(),
                vectorwing.farmersdelight.common.registry.ModItems.CABBAGE_LEAF.get()
        );
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM, sandwich);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends Recipe> getRecipeClass() {
        return Recipe.class;
    }

    @Override
    public Component getTitle() {
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
    public void setIngredients(Recipe decompositionRecipe, IIngredients ingredients) {
        ingredients.setInputIngredients(ImmutableList.of(Ingredient.of(breadSlice)));
        ingredients.setOutput(VanillaTypes.ITEM, sandwich);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, Recipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(0, true, 7, 4);
        stacks.set(0, breadSlice);
        stacks.init(1, true, 7, 42);
        stacks.set(1, breadSlice);
        stacks.init(2, false, 71, 23);
        stacks.set(2, sandwich);
    }

    @Override
    public List<Component> getTooltipStrings(Recipe recipe, double mouseX, double mouseY) {
        int x = 8;
        int y = 24;
        if (mouseX >= x && mouseY >= y && mouseX < x + 16 && mouseY < y + 16) {
            return ImmutableList.of(Util.translate("jei.sandwich_assembly.ingredients"));
        }
        return Collections.emptyList();
    }

    public static class Recipe {

    }
}
