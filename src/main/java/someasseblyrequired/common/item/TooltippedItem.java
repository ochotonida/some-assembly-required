package someasseblyrequired.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class TooltippedItem extends Item {

    private final boolean hasEffect;

    public TooltippedItem(Properties properties) {
        this(properties, false);
    }

    public TooltippedItem(Properties properties, boolean hasEffect) {
        super(properties);
        this.hasEffect = hasEffect;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".tooltip").mergeStyle(TextFormatting.GRAY));
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return hasEffect || super.hasEffect(stack);
    }
}
