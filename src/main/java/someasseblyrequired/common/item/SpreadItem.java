package someasseblyrequired.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import someasseblyrequired.common.init.SpreadTypes;
import someasseblyrequired.common.item.spreadtype.SpreadType;

public class SpreadItem extends TooltippedItem {

    public SpreadItem(Properties properties) {
        super(properties);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        ItemStack ingredient = ItemStack.read(stack.getOrCreateChildTag("Ingredient"));
        if (!ingredient.isEmpty()) {
            SpreadType spreadType = SpreadTypes.findSpreadType(ingredient.getItem());
            if (spreadType != null) {
                ITextComponent name = spreadType.getDisplayName(ingredient);
                if (name != null) {
                    return name;
                }
            }
            return ingredient.getDisplayName();
        }
        return super.getDisplayName(stack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entity) {
        super.onItemUseFinish(stack, world, entity);

        ItemStack ingredient = ItemStack.read(stack.getOrCreateChildTag("Ingredient"));
        if (!ingredient.isEmpty()) {
            SpreadType spreadType = SpreadTypes.findSpreadType(ingredient.getItem());
            if (spreadType != null) {
                Item containerItem = spreadType.getContainer(stack);
                ItemStack result = ingredient.onItemUseFinish(world, entity);
                if (!result.isEmpty() && result.getItem() != containerItem) {
                    return result;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("HasEffect");
    }
}
