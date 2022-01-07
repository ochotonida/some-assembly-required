package someassemblyrequired.common.ingredient.behavior;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;

public class PotionBehavior implements IngredientBehavior {

    @Override
    public void onEaten(ItemStack item, LivingEntity entity) {
        for (MobEffectInstance mobEffect : PotionUtils.getMobEffects(item)) {
            if (mobEffect.getEffect().isInstantenous()) {
                mobEffect.getEffect().applyInstantenousEffect(entity, entity, entity, mobEffect.getAmplifier(), 1);
            } else {
                entity.addEffect(new MobEffectInstance(mobEffect));
            }
        }
    }
}
