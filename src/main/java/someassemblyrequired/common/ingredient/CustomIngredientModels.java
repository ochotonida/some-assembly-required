package someassemblyrequired.common.ingredient;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.util.Util;
import someassemblyrequired.integration.ModCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomIngredientModels {

    public static final List<Item> itemsWithCustomModel = new ArrayList<>();

    static {
        itemsWithCustomModel.addAll(Arrays.asList(
                ModItems.APPLE_SLICES.get(),
                ModItems.GOLDEN_APPLE_SLICES.get(),
                ModItems.CHOPPED_CARROT.get(),
                ModItems.CHOPPED_GOLDEN_CARROT.get(),
                ModItems.CHOPPED_BEETROOT.get(),
                ModItems.SLICED_TOASTED_CRIMSON_FUNGUS.get(),
                ModItems.SLICED_TOASTED_WARPED_FUNGUS.get(),
                ModItems.TOMATO_SLICES.get()
        ));

        ModCompat.addCustomIngredientModels();
    }

    public static void registerItemModelProperties() {
        for (Item item : itemsWithCustomModel) {
            // noinspection ConstantConditions
            ResourceLocation id = Util.id("%s/%s".formatted(item.getRegistryName().getNamespace(), item.getRegistryName().getPath()));
            ItemProperties.register(ModItems.SPREAD.get(), id,
                    (stack, level, entity, seed) -> stack.getTag() != null && stack.getTag().getString("Item").equals(item.getRegistryName().toString()) ? 1 : 0
            );
        }
    }

    private static ResourceLocation getModelLocation(Item item) {
        return item.getRegistryName();
    }
}
