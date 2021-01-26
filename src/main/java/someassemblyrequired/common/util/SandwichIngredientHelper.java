package someassemblyrequired.common.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;

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
            if (!ModTags.SANDWICH_BREADS.contains(ingredient.getItem()) && result.stream().noneMatch(stack -> ItemStack.areItemStacksEqual(ingredient, stack))) {
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
            if (ModTags.SANDWICH_BREADS.contains(ingredient.getItem())) {
                if (++breadAmount > 3) {
                    return false;
                }
            }
        }
        return breadAmount == 3 && !ModTags.SANDWICH_BREADS.contains(ingredients.get(1).getItem()) && !ModTags.SANDWICH_BREADS.contains(ingredients.get(ingredients.size() - 2).getItem());
    }

    public static boolean isBLT(List<ItemStack> uniqueIngredients) {
        return uniqueIngredients.size() == 3
                && uniqueIngredients.stream().anyMatch(stack -> ModTags.COOKED_BACON.contains(stack.getItem()))
                && uniqueIngredients.stream().anyMatch(stack -> stack.getItem() == ModItems.TOMATO_SLICES.get() || ModTags.CROPS_TOMATOES.contains(stack.getItem()))
                && uniqueIngredients.stream().anyMatch(stack -> ModTags.CROPS_LETTUCE.contains(stack.getItem()));
    }
}
