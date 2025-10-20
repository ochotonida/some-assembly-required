package someassemblyrequired.ingredient;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import someassemblyrequired.registry.ModFoods;

import java.util.Optional;

public interface IngredientPropertiesBase {

    Optional<FoodProperties> food();

    default FoodProperties getFood(ItemStack stack, @Nullable LivingEntity entity) {
        if (food().isPresent()) {
            return food().get();
        }
        FoodProperties p = stack.getFoodProperties(entity);
        if (p != null) {
            if (p.usingConvertsTo().isEmpty() && stack.hasCraftingRemainingItem()) {
                ItemStack remainder = stack.getCraftingRemainingItem().copy();
                remainder.setCount(1);
                return new FoodProperties(p.nutrition(), p.saturation(), p.canAlwaysEat(),
                        p.eatSeconds(), Optional.of(remainder), p.effects());
            }
            return p;
        }
        return ModFoods.EMPTY;
    }

    default Component getDisplayName(ItemStack stack) {
        return displayName().orElse(getFullName(stack));
    }

    Optional<Component> displayName();

    default Component getFullName(ItemStack stack) {
        return fullName().orElse(stack.getHoverName());
    }

    Optional<Component> fullName();

    ItemStack displayItem();

    default ItemStack getDisplayItem(ItemStack stack) {
        return displayItem().isEmpty() ? stack : displayItem();
    }

    boolean renderAsItem();

    default boolean hidden() {
        return false;
    }

    default int height() {
        return 1;
    }

    Holder<SoundEvent> sound();

    default void playSound(Level level, Player player, BlockPos pos, float pitch) {
        // TODO allow custom resource pack sounds
        level.playSound(player, pos, sound().value(), SoundSource.BLOCKS, 1, pitch);
    }
}
