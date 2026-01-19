package someassemblyrequired.data.providers.ingredient;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.data.providers.Ingredients;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.registry.ModItems;

import java.util.List;

public class CreateFoodIngredients {

    public static final List<Holder<Item>> MODEL_OVERRIDES = List.of(
            Ingredients.reference(ModCompat.CREATE_FOOD, "pumpernickel_bread_slice")
    );

    public static void addIngredients(Ingredients ingredients) {
        ingredients.builder(Ingredients.reference(ModCompat.CREATE_FOOD, "bread_slice")).setDisplayItem(new ItemStack(ModItems.BREAD_SLICE));
        ingredients.builder(Ingredients.reference(ModCompat.CREATE_FOOD, "toast_slice")).setDisplayItem(new ItemStack(ModItems.TOASTED_BREAD_SLICE));
    }
}
