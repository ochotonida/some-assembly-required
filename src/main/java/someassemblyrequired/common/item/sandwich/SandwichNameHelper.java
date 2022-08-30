package someassemblyrequired.common.item.sandwich;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.ingredient.Ingredients;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;
import someassemblyrequired.common.util.Util;

import java.util.ArrayList;
import java.util.List;

public class SandwichNameHelper {

    public static Component getSandwichDisplayName(ItemStack stack) {
        SandwichItemHandler sandwich = SandwichItemHandler.get(stack).orElse(null);

        // noinspection ConstantConditions
        if (sandwich == null
                || sandwich.getItemCount() == 0
                || !ForgeRegistries.ITEMS.tags().isKnownTagName(ModTags.SANDWICH_BREAD)
                || !ForgeRegistries.ITEMS.tags().isKnownTagName(ModTags.BURGER_BUNS)
        ) {
            return translateItem("sandwich");
        }

        String sandwichName = sandwich.top().is(ModTags.BURGER_BUNS)
                && sandwich.bottom().is(ModTags.BURGER_BUNS)
                ? "burger" : "sandwich";

        int amountOfBread = getAmountOfBread(sandwich);

        // full bread sandwich
        if (sandwich.getItemCount() == amountOfBread) {
            return getBreadSandwichName(sandwich, sandwichName);
        }

        List<ItemStack> uniqueIngredients = getUniqueIngredientsExcludingBread(sandwich);

        // potion sandwich
        if (uniqueIngredients.size() == 1 && uniqueIngredients.get(0).is(Items.POTION)) {
            Potion potion = PotionUtils.getPotion(uniqueIngredients.get(0));
            if (potion.getEffects().size() == 1) {
                return translateItem("potion_%s".formatted(sandwichName), potion.getEffects().get(0).getEffect().getDisplayName());
            }
        }

        boolean isOpenFacedSandwich = amountOfBread == 1 && sandwich.getItemCount() > 1;

        if (uniqueIngredients.size() > 0 && uniqueIngredients.size() <= 3) {
            Component ingredientList = listIngredients(uniqueIngredients);
            if (sandwich.isDoubleDeckerSandwich()) {
                return translateItem("double_decker_ingredients_%s".formatted(sandwichName), ingredientList);
            } else if (isOpenFacedSandwich) {
                return translateItem("open_faced_ingredients_sandwich", ingredientList);
            } else {
                return translateItem("ingredients_%s".formatted(sandwichName), ingredientList);
            }
        }

        if (sandwich.isDoubleDeckerSandwich()) {
            return translateItem("double_decker_%s".formatted(sandwichName));
        } else if (isOpenFacedSandwich) {
            return translateItem("open_faced_sandwich");
        } else {
            return translateItem(sandwichName);
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

    private static Component getBreadSandwichName(SandwichItemHandler sandwich, String sandwichName) {
        if ((sandwich.getItemCount() == 3)
                && sandwich.getStackInSlot(0).getItem() != ModItems.TOASTED_BREAD_SLICE.get()
                && sandwich.getStackInSlot(1).getItem() == ModItems.TOASTED_BREAD_SLICE.get()
                && sandwich.getStackInSlot(2).getItem() != ModItems.TOASTED_BREAD_SLICE.get()) {
            return translateItem("ingredients_%s".formatted(sandwichName), Ingredients.getDisplayName(sandwich.getStackInSlot(1)));
        }
        return translateItem("bread_%s".formatted(sandwichName));
    }

    private static Component listIngredients(List<ItemStack> ingredients) {
        List<Component> ingredientNames = ingredients.stream().map(Ingredients::getDisplayName).toList();
        return Util.translate("tooltip.ingredient_list.%s".formatted(ingredientNames.size()), ingredientNames.toArray());
    }

    private static Component translateItem(String name, Object... args) {
        return Component.translatable("item.%s.%s".formatted(SomeAssemblyRequired.MODID, name), args);
    }
}
