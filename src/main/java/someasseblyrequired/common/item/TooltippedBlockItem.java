package someasseblyrequired.common.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class TooltippedBlockItem extends BlockItem {

    @Nullable
    private final String tooltipTranslationKey;

    public TooltippedBlockItem(Block block, Properties properties) {
        super(block, properties);
        tooltipTranslationKey = null;
    }

    public TooltippedBlockItem(Block block, Properties properties, String tooltipTranslationKey) {
        super(block, properties);
        this.tooltipTranslationKey = tooltipTranslationKey;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        if (tooltipTranslationKey == null) {
            tooltip.add(new TranslationTextComponent(getTranslationKey() + ".tooltip").mergeStyle(TextFormatting.GRAY));
        } else {
            // noinspection ConstantConditions
            tooltip.add(new TranslationTextComponent("block." + getRegistryName().getNamespace() + "." + tooltipTranslationKey + ".tooltip").mergeStyle(TextFormatting.GRAY));
        }
    }
}
