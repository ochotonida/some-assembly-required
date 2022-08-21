package someassemblyrequired.data.ingredient;

import net.minecraft.world.item.Item;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.data.Ingredients;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Arrays;
import java.util.List;

public class FarmersDelightIngredients {

    public static final List<Item> itemsWithCustomModel = Arrays.asList(
            ModItems.FRIED_EGG.get(),
            ModItems.CABBAGE_LEAF.get(),
            ModItems.MINCED_BEEF.get(),
            ModItems.BEEF_PATTY.get(),
            ModItems.CHICKEN_CUTS.get(),
            ModItems.COOKED_CHICKEN_CUTS.get(),
            ModItems.MUTTON_CHOPS.get(),
            ModItems.COOKED_MUTTON_CHOPS.get(),
            ModItems.BACON.get(),
            ModItems.COOKED_BACON.get(),
            ModItems.COD_SLICE.get(),
            ModItems.COOKED_COD_SLICE.get(),
            ModItems.SALMON_SLICE.get(),
            ModItems.COOKED_SALMON_SLICE.get(),
            ModItems.FRUIT_SALAD.get(),
            ModItems.MIXED_SALAD.get(),
            ModItems.NETHER_SALAD.get(),
            ModItems.COOKED_RICE.get(),
            ModItems.BEEF_STEW.get(),
            ModItems.CHICKEN_SOUP.get(),
            ModItems.VEGETABLE_SOUP.get(),
            ModItems.FISH_STEW.get(),
            ModItems.FRIED_RICE.get(),
            ModItems.PUMPKIN_SOUP.get(),
            ModItems.BAKED_COD_STEW.get(),
            ModItems.NOODLE_SOUP.get(),
            ModItems.BACON_AND_EGGS.get(),
            ModItems.PASTA_WITH_MEATBALLS.get(),
            ModItems.PASTA_WITH_MUTTON_CHOP.get(),
            ModItems.ROASTED_MUTTON_CHOPS.get(),
            ModItems.VEGETABLE_NOODLES.get(),
            ModItems.STEAK_AND_POTATOES.get(),
            ModItems.RATATOUILLE.get(),
            ModItems.SQUID_INK_PASTA.get(),
            ModItems.GRILLED_SALMON.get()
    );

    public static void addIngredients(Ingredients ingredients) {
        ingredients.builder(ModItems.TOMATO_SAUCE.get()).setBowled().setSpread(0xbe331f).setSpreadSound();
        ingredients.builder(ModItems.MILK_BOTTLE.get()).setFullName("ingredient.%s.milk_bucket".formatted(SomeAssemblyRequired.MODID)).setBottled().setSpread(0xEEFDFF).setSpreadSound();
        ingredients.builder(ModItems.HOT_COCOA.get()).setBottled().setSpread(0x7b4835).setSpreadSound();
        ingredients.builder(ModItems.APPLE_CIDER.get()).setBottled().setSpread(0xbd783d).setSpreadSound();
        ingredients.builder(ModItems.MELON_JUICE.get()).setBottled().setSpread(0xc73225).setSpreadSound();

        ingredients.builder(ModItems.MINCED_BEEF.get()).setSpreadSound();
        ingredients.builder(ModItems.CHICKEN_CUTS.get()).setSpreadSound().setCustomDisplayName();
        ingredients.builder(ModItems.BACON.get()).setCustomDisplayName();
        ingredients.builder(ModItems.MUTTON_CHOPS.get()).setSpreadSound().setCustomDisplayName();
        ingredients.builder(ModItems.COD_SLICE.get()).setSpreadSound().setCustomDisplayName();
        ingredients.builder(ModItems.SALMON_SLICE.get()).setSpreadSound().setCustomDisplayName();

        ingredients.builder(ModItems.CABBAGE_LEAF.get()).setCustomDisplayName().setLeavesSound();
        ingredients.builder(ModItems.FRIED_EGG.get()).setCustomDisplayName().setSpreadSound();
        ingredients.builder(ModItems.BEEF_PATTY.get()).setCustomDisplayName();
        ingredients.builder(ModItems.COOKED_CHICKEN_CUTS.get()).setDisplayName(ModItems.CHICKEN_CUTS.get());
        ingredients.builder(ModItems.COOKED_BACON.get()).setDisplayName(ModItems.BACON.get());
        ingredients.builder(ModItems.COOKED_COD_SLICE.get()).setDisplayName(ModItems.COD_SLICE.get());
        ingredients.builder(ModItems.COOKED_SALMON_SLICE.get()).setDisplayName(ModItems.SALMON_SLICE.get());
        ingredients.builder(ModItems.COOKED_MUTTON_CHOPS.get()).setDisplayName(ModItems.MUTTON_CHOPS.get());

        ingredients.builder(ModItems.COOKED_RICE.get()).setBowled().setCustomDisplayName();

        ingredients.builder(ModItems.MIXED_SALAD.get()).setBowled().setLeavesSound();
        ingredients.builder(ModItems.FRUIT_SALAD.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.NETHER_SALAD.get()).setBowled();
        ingredients.builder(ModItems.BEEF_STEW.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.CHICKEN_SOUP.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.VEGETABLE_SOUP.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.FISH_STEW.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.FRIED_RICE.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.PUMPKIN_SOUP.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.BAKED_COD_STEW.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.NOODLE_SOUP.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.BACON_AND_EGGS.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.PASTA_WITH_MEATBALLS.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.PASTA_WITH_MUTTON_CHOP.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.ROASTED_MUTTON_CHOPS.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.VEGETABLE_NOODLES.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.STEAK_AND_POTATOES.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.RATATOUILLE.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.SQUID_INK_PASTA.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.GRILLED_SALMON.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.ROAST_CHICKEN.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.STUFFED_PUMPKIN.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.HONEY_GLAZED_HAM.get()).setBowled().setSpreadSound();
        ingredients.builder(ModItems.SHEPHERDS_PIE.get()).setBowled().setSpreadSound();
    }
}
