package someassemblyrequired.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SpreadItem extends Item {

    public SpreadItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return super.isFoil(stack) || stack.getOrCreateTag().getBoolean("HasEffect");
    }
}
