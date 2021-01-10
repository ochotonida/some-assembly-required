package someasseblyrequired.common.item;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import someasseblyrequired.common.init.Items;
import someasseblyrequired.common.init.Tags;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SandwichNameHelper {

    @Nullable
    public static IFormattableTextComponent getSandwichDisplayName(ItemStack stack) {
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(EmptyHandler.INSTANCE);
        List<ItemStack> ingredients = new ArrayList<>();
        for (int slot = 0; slot < handler.getSlots() && !handler.getStackInSlot(slot).isEmpty(); slot++) {
            ingredients.add(handler.getStackInSlot(slot));
        }
        return getSandwichDisplayName(ingredients);
    }

    public static IFormattableTextComponent getSandwichDisplayName(List<ItemStack> ingredients) {
        int breadAmount = (int) ingredients.stream().filter(ingredient -> Tags.BREAD.contains(ingredient.getItem())).count();

        // BLT
        if (ingredients.size() == 5 && breadAmount == 2
                && ingredients.stream().anyMatch(stack -> stack.getItem() == Items.BACON_STRIPS.get())
                && ingredients.stream().anyMatch(stack -> stack.getItem() == Items.TOMATO_SLICES.get() || Tags.TOMATOES.contains(stack.getItem()))
                && ingredients.stream().anyMatch(stack -> Tags.LETTUCE.contains(stack.getItem()))) {
            return new TranslationTextComponent("item.someassemblyrequired.blt_sandwich");
        }

        // full bread sandwich
        if (breadAmount == ingredients.size() && (ingredients.size() != 3 || ingredients.get(1).getItem() != Items.TOASTED_BREAD_SLICE.get())) {
            return new TranslationTextComponent("item.someassemblyrequired.snadwich");
        }

        // sandwich with a single non-bread ingredient
        IFormattableTextComponent result = getSingleIngredientName(ingredients);
        if (result != null) {
            return result;
        }

        // two ingredients
        if (ingredients.size() == 4 && (ingredients.get(1).getItem() != ingredients.get(2).getItem() || ingredients.get(1).getItem() == Items.SPREAD.get() && !ItemStack.areItemStacksEqual(ingredients.get(1), ingredients.get(2)))) {
            return new TranslationTextComponent("item.someassemblyrequired.double_ingredient_sandwich", getIngredientDisplayName(ingredients.get(1)), getIngredientDisplayName(ingredients.get(2)));
        }

        // sandwich with alternating layers of bread and one specific ingredient
        result = getStackedSandwichName(ingredients);
        if (result != null) {
            return result;
        }

        if (breadAmount == 3) {
            return new TranslationTextComponent("item.someassemblyrequired.double_decker_sandwich");
        }

        return null;
    }

    @Nullable
    private static IFormattableTextComponent getSingleIngredientName(List<ItemStack> ingredients) {
        if (ingredients.size() != 3) {
            return null;
        }
        if (ingredients.get(1).getItem() == Items.SPREAD.get() && ingredients.get(1).getOrCreateTag().contains("Ingredient")) {
            ItemStack spreadItem = ItemStack.read(ingredients.get(1).getOrCreateChildTag("Ingredient"));
            if (spreadItem.getItem() == net.minecraft.item.Items.POTION) {
                Potion potion = PotionUtils.getPotionFromItem(spreadItem);
                if (potion == Potions.WATER) {
                    return new TranslationTextComponent("item.someassemblyrequired.soggy_sandwich");
                } else if (potion.getEffects().size() == 1) {
                    return new TranslationTextComponent("item.someassemblyrequired.potion_sandwich", potion.getEffects().get(0).getPotion().getDisplayName());
                }
            }
        }
        return new TranslationTextComponent("item.someassemblyrequired.single_ingredient_sandwich", getIngredientDisplayName(ingredients.get(1)));
    }

    @Nullable
    private static IFormattableTextComponent getStackedSandwichName(List<ItemStack> ingredients) {
        if (ingredients.size() >= 5 && ingredients.size() % 2 != 0) {
            int index;
            for (index = 2; index < ingredients.size(); index++) {
                ItemStack ingredient = ingredients.get(index);
                if (index % 2 == 0) {
                    if ((!Tags.BREAD.contains(ingredient.getItem()) || ingredient.getItem() == ingredients.get(1).getItem())) {
                        break;
                    }
                } else if ((Tags.BREAD.contains(ingredient.getItem()) && ingredient.getItem() != Items.TOASTED_BREAD_SLICE.get()) || !ItemStack.areItemStacksEqual(ingredient, ingredients.get(1))) {
                    break;
                }
            }
            if (index == ingredients.size()) {
                return new TranslationTextComponent("item.someassemblyrequired." + getQuantifier(ingredients.size() / 2) + "_stacked_sandwich", getIngredientDisplayName(ingredients.get(1)));
            }
        }
        return null;
    }

    private static String getQuantifier(int number) {
        switch (number) {
            case 2:
                return "double";
            case 3:
                return "triple";
            case 4:
                return "quadruple";
            case 5:
                return "quintuple";
            case 6:
                return "sextuple";
            case 7:
                return "septuple";
            default:
                return "very_large";
        }
    }

    private static IFormattableTextComponent getIngredientDisplayName(ItemStack ingredient) {
        if (ingredient.hasDisplayName() || !Arrays.asList(
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
                Items.LETTUCE_LEAF
        ).contains(ingredient.getItem())) {
            return ingredient.getDisplayName().copyRaw();
        }
        ResourceLocation registryName = ingredient.getItem().getRegistryName();
        // noinspection ConstantConditions
        return new TranslationTextComponent("ingredient." + registryName.getNamespace() + "." + registryName.getPath());
    }
}
