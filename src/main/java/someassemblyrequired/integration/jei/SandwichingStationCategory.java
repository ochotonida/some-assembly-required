package someassemblyrequired.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.init.ModBlocks;
import someassemblyrequired.init.ModItems;
import someassemblyrequired.init.ModTags;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.item.sandwich.SandwichItemHandler;

import java.util.List;
import java.util.Optional;

public class SandwichingStationCategory implements IRecipeCategory<SandwichingStationCategory.Recipe> {

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slot;
    private final IDrawable arrow;

    public SandwichingStationCategory(IGuiHelper helper) {
        ResourceLocation texture = SomeAssemblyRequired.id("textures/jei/sandwiching_station.png");
        background = helper.createBlankDrawable(96, 120);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SANDWICHING_STATION.get()));
        slot = helper.createDrawable(texture, 0, 0, 18, 18);
        arrow = helper.createDrawable(texture, 18, 0, 24, 17);
    }

    @Override
    public RecipeType<Recipe> getRecipeType() {
        return JEICompat.SANDWICHING_STATION;
    }

    @Override
    public Component getTitle() {
        return ModBlocks.SANDWICHING_STATION.get().getName();
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
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses) {
        Optional<SandwichItemHandler> sandwich = focuses.getItemStackFocuses(RecipeIngredientRole.OUTPUT)
                .findFirst()
                .map(IFocus::getTypedValue)
                .flatMap(ITypedIngredient::getItemStack)
                .filter(item -> item.getTag() == null || !item.getTag().getBoolean("IsJEIExample"))
                .flatMap(SandwichItemHandler::get);

        if (sandwich.isEmpty()) {
            Optional<ItemStack> input = focuses.getItemStackFocuses(RecipeIngredientRole.INPUT)
                    .map(IFocus::getTypedValue)
                    .map(ITypedIngredient::getItemStack)
                    .flatMap(Optional::stream)
                    .filter(Ingredients::canAddToSandwich)
                    .filter(stack -> !stack.is(ModTags.SANDWICH_BREAD))
                    .findFirst();

            if (input.isPresent()) {
                ItemStack ingredient = input.get().copy();
                ingredient.setCount(1);
                sandwich = SandwichItemHandler.get(SandwichItem.makeSandwich(ingredient));
            }
        }

        IRecipeSlotBuilder output = builder.addSlot(RecipeIngredientRole.OUTPUT, 72, 52).setBackground(slot, -1, -1);

        if (sandwich.isPresent() && sandwich.get().getItemCount() <= 6) {

            List<ItemStack> ingredients = sandwich.get().getItems();

            for (int i = 0; i < ingredients.size(); i++) {
                IRecipeSlotBuilder slotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, 8, 62 - ingredients.size() * 10 + i * 20).setBackground(slot, -1, -1);
                slotBuilder.addItemStack(ingredients.get(ingredients.size() - i - 1));
            }
            output.addItemStack(sandwich.get().getAsItem());
        } else {
            ItemStack bread = focuses.getItemStackFocuses(RecipeIngredientRole.INPUT)
                    .map(IFocus::getTypedValue)
                    .map(ITypedIngredient::getItemStack)
                    .flatMap(Optional::stream)
                    .filter(item -> item.is(ModTags.SANDWICH_BREAD))
                    .findFirst().orElse(JEIUtil.BREAD_SLICE).copy();
            bread.setCount(1);

            IRecipeSlotBuilder bottomBread = builder.addSlot(RecipeIngredientRole.INPUT, 8, 72).setBackground(slot, -1, -1);
            IRecipeSlotBuilder ingredientInput = builder.addSlot(RecipeIngredientRole.INPUT, 8, 52).setBackground(slot, -1, -1);
            IRecipeSlotBuilder topBread = builder.addSlot(RecipeIngredientRole.INPUT, 8, 32).setBackground(slot, -1, -1);

            if (bread.is(ModItems.BURGER_BUN_BOTTOM.get()) || bread.is(ModItems.BURGER_BUN_TOP.get())) {
                bottomBread.addItemStack(JEIUtil.BURGER_BUN_BOTTOM);
                topBread.addItemStack(JEIUtil.BURGER_BUN_TOP);
            } else {
                bottomBread.addItemStack(bread);
                topBread.addItemStack(bread);
            }

            ingredientInput.addItemStacks(JEIUtil.INGREDIENTS);
            output.addItemStacks(JEIUtil.getSandwichesForBread(bread));

            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
                    .addIngredients(Ingredient.of(ModTags.SANDWICH_BREAD))
                    .addItemStacks(JEIUtil.POTIONS);
        }
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 36, 51);
    }

    public static class Recipe {

    }
}
