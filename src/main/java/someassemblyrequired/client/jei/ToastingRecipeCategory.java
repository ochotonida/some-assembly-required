package someassemblyrequired.client.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.util.Util;

import java.util.Arrays;
import java.util.List;

// there's probs a better way to do this...
@SuppressWarnings("rawtypes")
public class ToastingRecipeCategory implements IRecipeCategory<Recipe> {

    public static final ResourceLocation ID = Util.prefix("toasting");

    private final IDrawable background;
    private final IDrawable arrow;
    private final IDrawable icon;
    private final String title;

    public ToastingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation texture = Util.prefix("textures/gui/toasting_recipe.png");
        background = guiHelper.createDrawable(texture, 0, 0, 111, 41);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.REDSTONE_TOASTER.get()));
        arrow = guiHelper.drawableBuilder(texture, 111, 0, 22, 16).buildAnimated(240, IDrawableAnimated.StartDirection.LEFT, false);
        title = I18n.get("recipecategory." + SomeAssemblyRequired.MODID + ".toasting");
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
    public void setIngredients(Recipe recipe, IIngredients ingredients) {
        //noinspection unchecked
        ingredients.setInputIngredients((List<Ingredient>) recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout layout, Recipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = layout.getItemStacks();

        stacks.init(0, true, 11, 7);
        //noinspection unchecked
        stacks.set(0, Arrays.asList(((NonNullList<Ingredient>) recipe.getIngredients()).get(0).getItems()));

        stacks.init(2, true, 79, 7);
        stacks.set(2, recipe.getResultItem());
    }

    @Override
    public void draw(Recipe recipe, PoseStack matrixStack, double mouseX, double mouseY) {
        arrow.draw(matrixStack, 44, 12);

        TranslatableComponent timeString = new TranslatableComponent("gui.jei.category.smelting.time.seconds", 12);
        Font fontRenderer = Minecraft.getInstance().font;
        int stringWidth = fontRenderer.width(timeString);
        fontRenderer.draw(matrixStack, timeString, background.getWidth() - stringWidth - 8, 33, 0xFF808080);
    }
}
