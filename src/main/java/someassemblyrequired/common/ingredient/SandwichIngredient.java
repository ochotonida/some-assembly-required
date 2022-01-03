package someassemblyrequired.common.ingredient;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface SandwichIngredient {

    default FoodProperties getFood(ItemStack item) {
        return item.getItem().getFoodProperties();
    }

    /**
     * Called server-side when a player eats a sandwich with this ingredient
     */
    default void onEaten(ItemStack item, Player player) {

    }

    /**
     * Returns the name of the ingredient
     */
    default Component getDisplayName(ItemStack item) {
        return item.getHoverName();
    }

    /**
     * Returns the item to render instead of the specified item when rendering a sandwich
     */
    default ItemStack getDisplayItem(ItemStack item) {
        return item;
    }

    /**
     * Returns the container this item is contained in.
     * The container will be returned to the player when the item is applied to the sandwich.
     * Items with a container will be discarded when removed from a sandwich.
     */
    default ItemStack getContainer(ItemStack item) {
        return ItemStack.EMPTY;
    }

    default void playApplySound(ItemStack item, Level level, @Nullable Player player, BlockPos pos) {
        if (getContainer(item).isEmpty()) {
            level.playSound(player, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.3F, 1.3F);
        } else {
            level.playSound(player, pos, SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS, 0.3F, 1.3F);
        }
    }

    default void playRemoveSound(ItemStack item, Level level, @Nullable Player player, BlockPos pos) {
        if (getContainer(item).isEmpty()) {
            level.playSound(player, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.3F, 1.6F);
        } else {
            level.playSound(player, pos, SoundEvents.HONEY_BLOCK_BREAK, SoundSource.BLOCKS, 0.3F, 1.6F);
        }
    }
}
