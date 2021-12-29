package someassemblyrequired.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import someassemblyrequired.common.item.spreadtype.SpreadType;
import someassemblyrequired.common.item.spreadtype.SpreadTypeManager;

public class SpreadItem extends Item {

    public SpreadItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        ItemStack ingredient = ItemStack.of(stack.getOrCreateTagElement("Ingredient"));
        if (!ingredient.isEmpty()) {
            SpreadType spreadType = SpreadTypeManager.INSTANCE.getSpreadType(ingredient.getItem());
            if (spreadType != null) {
                return spreadType.getDisplayName(ingredient);
            }
            return ingredient.getHoverName();
        }
        return super.getName(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        super.finishUsingItem(stack, world, entity);

        ItemStack ingredient = ItemStack.of(stack.getOrCreateTagElement("Ingredient"));
        if (!ingredient.isEmpty()) {
            SpreadType spreadType = SpreadTypeManager.INSTANCE.getSpreadType(ingredient.getItem());
            if (spreadType != null) {
                Item containerItem = spreadType.getContainer(stack);
                ItemStack result = ingredient.finishUsingItem(world, entity);
                if (!result.isEmpty() && result.getItem() != containerItem) {
                    return result;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return super.isFoil(stack) || stack.getOrCreateTag().getBoolean("HasEffect");
    }
}
