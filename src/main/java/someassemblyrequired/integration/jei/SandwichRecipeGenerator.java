package someassemblyrequired.integration.jei;

import com.simibubi.create.foundation.fluid.FluidIngredient;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.advanced.ISimpleRecipeManagerPlugin;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class SandwichRecipeGenerator<RECIPE> implements ISimpleRecipeManagerPlugin<RECIPE> {

    protected abstract int getMaxToppings();

    @Override
    public boolean isHandledInput(ITypedIngredient<?> input) {
        if (input.getType() == VanillaTypes.ITEM_STACK) {
            ItemStack item = input.getItemStack().orElse(ItemStack.EMPTY);
            return item.is(ModTags.SANDWICH_BREAD) || isHandledFilling(item);
        } else if (input.getType() == NeoForgeTypes.FLUID_STACK) {
            return isHandledFilling(input.getCastIngredient(NeoForgeTypes.FLUID_STACK));
        }
        return false;
    }

    protected boolean isHandledFilling(ItemStack filling) {
        return !filling.is(ModItems.SANDWICH)
                && !filling.is(ModTags.SANDWICH_BREAD)
                && getFluidFromFilling(filling).isEmpty()
                && Ingredients.canAddToSandwich(filling);
    }

    protected boolean isHandledFilling(FluidStack fluid) {
        return getFillingFromFluid(fluid).isPresent();
    }

    @Override
    public boolean isHandledOutput(ITypedIngredient<?> output) {
        if (output.getType() == VanillaTypes.ITEM_STACK) {
            ItemStack stack = output.getItemStack().orElse(ItemStack.EMPTY);
            return stack.is(ModItems.SANDWICH) && SandwichContents.get(stack).size() >= 2;
        }
        return false;
    }

    @Override
    public List<RECIPE> getRecipesForInput(ITypedIngredient<?> input) {
        if (input.getType() == NeoForgeTypes.FLUID_STACK) {
            Optional<ItemStack> filling = getFillingFromFluid(input.getCastIngredient(NeoForgeTypes.FLUID_STACK));
            if (filling.isEmpty()) {
                return List.of();
            }
            return getRecipesForFilling(filling.get());
        }

        ItemStack stack = input.getItemStack().orElseThrow();
        stack = stack.copyWithCount(1);

        if (stack.is(ModTags.SANDWICH_BREAD)) {
            ItemStack bottomBread = stack.is(ModItems.BURGER_BUN_TOP) ? new ItemStack(ModItems.BURGER_BUN_BOTTOM) : stack;
            ItemStack topBread = stack.is(ModItems.BURGER_BUN_BOTTOM) ? new ItemStack(ModItems.BURGER_BUN_TOP) : stack;
            return getRecipesForBread(bottomBread, topBread);
        } else {
            return getRecipesForFilling(stack);
        }
    }

    protected List<RECIPE> getRecipesForBread(ItemStack bottomBread, ItemStack topBread) {
        List<RECIPE> recipes = new ArrayList<>();
        Consumer<ItemStack> consumer = sandwich -> {
            SandwichContents contents = SandwichContents.get(sandwich);
            if (contents.getFirst().is(bottomBread.getItem())
                    && contents.getLast().is(topBread.getItem())) {
                recipes.add(getRecipeForSandwich(contents));
            }
        };
        ModCompat.gatherCreativeTabSandwiches(consumer);
        recipes.add(createRecipe(bottomBread, new ItemStack(Items.HONEY_BOTTLE), topBread));
        return recipes;
    }

    protected List<RECIPE> getRecipesForFilling(ItemStack filling) {
        List<RECIPE> recipes = new ArrayList<>();
        BuiltInRegistries.ITEM.getTag(ModTags.SANDWICH_BREAD).ifPresent(tag -> {
            for (Holder<Item> holder : tag) {
                if (!holder.is(ModTags.BURGER_BUNS)) {
                    ItemStack bread = new ItemStack(holder.value());
                    recipes.add(createRecipe(bread, filling, bread));
                }
            }
            ItemStack bottom = new ItemStack(ModItems.BURGER_BUN_BOTTOM);
            ItemStack top = new ItemStack(ModItems.BURGER_BUN_TOP);
            recipes.add(createRecipe(bottom, filling, top));
        });
        return recipes;
    }

    protected abstract Optional<ItemStack> getFillingFromFluid(FluidStack fluid);

    protected abstract Optional<FluidIngredient> getFluidFromFilling(ItemStack filling);

    protected RECIPE createRecipe(ItemStack bottomBread, ItemStack filling, ItemStack topBread) {
        if (filling.getCount() != 1) {
            filling = filling.copy();
            filling.setCount(1);
        }
        return getRecipeForSandwich(new SandwichContents(List.of(bottomBread, filling, topBread)));
    }

    @Override
    public List<RECIPE> getRecipesForOutput(ITypedIngredient<?> output) {
        ItemStack sandwich = output.getItemStack().orElse(ItemStack.EMPTY);
        return List.of(getRecipeForSandwich(SandwichContents.get(sandwich)));
    }

    protected RECIPE getRecipeForSandwich(SandwichContents contents) {
        List<ItemStack> items = contents;
        ItemStack prefix = items.getFirst();
        int size = items.size();
        if (size - 1 > getMaxToppings()) {
            prefix = SandwichItem.of(items.subList(0, size - getMaxToppings()));
            items = items.subList(size - getMaxToppings(), size);
        } else {
            items = items.subList(1, size);
        }
        return getRecipeForSandwich(prefix, items, contents.makeItem());
    }

    protected abstract RECIPE getRecipeForSandwich(ItemStack prefix, List<ItemStack> toppings, ItemStack result);

    @Override
    public List<RECIPE> getAllRecipes() {
        List<RECIPE> recipes = new ArrayList<>();
        ModCompat.gatherCreativeTabSandwiches(sandwich -> recipes.add(getRecipeForSandwich(SandwichContents.get(sandwich))));
        recipes.add(getRecipeForSandwich(SandwichContents.get(SandwichItem.makeToastSandwich(Items.HONEY_BOTTLE))));
        return recipes;
    }
}
