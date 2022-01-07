package someassemblyrequired.common.ingredient.behavior;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SuspiciousStewBehavior implements IngredientBehavior {

    @Override
    public void onEaten(ItemStack item, LivingEntity entity) {
        CompoundTag tag = item.getTag();
        if (tag != null && tag.contains("Effects", Tag.TAG_LIST)) {
            ListTag list = tag.getList("Effects", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); ++i) {
                int duration = 160;
                CompoundTag effectTag = list.getCompound(i);
                if (effectTag.contains("EffectDuration", Tag.TAG_INT)) {
                    duration = effectTag.getInt("EffectDuration");
                }
                MobEffect effect = MobEffect.byId(effectTag.getByte("EffectId"));
                if (effect != null) {
                    entity.addEffect(new MobEffectInstance(effect, duration));
                }
            }
        }
    }
}
