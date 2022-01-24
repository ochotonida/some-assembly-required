package someassemblyrequired.integration.farmersdelight;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.common.item.sandwich.SandwichItem;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.registry.ModItems;

public class FarmersDelightCompat {

    public static void setup() {

    }

    public static void addSandwichSubtypes(NonNullList<ItemStack> items) {
        items.add(blt());
    }

    public static ItemStack blt() {
        return SandwichItem.makeSandwich(
                ModItems.COOKED_BACON.get(),
                ModItems.CABBAGE_LEAF.get(),
                someassemblyrequired.common.init.ModItems.TOMATO_SLICES.get()
        );
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
