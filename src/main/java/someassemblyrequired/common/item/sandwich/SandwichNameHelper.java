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
import someassemblyrequired.common.ingredient.CustomIngredients;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SandwichNameHelper {

    public static Component getSandwichDisplayName(ItemStack stack) {
        SandwichItemHandler sandwich = SandwichItemHandler.get(stack).orElse(null);

        if (sandwich == null || !ItemTags.getAllTags().hasTag(ModTags.BREAD_SLICES.getName())) {
            return new TranslatableComponent("item.%s.sandwich".formatted(SomeAssemblyRequired.MODID));
        }

        List<ItemStack> ingredients = new ArrayList<>();
        sandwich.forEach(ingredients::add);

        int amountOfBread = getAmountOfBread(ingredients);

        // full bread sandwich
        if (ingredients.size() == amountOfBread) {
            return getBreadSandwichName(ingredients);
        }

        List<ItemStack> uniqueIngredients = getUniqueIngredientsExcludingBread(ingredients);

        // BLT
        if (sandwich.isBLT()) {
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

        if (uniqueIngredients.size() > 0 && uniqueIngredients.size() <= 3) {
            Component ingredientList = listIngredients(uniqueIngredients);
            if (sandwich.isDoubleDeckerSandwich()) {
                return new TranslatableComponent("item.%s.double_decker_ingredients_sandwich".formatted(SomeAssemblyRequired.MODID), ingredientList);
            } else {
                return new TranslatableComponent("item.%s.ingredients_sandwich".formatted(SomeAssemblyRequired.MODID), ingredientList);
            }
        }

        if (sandwich.isDoubleDeckerSandwich()) {
            return new TranslatableComponent("item.%s.double_decker_sandwich".formatted(SomeAssemblyRequired.MODID));
        } else {
            return new TranslatableComponent("item.%s.sandwich".formatted(SomeAssemblyRequired.MODID));
        }
    }

    private static List<ItemStack> getUniqueIngredientsExcludingBread(List<ItemStack> ingredients) {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack ingredient : ingredients) {
            if (!ingredient.is(ModTags.BREAD_SLICES) && result.stream().noneMatch(stack -> ItemStack.matches(ingredient, stack))) {
                result.add(ingredient);
            }
        }
        return result;
    }

    private static int getAmountOfBread(List<ItemStack> ingredients) {
        int result = 0;
        for (ItemStack ingredient : ingredients) {
            if (ingredient.is(ModTags.BREAD_SLICES)) {
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
            return new TranslatableComponent("item.%s.ingredients_sandwich".formatted(SomeAssemblyRequired.MODID), CustomIngredients.getDisplayName(ingredients.get(1)));
        }
        return new TranslatableComponent("item.%s.bread_sandwich".formatted(SomeAssemblyRequired.MODID));
    }

    private static Component listIngredients(List<ItemStack> ingredients) {
        List<Component> ingredientNames = ingredients.stream().map(CustomIngredients::getDisplayName).collect(Collectors.toList());
        return new TranslatableComponent("tooltip.%s.ingredient_list.".formatted(SomeAssemblyRequired.MODID) + ingredientNames.size(), ingredientNames.toArray());
    }
}
