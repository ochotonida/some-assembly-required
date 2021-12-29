package someassemblyrequired.common.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class DrinkableBottleItem extends Item {

    private final SoundEvent drinkSound;

    public DrinkableBottleItem(Item.Properties properties) {
        this(properties, SoundEvents.GENERIC_DRINK);
    }

    public DrinkableBottleItem(Item.Properties properties, SoundEvent drinkSound) {
        super(properties);
        this.drinkSound = drinkSound;
    }

    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        super.finishUsingItem(stack, world, entity);

        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if (entity instanceof Player && !((Player) entity).abilities.instabuild) {
                ItemStack itemstack = new ItemStack(Items.GLASS_BOTTLE);
                Player playerentity = (Player) entity;
                if (!playerentity.inventory.add(itemstack)) {
                    playerentity.drop(itemstack, false);
                }
            }

            return stack;
        }
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    public SoundEvent getDrinkingSound() {
        return drinkSound;
    }

    public SoundEvent getEatingSound() {
        return drinkSound;
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        return ItemUtils.useDrink(world, player, hand);
    }
}
