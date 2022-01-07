package someassemblyrequired.common.ingredient.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class MilkBucketBehavior implements IngredientBehavior {

    @Override
    public void onEaten(ItemStack item, LivingEntity entity) {
        if (!entity.getLevel().isClientSide) {
            entity.curePotionEffects(item);
        }
    }
}
