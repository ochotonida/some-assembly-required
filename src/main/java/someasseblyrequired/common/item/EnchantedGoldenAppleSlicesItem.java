package someasseblyrequired.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EnchantedGoldenAppleSlicesItem extends Item {

    public EnchantedGoldenAppleSlicesItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}
