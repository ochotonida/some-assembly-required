package someassemblyrequired.integration.jei;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.init.ModItems;
import someassemblyrequired.init.ModTags;
import someassemblyrequired.item.sandwich.SandwichItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JEIUtil {

    public static final ItemStack BREAD_SLICE = new ItemStack(ModItems.BREAD_SLICE.get());
    public static final ItemStack BURGER_BUN_BOTTOM = new ItemStack(ModItems.BURGER_BUN_BOTTOM.get());
    public static final ItemStack BURGER_BUN_TOP = new ItemStack(ModItems.BURGER_BUN_TOP.get());
    private static final Map<Item, List<ItemStack>> SANDWICHES = new HashMap<>();
    private static final List<ItemStack> BURGERS = new ArrayList<>();
    public static final List<ItemStack> INGREDIENTS = new ArrayList<>();
    public static final List<ItemStack> POTIONS = ForgeRegistries.POTIONS.getValues().stream()
            .map(potion -> PotionUtils.setPotion(new ItemStack(Items.POTION), potion))
            .toList();

    public static List<ItemStack> getSandwichesForBread(ItemStack bread) {
        if (bread.is(ModItems.BURGER_BUN_BOTTOM.get()) || bread.is(ModItems.BURGER_BUN_TOP.get())) {
            return BURGERS;
        }

        if (!SANDWICHES.containsKey(bread.getItem())) {
            SANDWICHES.put(bread.getItem(), INGREDIENTS.stream()
                    .map(ingredient -> SandwichItem.of(
                            bread.copy(),
                            ingredient,
                            bread.copy()
                    ))
                    .collect(Collectors.toList())
            );
        }

        return SANDWICHES.get(bread.getItem());
    }

    public static void refresh() {
        INGREDIENTS.clear();
        SANDWICHES.clear();

        INGREDIENTS.addAll(ForgeRegistries.ITEMS.getValues()
                .stream()
                .filter(Ingredients::hasCustomIngredientProperties)
                .map(ItemStack::new)
                .filter(item -> !item.is(ModTags.SANDWICH_BREAD))
                .toList());

        for (ItemStack ingredient : INGREDIENTS) {
            BURGERS.add(SandwichItem.of(
                    BURGER_BUN_BOTTOM,
                    ingredient,
                    BURGER_BUN_TOP
            ));
        }
    }
}
