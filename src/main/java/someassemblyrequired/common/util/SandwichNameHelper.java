package someassemblyrequired.common.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import someassemblyrequired.common.init.Items;
import someassemblyrequired.common.init.Tags;

import java.util.*;
import java.util.stream.Collectors;

public class SandwichNameHelper {

    private static final Set<Item> INGREDIENT_NAME_OVERRIDES = new HashSet<>(Arrays.asList(
            Items.TOASTED_BREAD_SLICE.get(),
            Items.APPLE_SLICES.get(),
            Items.GOLDEN_APPLE_SLICES.get(),
            Items.ENCHANTED_GOLDEN_APPLE_SLICES.get(),
            Items.CHOPPED_CARROT.get(),
            Items.CHOPPED_GOLDEN_CARROT.get(),
            Items.CHOPPED_BEETROOT.get(),
            Items.PORK_CUTS.get(),
            Items.BACON_STRIPS.get(),
            Items.SLICED_TOASTED_CRIMSON_FUNGUS.get(),
            Items.SLICED_TOASTED_WARPED_FUNGUS.get(),
            Items.TOMATO_SLICES.get(),
            Items.LETTUCE_LEAF.get()
    ));

    public static ITextComponent getSandwichDisplayName(ItemStack sandwich) {
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
            return new TranslationTextComponent("item.someassemblyrequired.blt_sandwich");
        }

        // potion sandwich
        if (uniqueIngredients.size() == 1 && uniqueIngredients.get(0).getItem() == Items.SPREAD.get() && uniqueIngredients.get(0).getOrCreateTag().contains("Ingredient")) {
            ItemStack spreadItem = ItemStack.read(uniqueIngredients.get(0).getOrCreateChildTag("Ingredient"));
            if (spreadItem.getItem() == net.minecraft.item.Items.POTION) {
                Potion potion = PotionUtils.getPotionFromItem(spreadItem);
                if (potion == Potions.WATER) {
                    return new TranslationTextComponent("item.someassemblyrequired.soggy_sandwich");
                } else if (potion.getEffects().size() == 1) {
                    return new TranslationTextComponent("item.someassemblyrequired.potion_sandwich", potion.getEffects().get(0).getPotion().getDisplayName());
                }
            }
        }

        if (uniqueIngredients.size() > 0 && uniqueIngredients.size() <= 3) {
            ITextComponent ingredientList = listIngredients(uniqueIngredients);
            if (SandwichIngredientHelper.isDoubleDeckerSandwich(ingredients)) {
                return new TranslationTextComponent("item.someassemblyrequired.double_decker_ingredients_sandwich", ingredientList);
            } else {
                return new TranslationTextComponent("item.someassemblyrequired.ingredients_sandwich", ingredientList);
            }
        }

        if (SandwichIngredientHelper.isDoubleDeckerSandwich(ingredients)) {
            return new TranslationTextComponent("item.someassemblyrequired.double_decker_sandwich");
        } else {
            return new TranslationTextComponent("item.someassemblyrequired.sandwich");
        }
    }

    private static int getAmountOfBread(List<ItemStack> ingredients) {
        int result = 0;
        for (ItemStack ingredient : ingredients) {
            if (Tags.BREAD.contains(ingredient.getItem())) {
                result++;
            }
        }
        return result;
    }

    private static ITextComponent getBreadSandwichName(List<ItemStack> ingredients) {
        if ((ingredients.size() == 3)
                && ingredients.get(0).getItem() != Items.TOASTED_BREAD_SLICE.get()
                && ingredients.get(1).getItem() == Items.TOASTED_BREAD_SLICE.get()
                && ingredients.get(2).getItem() != Items.TOASTED_BREAD_SLICE.get()) {
            return new TranslationTextComponent("item.someassemblyrequired.ingredients_sandwich", getIngredientDisplayName(ingredients.get(1)));
        }
        return new TranslationTextComponent("item.someassemblyrequired.bread_sandwich");
    }

    private static ITextComponent getIngredientDisplayName(ItemStack ingredient) {
        if (!ingredient.hasDisplayName() && INGREDIENT_NAME_OVERRIDES.contains(ingredient.getItem())) {
            // noinspection ConstantConditions
            return new TranslationTextComponent(
                    String.format("ingredient.%s.%s",
                            ingredient.getItem().getRegistryName().getNamespace(),
                            ingredient.getItem().getRegistryName().getPath()
                    )
            );
        }
        return ingredient.getDisplayName();
    }

    private static ITextComponent listIngredients(List<ItemStack> ingredients) {
        List<ITextComponent> ingredientNames = ingredients.stream().map(SandwichNameHelper::getIngredientDisplayName).collect(Collectors.toList());
        return new TranslationTextComponent("tooltip.someassemblyrequired.ingredient_list." + ingredientNames.size(), ingredientNames.toArray());
    }
}
