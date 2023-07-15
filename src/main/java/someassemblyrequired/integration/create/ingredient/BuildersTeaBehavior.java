package someassemblyrequired.integration.create.ingredient;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.ingredient.behavior.IngredientBehavior;

public class BuildersTeaBehavior implements IngredientBehavior {

    @Override
    public void onEaten(ItemStack ingredient, LivingEntity entity) {
        if (!entity.level().isClientSide()) {
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 3600, 0, false, false, false));
        }
    }
}
