package someassemblyrequired.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import someassemblyrequired.client.ingredient.IngredientInfoManager;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SandwichNameHelper {

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
        if (uniqueIngredients.size() == 1 && uniqueIngredients.get(0).getItem() == ModItems.SPREAD.get() && uniqueIngredients.get(0).getOrCreateTag().contains("Ingredient")) {
            ItemStack spreadItem = ItemStack.read(uniqueIngredients.get(0).getOrCreateChildTag("Ingredient"));
            if (spreadItem.getItem() == Items.POTION) {
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

    private static ITextComponent getIngredientName(ItemStack ingredient) {
        if (!ingredient.hasDisplayName()) {
            if (ingredient.getItem() == Items.POTION && PotionUtils.getPotionFromItem(ingredient) == Potions.WATER) {
                return new TranslationTextComponent("ingredient.someassemblyrequired.water_bottle");
            } else {
                ITextComponent name = IngredientInfoManager.INSTANCE.getDisplayName(ingredient.getItem());
                if (name != null) {
                    return name;
                }
            }
        }
        return ingredient.getDisplayName();
    }

    private static int getAmountOfBread(List<ItemStack> ingredients) {
        int result = 0;
        for (ItemStack ingredient : ingredients) {
            if (ModTags.isBread(ingredient.getItem())) {
                result++;
            }
        }
        return result;
    }

    private static ITextComponent getBreadSandwichName(List<ItemStack> ingredients) {
        if ((ingredients.size() == 3)
                && ingredients.get(0).getItem() != ModItems.TOASTED_BREAD_SLICE.get()
                && ingredients.get(1).getItem() == ModItems.TOASTED_BREAD_SLICE.get()
                && ingredients.get(2).getItem() != ModItems.TOASTED_BREAD_SLICE.get()) {
            return new TranslationTextComponent("item.someassemblyrequired.ingredients_sandwich", getIngredientName(ingredients.get(1)));
        }
        return new TranslationTextComponent("item.someassemblyrequired.bread_sandwich");
    }

    private static ITextComponent listIngredients(List<ItemStack> ingredients) {
        List<ITextComponent> ingredientNames = ingredients.stream().map(SandwichNameHelper::getIngredientName).collect(Collectors.toList());
        return new TranslationTextComponent("tooltip.someassemblyrequired.ingredient_list." + ingredientNames.size(), ingredientNames.toArray());
    }
}
