package someassemblyrequired.common.item.spreadtype;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class PotionSpreadType extends SpreadType {

    public PotionSpreadType() {
        super(Items.POTION);
    }

    @Override
    public Item getContainer(ItemStack stack) {
        return Items.GLASS_BOTTLE;
    }

    @Override
    public int getColor(ItemStack stack) {
        return PotionUtils.getColor(stack);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (PotionUtils.getPotionFromItem(stack) == Potions.WATER) {
            return new TranslationTextComponent("spreadtype.someassemblyrequired.water_bottle");
        }
        return stack.getDisplayName();
    }
}
