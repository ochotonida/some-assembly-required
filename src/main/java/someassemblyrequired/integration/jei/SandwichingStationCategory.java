package someassemblyrequired.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.common.ingredient.Ingredients;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;
import someassemblyrequired.common.item.sandwich.SandwichItem;
import someassemblyrequired.common.item.sandwich.SandwichItemHandler;
import someassemblyrequired.common.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SandwichingStationCategory implements IRecipeCategory<SandwichingStationCategory.Recipe> {

    private final IDrawable background;
    private final IDrawable icon;

    private static final ItemStack BREAD_SLICE = new ItemStack(ModItems.BREAD_SLICE.get());
    private static final List<ItemStack> SANDWICHES = new ArrayList<>();
    private static final List<ItemStack> INGREDIENTS = new ArrayList<>();
    private static final List<ItemStack> POTIONS = new ArrayList<>();

    public static void refreshSandwiches() {
        INGREDIENTS.clear();
        POTIONS.clear();
        SANDWICHES.clear();

        POTIONS.addAll(ForgeRegistries.POTIONS.getValues()
                .stream()
                .map(potion -> PotionUtils.setPotion(new ItemStack(Items.POTION), potion))
                .toList());

        INGREDIENTS.addAll(ForgeRegistries.ITEMS.getValues()
                .stream()
                .filter(Ingredients::hasCustomIngredientProperties)
                .map(ItemStack::new)
                .toList());

        INGREDIENTS.stream().map(SandwichItem::makeSandwich).forEach(SANDWICHES::add);
    }

    public SandwichingStationCategory(IGuiHelper helper) {
        background = helper.createDrawable(Util.id("textures/jei/sandwich_assembly.png"), 0, 0, 96, 64);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SANDWICHING_STATION.get()));
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
        IRecipeSlotBuilder breadInput1 = builder.addSlot(RecipeIngredientRole.INPUT, 8, 43);
        IRecipeSlotBuilder breadInput2 = builder.addSlot(RecipeIngredientRole.INPUT, 8, 5);
        IRecipeSlotBuilder ingredientInput = builder.addSlot(RecipeIngredientRole.INPUT, 8, 24);
        IRecipeSlotBuilder output = builder.addSlot(RecipeIngredientRole.OUTPUT, 72, 24);

        Optional<SandwichItemHandler> sandwich = focuses.getItemStackFocuses(RecipeIngredientRole.OUTPUT)
                .findFirst()
                .map(IFocus::getTypedValue)
                .flatMap(ITypedIngredient::getItemStack)
                .flatMap(SandwichItemHandler::get);

        if (sandwich.isEmpty()) {
            Optional<ItemStack> input = focuses.getItemStackFocuses(RecipeIngredientRole.INPUT)
                    .map(IFocus::getTypedValue)
                    .map(ITypedIngredient::getItemStack)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(Ingredients::canAddToSandwich)
                    .filter(stack -> !stack.is(ModTags.SANDWICH_BREAD) || Ingredients.hasCustomIngredientProperties(stack.getItem()))
                    .findFirst();

            if (input.isPresent()) {
                ItemStack ingredient = input.get().copy();
                ingredient.setCount(1);
                sandwich = SandwichItemHandler.get(SandwichItem.makeSandwich(ingredient));
            }
        }

        if (sandwich.isPresent() && sandwich.get().size() == 3 && sandwich.get().hasTopAndBottomBread()) {
            breadInput1.addItemStack(sandwich.get().bottom());
            breadInput2.addItemStack(sandwich.get().top());
            ingredientInput.addItemStack(sandwich.get().getItems().get(1));
            output.addItemStack(sandwich.get().getAsItem());
        } else {
            breadInput1.addItemStack(BREAD_SLICE);
            breadInput2.addItemStack(BREAD_SLICE);
            ingredientInput.addItemStacks(INGREDIENTS);
            output.addItemStacks(SANDWICHES);

            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
                    .addIngredients(Ingredient.of(ModTags.SANDWICH_BREAD))
                    .addItemStacks(POTIONS);
        }
    }

    public static class Recipe {

    }
}
