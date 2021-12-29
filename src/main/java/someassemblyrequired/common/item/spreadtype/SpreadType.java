package someassemblyrequired.common.item.spreadtype;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class SpreadType extends ForgeRegistryEntry<SpreadType> {

    private final Item ingredient;

    public SpreadType(Item ingredient) {
        this.ingredient = ingredient;
    }

    @Nonnull
    public abstract Item getContainer(ItemStack stack);

    public abstract int getColor(ItemStack stack);

    @Nonnull
    public final Item getIngredient() {
        return ingredient;
    }

    public final boolean hasContainer(ItemStack stack) {
        return getContainer(stack) != Items.AIR;
    }

    @Nonnull
    public abstract Component getDisplayName(ItemStack stack);
}
