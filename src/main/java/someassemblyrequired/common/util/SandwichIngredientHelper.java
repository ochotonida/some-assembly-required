package someassemblyrequired.common.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import someassemblyrequired.common.init.Items;
import someassemblyrequired.common.init.Tags;

import java.util.ArrayList;
import java.util.List;

public class SandwichIngredientHelper {

    public static List<ItemStack> getIngredients(ItemStack sandwich) {
        List<ItemStack> ingredients = new ArrayList<>();
        sandwich.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            for (int slot = 0; slot < handler.getSlots() && !handler.getStackInSlot(slot).isEmpty(); slot++) {
                ingredients.add(handler.getStackInSlot(slot));
            }
        });
        return ingredients;
    }

    public static List<ItemStack> getUniqueIngredientsExcludingBread(List<ItemStack> ingredients) {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack ingredient : ingredients) {
            if (!Tags.BREAD.contains(ingredient.getItem()) && result.stream().noneMatch(stack -> ItemStack.areItemStacksEqual(ingredient, stack))) {
                result.add(ingredient);
            }
        }
        return result;
    }

    public static boolean isDoubleDeckerSandwich(List<ItemStack> ingredients) {
        if (ingredients.size() < 5) {
            return false;
        }

        int breadAmount = 0;
        for (ItemStack ingredient : ingredients) {
            if (Tags.BREAD.contains(ingredient.getItem())) {
                if (++breadAmount > 3) {
                    return false;
                }
            }
        }
        return breadAmount == 3 && !Tags.BREAD.contains(ingredients.get(1).getItem()) && !Tags.BREAD.contains(ingredients.get(ingredients.size() - 2).getItem());
    }

    public static boolean isBLT(List<ItemStack> uniqueIngredients) {
        return uniqueIngredients.size() == 3
                && uniqueIngredients.stream().anyMatch(stack -> stack.getItem() == Items.BACON_STRIPS.get())
                && uniqueIngredients.stream().anyMatch(stack -> stack.getItem() == Items.TOMATO_SLICES.get() || Tags.TOMATOES.contains(stack.getItem()))
                && uniqueIngredients.stream().anyMatch(stack -> Tags.LETTUCE.contains(stack.getItem()));
    }
}
