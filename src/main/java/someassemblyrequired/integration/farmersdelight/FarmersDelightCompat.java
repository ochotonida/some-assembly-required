package someassemblyrequired.integration.farmersdelight;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.common.ingredient.CustomIngredientModels;
import someassemblyrequired.common.item.sandwich.SandwichBuilder;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Arrays;

public class FarmersDelightCompat {

    public static void setup() {

    }

    public static void addCustomIngredientModels() {
        CustomIngredientModels.itemsWithCustomModel.addAll(Arrays.asList(
                ModItems.BACON.get(),
                ModItems.BEEF_PATTY.get(),
                ModItems.COOKED_BACON.get(),
                ModItems.FRIED_EGG.get(),
                ModItems.MIXED_SALAD.get()
        ));
    }

    public static void addSandwichSubtypes(NonNullList<ItemStack> items) {
        items.add(blt());
    }

    public static ItemStack blt() {
        return SandwichBuilder.builder()
                .add(ModItems.COOKED_BACON.get())
                .add(ModItems.CABBAGE_LEAF.get())
                .add(someassemblyrequired.common.init.ModItems.TOMATO_SLICES.get())
                .build();
    }

    public static boolean canAddToSandwich(ItemStack item) {
        return item.getItem() instanceof ConsumableItem;
    }

    public static void onFoodEaten(ItemStack stack, LivingEntity entity) {
        if (stack.getItem() instanceof ConsumableItem item && !entity.getLevel().isClientSide()) {
            item.affectConsumer(stack, entity.getLevel(), entity);
        }
    }
}
