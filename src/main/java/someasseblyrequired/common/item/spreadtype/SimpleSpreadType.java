package someasseblyrequired.common.item.spreadtype;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SimpleSpreadType extends SpreadType {

    private final Item container;
    private final int color;

    public SimpleSpreadType(Item ingredient, Item container, int color) {
        super(ingredient);
        this.container = container;
        this.color = color;
    }

    public SimpleSpreadType(Item ingredient, int color) {
        this(ingredient, Items.AIR, color);
    }

    public Item getContainer(ItemStack stack) {
        return container;
    }

    public int getColor(ItemStack stack) {
        return color;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        // noinspection ConstantConditions
        return new TranslationTextComponent(String.format("spreadtype.%s.%s", getRegistryName().getNamespace(), getRegistryName().getPath()));
    }
}
