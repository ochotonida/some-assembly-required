package someassemblyrequired.ingredient.behavior;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class HoneyBottleBehavior implements IngredientBehavior {

    @Override
    public void onEaten(ItemStack item, LivingEntity entity) {
        if (!entity.getLevel().isClientSide) {
            entity.removeEffect(MobEffects.POISON);
        }
    }
}
