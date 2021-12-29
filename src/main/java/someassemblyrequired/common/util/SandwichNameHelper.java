package someassemblyrequired.common.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;

import java.util.*;
import java.util.stream.Collectors;

public class SandwichNameHelper {

    private static final Set<Item> INGREDIENT_NAME_OVERRIDES = new HashSet<>(Arrays.asList(
            ModItems.TOASTED_BREAD_SLICE.get(),
            ModItems.APPLE_SLICES.get(),
            ModItems.GOLDEN_APPLE_SLICES.get(),
            ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(),
            ModItems.CHOPPED_CARROT.get(),
            ModItems.CHOPPED_GOLDEN_CARROT.get(),
            ModItems.CHOPPED_BEETROOT.get(),
            ModItems.PORK_CUTS.get(),
            ModItems.BACON_STRIPS.get(),
            ModItems.SLICED_TOASTED_CRIMSON_FUNGUS.get(),
            ModItems.SLICED_TOASTED_WARPED_FUNGUS.get(),
            ModItems.TOMATO_SLICES.get(),
            ModItems.LETTUCE_LEAF.get()
    ));

    public static Component getSandwichDisplayName(ItemStack sandwich) {
        IItemHandler handler = sandwich.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(EmptyHandler.INSTANCE);
        List<ItemStack> ingredients = new ArrayList<>();
        for (int slot = 0; slot < handler.getSlots() && !handler.getStackInSlot(slot).isEmpty(); slot++) {
            ingredients.add(handler.getStackInSlot(slot));
        }

        int amountOfBread = getAmountOfBread(ingredients);

        // full bread sandwich
        if (ingredients.size() == amountOfBread) {
            return getBreadSandwichName(ingredients);
        }

        List<ItemStack> uniqueIngredients = SandwichIngredientHelper.getUniqueIngredientsExcludingBread(ingredients);

        // BLT
        if (SandwichIngredientHelper.isBLT(uniqueIngredients)) {
            return new TranslatableComponent("item.someassemblyrequired.blt_sandwich");
        }

        // potion sandwich
        if (uniqueIngredients.size() == 1 && uniqueIngredients.get(0).getItem() == ModItems.SPREAD.get() && uniqueIngredients.get(0).getOrCreateTag().contains("Ingredient")) {
            ItemStack spreadItem = ItemStack.of(uniqueIngredients.get(0).getOrCreateTagElement("Ingredient"));
            if (spreadItem.getItem() == Items.POTION) {
                Potion potion = PotionUtils.getPotion(spreadItem);
                if (potion == Potions.WATER) {
                    return new TranslatableComponent("item.someassemblyrequired.soggy_sandwich");
                } else if (potion.getEffects().size() == 1) {
                    return new TranslatableComponent("item.someassemblyrequired.potion_sandwich", potion.getEffects().get(0).getEffect().getDisplayName());
                }
            }
        }

        if (uniqueIngredients.size() > 0 && uniqueIngredients.size() <= 3) {
            Component ingredientList = listIngredients(uniqueIngredients);
            if (SandwichIngredientHelper.isDoubleDeckerSandwich(ingredients)) {
                return new TranslatableComponent("item.someassemblyrequired.double_decker_ingredients_sandwich", ingredientList);
            } else {
                return new TranslatableComponent("item.someassemblyrequired.ingredients_sandwich", ingredientList);
            }
        }

        if (SandwichIngredientHelper.isDoubleDeckerSandwich(ingredients)) {
            return new TranslatableComponent("item.someassemblyrequired.double_decker_sandwich");
        } else {
            return new TranslatableComponent("item.someassemblyrequired.sandwich");
        }
    }

    private static int getAmountOfBread(List<ItemStack> ingredients) {
        int result = 0;
        for (ItemStack ingredient : ingredients) {
            if (ModTags.SANDWICH_BREADS.contains(ingredient.getItem())) {
                result++;
            }
        }
        return result;
    }

    private static Component getBreadSandwichName(List<ItemStack> ingredients) {
        if ((ingredients.size() == 3)
                && ingredients.get(0).getItem() != ModItems.TOASTED_BREAD_SLICE.get()
                && ingredients.get(1).getItem() == ModItems.TOASTED_BREAD_SLICE.get()
                && ingredients.get(2).getItem() != ModItems.TOASTED_BREAD_SLICE.get()) {
            return new TranslatableComponent("item.someassemblyrequired.ingredients_sandwich", getIngredientDisplayName(ingredients.get(1)));
        }
        return new TranslatableComponent("item.someassemblyrequired.bread_sandwich");
    }

    private static Component getIngredientDisplayName(ItemStack ingredient) {
        if (!ingredient.hasCustomHoverName() && INGREDIENT_NAME_OVERRIDES.contains(ingredient.getItem())) {
            // noinspection ConstantConditions
            return new TranslatableComponent(
                    String.format("ingredient.%s.%s",
                            ingredient.getItem().getRegistryName().getNamespace(),
                            ingredient.getItem().getRegistryName().getPath()
                    )
            );
        }
        return ingredient.getHoverName();
    }

    private static Component listIngredients(List<ItemStack> ingredients) {
        List<Component> ingredientNames = ingredients.stream().map(SandwichNameHelper::getIngredientDisplayName).collect(Collectors.toList());
        return new TranslatableComponent("tooltip.someassemblyrequired.ingredient_list." + ingredientNames.size(), ingredientNames.toArray());
    }
}
