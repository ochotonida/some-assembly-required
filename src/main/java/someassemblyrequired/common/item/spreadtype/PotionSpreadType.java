package someassemblyrequired.common.item.spreadtype;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import someassemblyrequired.SomeAssemblyRequired;

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
    public Component getDisplayName(ItemStack stack) {
        if (PotionUtils.getPotion(stack) == Potions.WATER) {
            return new TranslatableComponent("spreadtype.%s.water_bottle".formatted(SomeAssemblyRequired.MODID));
        }
        return stack.getHoverName();
    }
}
