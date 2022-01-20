package someassemblyrequired.data.ingredient;

import net.minecraft.world.item.Item;
import someassemblyrequired.SomeAssemblyRequired;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public record FarmersDelightIngredients(Map<Item, IngredientBuilder> ingredients) {

    public static final List<Item> itemsWithCustomModel = Arrays.asList(
            ModItems.FRIED_EGG.get(),
            ModItems.CABBAGE_LEAF.get(),
            ModItems.BEEF_PATTY.get(),
            ModItems.BACON.get(),
            ModItems.COOKED_BACON.get(),
            ModItems.FRUIT_SALAD.get(),
            ModItems.MIXED_SALAD.get(),
            ModItems.NETHER_SALAD.get(),
            ModItems.COOKED_RICE.get(),
            ModItems.BEEF_STEW.get(),
            ModItems.CHICKEN_SOUP.get()
    );

    public void addIngredients() {
        builder(ModItems.TOMATO_SAUCE.get()).setBowled().setSpread(0xbe331f).setSpreadSound();

        builder(ModItems.MILK_BOTTLE.get()).setFullName("ingredient.%s.milk_bucket".formatted(SomeAssemblyRequired.MODID)).setBottled().setSpread(0xEEFDFF).setSpreadSound();
        builder(ModItems.HOT_COCOA.get()).setBottled().setSpread(0x7b4835).setSpreadSound();
        builder(ModItems.APPLE_CIDER.get()).setBottled().setSpread(0xbd783d).setSpreadSound();
        builder(ModItems.MELON_JUICE.get()).setBottled().setSpread(0xc73225).setSpreadSound();

        builder(ModItems.CABBAGE_LEAF.get()).setCustomDisplayName().setLeavesSound();
        builder(ModItems.FRIED_EGG.get()).setCustomDisplayName().setSpreadSound();
        builder(ModItems.BEEF_PATTY.get()).setCustomDisplayName();
        builder(ModItems.COOKED_CHICKEN_CUTS.get()).setCustomDisplayName();
        builder(ModItems.COOKED_BACON.get()).setCustomDisplayName();
        builder(ModItems.COOKED_COD_SLICE.get()).setCustomDisplayName();
        builder(ModItems.COOKED_SALMON_SLICE.get()).setCustomDisplayName();
        builder(ModItems.COOKED_MUTTON_CHOPS.get()).setCustomDisplayName();

        builder(ModItems.COOKED_RICE.get()).setBowled().setCustomDisplayName();

        builder(ModItems.MIXED_SALAD.get()).setBowled().setLeavesSound();
        builder(ModItems.FRUIT_SALAD.get()).setBowled().setSpreadSound();
        builder(ModItems.NETHER_SALAD.get()).setBowled();
        builder(ModItems.BEEF_STEW.get()).setBowled().setSpreadSound();
        builder(ModItems.CHICKEN_SOUP.get()).setBowled().setSpreadSound();
        builder(ModItems.VEGETABLE_SOUP.get()).setBowled().setSpreadSound();
        builder(ModItems.FISH_STEW.get()).setBowled().setSpreadSound();
        builder(ModItems.FRIED_RICE.get()).setBowled().setSpreadSound();
        builder(ModItems.PUMPKIN_SOUP.get()).setBowled().setSpreadSound();
        builder(ModItems.BAKED_COD_STEW.get()).setBowled().setSpreadSound();
        builder(ModItems.NOODLE_SOUP.get()).setBowled().setSpreadSound();
        builder(ModItems.BACON_AND_EGGS.get()).setBowled().setSpreadSound();
        builder(ModItems.PASTA_WITH_MEATBALLS.get()).setBowled().setSpreadSound();
        builder(ModItems.PASTA_WITH_MUTTON_CHOP.get()).setBowled().setSpreadSound();
        builder(ModItems.ROASTED_MUTTON_CHOPS.get()).setBowled().setSpreadSound();
        builder(ModItems.VEGETABLE_NOODLES.get()).setBowled().setSpreadSound();
        builder(ModItems.STEAK_AND_POTATOES.get()).setBowled().setSpreadSound();
        builder(ModItems.RATATOUILLE.get()).setBowled().setSpreadSound();
        builder(ModItems.SQUID_INK_PASTA.get()).setBowled().setSpreadSound();
        builder(ModItems.GRILLED_SALMON.get()).setBowled().setSpreadSound();
        builder(ModItems.ROAST_CHICKEN.get()).setBowled().setSpreadSound();
        builder(ModItems.STUFFED_PUMPKIN.get()).setBowled().setSpreadSound();
        builder(ModItems.HONEY_GLAZED_HAM.get()).setBowled().setSpreadSound();
        builder(ModItems.SHEPHERDS_PIE.get()).setBowled().setSpreadSound();
    }

    private IngredientBuilder builder(Item item) {
        if (ingredients.containsKey(item)) {
            return ingredients.get(item);
        }
        IngredientBuilder builder = new IngredientBuilder(item);
        ingredients.put(item, builder);
        return builder;
    }
}
