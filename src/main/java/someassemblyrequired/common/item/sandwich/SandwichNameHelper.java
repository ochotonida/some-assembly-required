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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SandwichNameHelper {

    public static Component getSandwichDisplayName(ItemStack stack) {
        SandwichItemHandler sandwich = SandwichItemHandler.get(stack).orElse(null);

        if (sandwich == null || !ItemTags.getAllTags().hasTag(ModTags.SANDWICH_BREAD.getName())) {
            return new TranslatableComponent("item.%s.sandwich".formatted(SomeAssemblyRequired.MODID));
        }

        int amountOfBread = getAmountOfBread(sandwich);

        // full bread sandwich
        if (sandwich.size() == amountOfBread) {
            return getBreadSandwichName(sandwich);
        }

        List<ItemStack> uniqueIngredients = getUniqueIngredientsExcludingBread(sandwich);

        // BLT
        if (isBLT(sandwich)) {
            return new TranslatableComponent("item.%s.blt_sandwich".formatted(SomeAssemblyRequired.MODID));
        }

        // potion sandwich
        if (uniqueIngredients.size() == 1 && uniqueIngredients.get(0).is(Items.POTION)) {
            Potion potion = PotionUtils.getPotion(uniqueIngredients.get(0));
            if (potion == Potions.WATER) {
                return new TranslatableComponent("item.%s.soggy_sandwich".formatted(SomeAssemblyRequired.MODID));
            } else if (potion.getEffects().size() == 1) {
                return new TranslatableComponent("item.%s.potion_sandwich".formatted(SomeAssemblyRequired.MODID), potion.getEffects().get(0).getEffect().getDisplayName());
            }
        }

        boolean isOpenFacedSandwich = amountOfBread == 1 && sandwich.size() > 1;

        if (uniqueIngredients.size() > 0 && uniqueIngredients.size() <= 3) {
            Component ingredientList = listIngredients(uniqueIngredients);
            if (sandwich.isDoubleDeckerSandwich()) {
                return new TranslatableComponent("item.%s.double_decker_ingredients_sandwich".formatted(SomeAssemblyRequired.MODID), ingredientList);
            } else if (isOpenFacedSandwich) {
                return new TranslatableComponent("item.%s.open_faced_ingredients_sandwich".formatted(SomeAssemblyRequired.MODID), ingredientList);
            } else {
                return new TranslatableComponent("item.%s.ingredients_sandwich".formatted(SomeAssemblyRequired.MODID), ingredientList);
            }
        }

        if (sandwich.isDoubleDeckerSandwich()) {
            return new TranslatableComponent("item.%s.double_decker_sandwich".formatted(SomeAssemblyRequired.MODID));
        } else if (isOpenFacedSandwich) {
            return new TranslatableComponent("item.%s.open_faced_sandwich".formatted(SomeAssemblyRequired.MODID));
        } else {
            return new TranslatableComponent("item.%s.sandwich".formatted(SomeAssemblyRequired.MODID));
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
            return new TranslatableComponent("item.%s.ingredients_sandwich".formatted(SomeAssemblyRequired.MODID), Ingredients.getDisplayName(sandwich.getStackInSlot(1)));
        }
        return new TranslatableComponent("item.%s.bread_sandwich".formatted(SomeAssemblyRequired.MODID));
    }

    private static Component listIngredients(List<ItemStack> ingredients) {
        List<Component> ingredientNames = ingredients.stream().map(Ingredients::getDisplayName).collect(Collectors.toList());
        return new TranslatableComponent("tooltip.%s.ingredient_list.".formatted(SomeAssemblyRequired.MODID) + ingredientNames.size(), ingredientNames.toArray());
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
