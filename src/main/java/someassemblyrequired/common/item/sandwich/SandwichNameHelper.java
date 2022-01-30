package someassemblyrequired.common.item.sandwich;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.ingredient.Ingredients;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;
import someassemblyrequired.common.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SandwichNameHelper {

    public static Component getSandwichDisplayName(ItemStack stack) {
        SandwichItemHandler sandwich = SandwichItemHandler.get(stack).orElse(null);

        if (sandwich == null || !ItemTags.getAllTags().hasTag(ModTags.SANDWICH_BREAD.getName())) {
            return translateItem("sandwich");
        }

        int amountOfBread = getAmountOfBread(sandwich);

        // full bread sandwich
        if (sandwich.size() == amountOfBread) {
            return getBreadSandwichName(sandwich);
        }

        List<ItemStack> uniqueIngredients = getUniqueIngredientsExcludingBread(sandwich);

        // BLT
        if (isBLT(sandwich)) {
            return translateItem("blt_sandwich");
        }

        // potion sandwich
        if (uniqueIngredients.size() == 1 && uniqueIngredients.get(0).is(Items.POTION)) {
            Potion potion = PotionUtils.getPotion(uniqueIngredients.get(0));
            if (potion == Potions.WATER) {
                return translateItem("soggy_sandwich");
            } else if (potion.getEffects().size() == 1) {
                return translateItem("potion_sandwich", potion.getEffects().get(0).getEffect().getDisplayName());
            }
        }

        boolean isOpenFacedSandwich = amountOfBread == 1 && sandwich.size() > 1;

        if (uniqueIngredients.size() > 0 && uniqueIngredients.size() <= 3) {
            Component ingredientList = listIngredients(uniqueIngredients);
            if (sandwich.isDoubleDeckerSandwich()) {
                return translateItem("double_decker_ingredients_sandwich", ingredientList);
            } else if (isOpenFacedSandwich) {
                return translateItem("open_faced_ingredients_sandwich", ingredientList);
            } else {
                return translateItem("ingredients_sandwich", ingredientList);
            }
        }

        if (sandwich.isDoubleDeckerSandwich()) {
            return translateItem("double_decker_sandwich");
        } else if (isOpenFacedSandwich) {
            return translateItem("open_faced_sandwich");
        } else {
            return translateItem("sandwich");
        }
    }

    private static List<ItemStack> getUniqueIngredientsExcludingBread(SandwichItemHandler sandwich) {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack ingredient : sandwich) {
            if (!ingredient.is(ModTags.SANDWICH_BREAD) && result.stream().noneMatch(stack -> ItemStack.matches(ingredient, stack))) {
                result.add(ingredient);
            }
        }
        return result;
    }

    private static int getAmountOfBread(SandwichItemHandler sandwich) {
        int result = 0;
        for (ItemStack ingredient : sandwich) {
            if (ingredient.is(ModTags.SANDWICH_BREAD)) {
                result++;
            }
        }
        return result;
    }

    private static Component getBreadSandwichName(SandwichItemHandler sandwich) {
        if ((sandwich.size() == 3)
                && sandwich.getStackInSlot(0).getItem() != ModItems.TOASTED_BREAD_SLICE.get()
                && sandwich.getStackInSlot(1).getItem() == ModItems.TOASTED_BREAD_SLICE.get()
                && sandwich.getStackInSlot(2).getItem() != ModItems.TOASTED_BREAD_SLICE.get()) {
            return translateItem("ingredients_sandwich", Ingredients.getDisplayName(sandwich.getStackInSlot(1)));
        }
        return translateItem("bread_sandwich");
    }

    private static Component listIngredients(List<ItemStack> ingredients) {
        List<Component> ingredientNames = ingredients.stream().map(Ingredients::getDisplayName).collect(Collectors.toList());
        return Util.translate("tooltip.ingredient_list.%s".formatted(ingredientNames.size()), ingredientNames.toArray());
    }

    private static Component translateItem(String name, Object... args) {
        return new TranslatableComponent("item.%s.%s".formatted(SomeAssemblyRequired.MODID, name), args);
    }

    private static boolean isBLT(SandwichItemHandler sandwich) {
        if (!sandwich.hasTopAndBottomBread()) {
            return false;
        }

        boolean hasBacon = false;
        boolean hasLettuce = false;
        boolean hasTomato = false;

        for (ItemStack stack : sandwich) {
            if (stack.is(ModTags.COOKED_BACON)) {
                hasBacon = true;
            } else if (stack.is(ModTags.SALAD_INGREDIENTS)) {
                hasLettuce = true;
            } else if (stack.is(ModTags.VEGETABLES_TOMATO)) {
                hasTomato = true;
            } else if (!stack.is(ModTags.SANDWICH_BREAD)) {
                return false;
            }
        }

        return hasBacon && hasLettuce && hasTomato;
    }
}
