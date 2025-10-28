package someassemblyrequired.integration.jei;

import com.simibubi.create.foundation.fluid.FluidIngredient;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.advanced.ISimpleRecipeManagerPlugin;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.FluidStack;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.item.sandwich.SandwichItemHandler;
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
        } else if (input.getType() == ForgeTypes.FLUID_STACK) {
            return isHandledFilling(input.getIngredient(ForgeTypes.FLUID_STACK).orElseThrow());
        }
        return false;
    }

    protected boolean isHandledFilling(ItemStack filling) {
        return !filling.is(ModItems.SANDWICH.get())
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
            return stack.is(ModItems.SANDWICH.get()) && SandwichItemHandler.get(stack).orElse(new SandwichItemHandler()).getItemCount() >= 2;
        }
        return false;
    }

    @Override
    public List<RECIPE> getRecipesForInput(ITypedIngredient<?> input) {
        if (input.getType() == ForgeTypes.FLUID_STACK) {
            Optional<ItemStack> filling = getFillingFromFluid(input.getIngredient(ForgeTypes.FLUID_STACK).orElseThrow());
            if (filling.isEmpty()) {
                return List.of();
            }
            return getRecipesForFilling(filling.get());
        }

        ItemStack stack = input.getItemStack().orElseThrow();
        stack = stack.copyWithCount(1);

        if (stack.is(ModTags.SANDWICH_BREAD)) {
            ItemStack bottomBread = stack.is(ModItems.BURGER_BUN_TOP.get()) ? new ItemStack(ModItems.BURGER_BUN_BOTTOM.get()) : stack;
            ItemStack topBread = stack.is(ModItems.BURGER_BUN_BOTTOM.get()) ? new ItemStack(ModItems.BURGER_BUN_TOP.get()) : stack;
            return getRecipesForBread(bottomBread, topBread);
        } else {
            return getRecipesForFilling(stack);
        }
    }

    protected List<RECIPE> getRecipesForBread(ItemStack bottomBread, ItemStack topBread) {
        List<RECIPE> recipes = new ArrayList<>();
        Consumer<ItemStack> consumer = sandwich -> {
            SandwichItemHandler contents = SandwichItemHandler.get(sandwich).orElse(new SandwichItemHandler());
            if (contents.bottom().is(bottomBread.getItem())
                    && contents.top().is(topBread.getItem())) {
                recipes.add(getRecipeForSandwich(contents));
            }
        };
        ModCompat.gatherCreativeTabSandwiches(consumer);
        recipes.add(createRecipe(bottomBread, new ItemStack(Items.HONEY_BOTTLE), topBread));
        return recipes;
    }

    @SuppressWarnings("deprecation")
    protected List<RECIPE> getRecipesForFilling(ItemStack filling) {
        List<RECIPE> recipes = new ArrayList<>();
        BuiltInRegistries.ITEM.getTag(ModTags.SANDWICH_BREAD).ifPresent(tag -> {
            for (Holder<Item> holder : tag) {
                if (!holder.is(ModTags.BURGER_BUNS)) {
                    ItemStack bread = new ItemStack(holder.value());
                    recipes.add(createRecipe(bread, filling, bread));
                }
            }
            ItemStack bottom = new ItemStack(ModItems.BURGER_BUN_BOTTOM.get());
            ItemStack top = new ItemStack(ModItems.BURGER_BUN_TOP.get());
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
        return getRecipeForSandwich(SandwichItemHandler.of(List.of(bottomBread, filling, topBread)));
    }

    @Override
    public List<RECIPE> getRecipesForOutput(ITypedIngredient<?> output) {
        ItemStack sandwich = output.getItemStack().orElse(ItemStack.EMPTY);
        return List.of(getRecipeForSandwich(SandwichItemHandler.get(sandwich).orElse(new SandwichItemHandler())));
    }

    protected RECIPE getRecipeForSandwich(SandwichItemHandler contents) {
        List<ItemStack> items = contents.getItems();
        ItemStack prefix = items.get(0);
        int size = items.size();
        if (size - 1 > getMaxToppings()) {
            prefix = SandwichItem.of(items.subList(0, size - getMaxToppings()));
            items = items.subList(size - getMaxToppings(), size);
        } else {
            items = items.subList(1, size);
        }
        return getRecipeForSandwich(prefix, items, SandwichItem.of(contents.getItems()));
    }

    protected abstract RECIPE getRecipeForSandwich(ItemStack prefix, List<ItemStack> toppings, ItemStack result);

    @Override
    public List<RECIPE> getAllRecipes() {
        List<RECIPE> recipes = new ArrayList<>();
        ModCompat.gatherCreativeTabSandwiches(sandwich -> recipes.add(getRecipeForSandwich(SandwichItemHandler.get(sandwich).orElse(new SandwichItemHandler()))));
        recipes.add(getRecipeForSandwich(SandwichItemHandler.get(SandwichItem.makeToastSandwich(Items.HONEY_BOTTLE)).orElseThrow()));
        return recipes;
    }
}