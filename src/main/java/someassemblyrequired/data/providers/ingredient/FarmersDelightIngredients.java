package someassemblyrequired.data.providers.ingredient;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import someassemblyrequired.data.providers.Ingredients;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Arrays;
import java.util.List;

public class FarmersDelightIngredients {

    public static final List<Item> MODEL_OVERRIDES = Arrays.asList(
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
            ModItems.GRILLED_SALMON.get(),
            ModItems.STUFFED_PUMPKIN.get(),
            ModItems.SHEPHERDS_PIE.get(),
            ModItems.HONEY_GLAZED_HAM.get(),
            ModItems.ROAST_CHICKEN.get(),
            ModItems.HAMBURGER.get(),
            ModItems.GLOW_BERRY_CUSTARD.get(),
            ModItems.GLEAMING_SALAD.get()
    );

    @SuppressWarnings("unchecked")
    public static void addIngredients(Ingredients ingredients) {
        ingredients.builder(ModItems.TOMATO_SAUCE.get()).setSpread(0xbe331f).setMoistSound();
        ingredients.builder(ModItems.MILK_BOTTLE.get()).setFullName(BuiltInRegistries.ITEM.wrapAsHolder(Items.MILK_BUCKET)).setSpread(0xEEFDFF).setMoistSound();
        ingredients.builder(ModItems.HOT_COCOA.get()).setSpread(0x7b4835).setMoistSound();
        ingredients.builder(ModItems.APPLE_CIDER.get()).setSpread(0xbd783d, 0.8).setMoistSound();
        ingredients.builder(ModItems.MELON_JUICE.get()).setSpread(0xc73225, 0.8).setMoistSound();
        ingredients.builder(ModItems.ONION_SOUP.get()).setSpread(0xb87345).setMoistSound();

        ingredients.builder(ModItems.MINCED_BEEF.get()).setSlimySound().customModel().setHeight(2);
        ingredients.builder(ModItems.CHICKEN_CUTS.get()).setCustomDisplayName().setWetSound();
        ingredients.builder(ModItems.BACON.get()).setCustomDisplayName();
        ingredients.builder(ModItems.MUTTON_CHOPS.get()).setMoistSound().setCustomDisplayName();
        ingredients.builder(ModItems.COD_SLICE.get()).setCustomDisplayName().setWetSound();
        ingredients.builder(ModItems.SALMON_SLICE.get()).setCustomDisplayName().setWetSound();

        ingredients.builder(ModItems.CABBAGE_LEAF.get()).setCustomDisplayName().setLeafySound();
        ingredients.builder(ModItems.FRIED_EGG.get()).setCustomDisplayName().setSlimySound();
        ingredients.builder(ModItems.BEEF_PATTY.get()).setCustomDisplayName().customModel().setHeight(2);
        ingredients.builder(ModItems.COOKED_CHICKEN_CUTS.get()).setDisplayName((Holder<Item>) ModItems.CHICKEN_CUTS);
        ingredients.builder(ModItems.COOKED_BACON.get()).setDisplayName((Holder<Item>) ModItems.BACON);
        ingredients.builder(ModItems.COOKED_COD_SLICE.get()).setDisplayName((Holder<Item>) ModItems.COD_SLICE);
        ingredients.builder(ModItems.COOKED_SALMON_SLICE.get()).setDisplayName((Holder<Item>) ModItems.SALMON_SLICE);
        ingredients.builder(ModItems.COOKED_MUTTON_CHOPS.get()).setDisplayName((Holder<Item>) ModItems.MUTTON_CHOPS);
        ingredients.builder(ModItems.HAMBURGER.get()).customModel().setHeight(7);

        ingredients.builder(ModItems.COOKED_RICE.get()).setCustomDisplayName();

        ingredients.builder(ModItems.MIXED_SALAD.get()).setLeafySound();
        ingredients.builder(ModItems.FRUIT_SALAD.get()).setWetSound();
        ingredients.builder(ModItems.NETHER_SALAD.get());
        ingredients.builder(ModItems.BEEF_STEW.get()).setMoistSound();
        ingredients.builder(ModItems.CHICKEN_SOUP.get()).setMoistSound();
        ingredients.builder(ModItems.VEGETABLE_SOUP.get()).setMoistSound();
        ingredients.builder(ModItems.FISH_STEW.get()).setMoistSound();
        ingredients.builder(ModItems.FRIED_RICE.get());
        ingredients.builder(ModItems.PUMPKIN_SOUP.get()).setMoistSound();
        ingredients.builder(ModItems.BAKED_COD_STEW.get()).setSlimySound();
        ingredients.builder(ModItems.NOODLE_SOUP.get()).setSlimySound();
        ingredients.builder(ModItems.BACON_AND_EGGS.get()).setWetSound();
        ingredients.builder(ModItems.PASTA_WITH_MEATBALLS.get()).setSlimySound();
        ingredients.builder(ModItems.PASTA_WITH_MUTTON_CHOP.get()).setSlimySound();
        ingredients.builder(ModItems.ROASTED_MUTTON_CHOPS.get()).setWetSound();
        ingredients.builder(ModItems.VEGETABLE_NOODLES.get()).setSlimySound();
        ingredients.builder(ModItems.STEAK_AND_POTATOES.get()).setWetSound();
        ingredients.builder(ModItems.RATATOUILLE.get()).setWetSound();
        ingredients.builder(ModItems.SQUID_INK_PASTA.get()).setSlimySound();
        ingredients.builder(ModItems.GRILLED_SALMON.get()).setWetSound();
        ingredients.builder(ModItems.GLOW_BERRY_CUSTARD.get()).setSlimySound();
        ingredients.builder(ModItems.ROAST_CHICKEN.get()).setWetSound().setFullName(getTranslationKey(ModItems.ROAST_CHICKEN_BLOCK.get()));
        ingredients.builder(ModItems.STUFFED_PUMPKIN.get()).setSlimySound().setFullName(getTranslationKey(ModItems.STUFFED_PUMPKIN_BLOCK.get()));
        ingredients.builder(ModItems.HONEY_GLAZED_HAM.get()).setSlimySound().setFullName(getTranslationKey(ModItems.HONEY_GLAZED_HAM_BLOCK.get()));
        ingredients.builder(ModItems.SHEPHERDS_PIE.get()).setMoistSound().setFullName(getTranslationKey(ModItems.SHEPHERDS_PIE_BLOCK.get()));
        ingredients.builder(ModItems.GLEAMING_SALAD.get()).setLeafySound().setFullName(getTranslationKey(ModItems.GLEAMING_SALAD_BLOCK.get()));
    }

    private static Component getTranslationKey(Item item) {
        return item.getName(new ItemStack(item));
    }
}
