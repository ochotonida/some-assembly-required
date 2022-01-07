package someassemblyrequired.common.ingredient.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface IngredientBehavior {

    void onEaten(ItemStack ingredient, LivingEntity player);
}
