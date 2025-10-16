package someassemblyrequired.item.sandwich;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.fml.loading.FMLEnvironment;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModTags;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SandwichNameHelper {

    public static Component getSandwichDisplayName(ItemStack stack) {
        SandwichContents contents = SandwichContents.get(stack);

        if (contents.isEmpty()
                || BuiltInRegistries.ITEM.getTags().noneMatch(p -> p.getFirst().equals(ModTags.SANDWICH_BREAD))
        ) {
            return translate("base", "sandwich");
        }

        int breadCount = countItems(contents, ModTags.SANDWICH_BREAD);
        int bunCount = countItems(contents, ModTags.BURGER_BUNS);

        String sandwichType = bunCount > (breadCount - bunCount) ? "burger" : "sandwich";

        // full bread sandwich
        if (contents.size() == breadCount) {
            return getBreadSandwichName(contents, sandwichType);
        }

        List<ItemStack> uniqueIngredients = getUniqueIngredientsExcludingBread(contents);

        List<ItemStack> potions = uniqueIngredients.stream().filter(item -> item.is(Items.POTION)).toList();
        // potion sandwich
        if (potions.size() == 1) {
            if (FMLEnvironment.dist.isDedicatedServer()) {
                return translate("base", "sandwich");
            }
            return getPotionSandwichName(potions.getFirst(), sandwichType);
        }

        boolean isOpenFacedSandwich = breadCount == 1 && contents.size() > 1;

        if (!uniqueIngredients.isEmpty() && uniqueIngredients.size() <= 3) {
            Component ingredientList = listIngredients(uniqueIngredients);
            if (contents.isDoubleDecker()) {
                return translateWithArg("double_decker", "with_ingredients", sandwichType, ingredientList);
            } else if (isOpenFacedSandwich) {
                return translateWithArg("open_faced", "with_ingredients", sandwichType, ingredientList);
            } else {
                return translateWithArg("base", "with_ingredients", sandwichType, ingredientList);
            }
        }

        if (contents.isDoubleDecker()) {
            return translate("double_decker", sandwichType);
        } else if (isOpenFacedSandwich) {
            return translate("open_faced", sandwichType);
        } else {
            return translate("base", sandwichType);
        }
    }

    private static List<ItemStack> getUniqueIngredientsExcludingBread(SandwichContents sandwich) {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack ingredient : sandwich) {
            if (!ingredient.is(ModTags.SANDWICH_BREAD) && result.stream().noneMatch(stack -> ItemStack.matches(ingredient, stack))) {
                result.add(ingredient);
            }
        }
        return result;
    }

    private static int countItems(SandwichContents sandwich, TagKey<Item> tagKey) {
        int result = 0;
        for (ItemStack ingredient : sandwich) {
            if (ingredient.is(tagKey)) {
                result++;
            }
        }
        return result;
    }

    private static Component getBreadSandwichName(SandwichContents contents, String sandwichType) {
        if ((contents.size() == 3)
                && contents.getFirst().getItem() != ModItems.TOASTED_BREAD_SLICE.get()
                && contents.get(1).getItem() == ModItems.TOASTED_BREAD_SLICE.get()
                && contents.get(2).getItem() != ModItems.TOASTED_BREAD_SLICE.get()) {
            return translateWithArg("base", "with_ingredients", sandwichType, Ingredients.getDisplayName(contents.get(1)));
        }
        return translate("only_bread", sandwichType);
    }

    private static Component listIngredients(List<ItemStack> ingredients) {
        List<Component> ingredientNames = ingredients.stream().map(Ingredients::getDisplayName).toList();
        return SomeAssemblyRequired.translate("tooltip.ingredient_list.%s".formatted(ingredientNames.size()), ingredientNames.toArray());
    }

    private static Component translateWithArg(Object... keys) {
        return Component.translatable(translationKey(Arrays.copyOfRange(keys, 0, keys.length - 1)), keys[keys.length - 1]);
    }

    private static Component translate(Object... keys) {
        return Component.translatable(translationKey(keys));
    }

    private static String translationKey(Object... keys) {
        StringBuilder builder = new StringBuilder("item.%s.sandwich".formatted(SomeAssemblyRequired.MOD_ID));
        for (Object key : keys) {
            builder.append('.');
            builder.append(key);
        }
        return builder.toString();
    }

    private static Component getPotionSandwichName(ItemStack potionItem, String sandwichType) {
        Optional<Holder<Potion>> potion = potionItem
                .getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                .potion();

        if (potion.isPresent()) {
            ResourceLocation id = Objects.requireNonNull(BuiltInRegistries.POTION.getKey(potion.get().value()));
            String key = translationKey("potion", "with_id", id.getNamespace(), id.getPath(), sandwichType);
            if (I18n.exists(key)) {
                return Component.translatable(key);
            }
        }

        String potionKey = potion.map(potionHolder -> PotionContents.createItemStack(Items.POTION, potionHolder))
                .orElse(new ItemStack(Items.POTION))
                .getDescriptionId();

        int i = 1;
        do {
            Pattern pattern = Pattern.compile(I18n.get(translationKey("potion", "pattern", i)));
            String replacement = I18n.get(translationKey("potion", "pattern", i, sandwichType));
            Matcher matcher = pattern.matcher(I18n.get(potionKey));

            if (matcher.matches()) {
                return Component.literal(matcher.replaceAll(replacement));
            }

            i += 1;
        } while (I18n.exists(translationKey("potion", "pattern", i)));

        return translateWithArg("base", "with_ingredients", sandwichType, listIngredients(List.of(potionItem)));
    }
}
