package someassemblyrequired.ingredient;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.Nullable;
import someassemblyrequired.init.ModItems;
import someassemblyrequired.init.ModSoundEvents;
import someassemblyrequired.util.Util;

public class PotionProperties extends IngredientProperties {

    private final ItemStack displayItem;

    public PotionProperties() {
        super(null, null, null, ItemStack.EMPTY, new ItemStack(Items.GLASS_BOTTLE), ModSoundEvents.ADD_ITEM_MOIST.get(), 1, true);
        displayItem = new ItemStack(ModItems.SPREAD.get());
        displayItem.getOrCreateTag();
    }

    @Nullable
    @Override
    public FoodProperties getFood(ItemStack item, @Nullable LivingEntity entity) {
        FoodProperties.Builder builder = new FoodProperties.Builder();

        for (MobEffectInstance mobEffect : PotionUtils.getMobEffects(item)) {
            builder.effect(() -> new MobEffectInstance(mobEffect), 1);
        }

        return builder.build();
    }

    @Override
    public ItemStack getDisplayItem(ItemStack item) {
        Potion potion = PotionUtils.getPotion(item);
        // noinspection ConstantConditions
        displayItem.getTag().putBoolean("HasEffect", potion != Potions.WATER);
        displayItem.getTag().putInt("Color", PotionUtils.getColor(potion));
        return displayItem;
    }

    @Override
    public Component getDisplayName(ItemStack item) {
        Potion potion = PotionUtils.getPotion(item);
        if (!item.hasCustomHoverName() && potion == Potions.WATER) {
            return Util.translate("ingredient.water_bottle");
        }
        return super.getDisplayName(item);
    }

    @Override
    public Component getFullName(ItemStack item) {
        if (PotionUtils.getPotion(item) == Potions.WATER) {
            return getDisplayName(item);
        }
        return super.getFullName(item);
    }
}
