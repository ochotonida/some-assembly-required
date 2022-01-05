package someassemblyrequired.common.ingredient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import someassemblyrequired.common.init.ModItems;

public class SuspiciousStewIngredient implements SandwichIngredient {

    private static final ItemStack CONTAINER = new ItemStack(Items.BOWL);
    private static final ItemStack DISPLAY_ITEM;

    static {
        DISPLAY_ITEM = new ItemStack(ModItems.SPREAD.get());
        DISPLAY_ITEM.getOrCreateTag().putInt("Color", 0x3f9E80);
    }

    @Override
    public ItemStack getDisplayItem(ItemStack item) {
        return DISPLAY_ITEM;
    }

    @Override
    public ItemStack getContainer(ItemStack item) {
        return CONTAINER;
    }

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
