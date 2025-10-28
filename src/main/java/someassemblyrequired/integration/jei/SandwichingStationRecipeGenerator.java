package someassemblyrequired.integration.jei;

import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

public class SandwichingStationRecipeGenerator extends SandwichRecipeGenerator<SandwichingStationCategory.Recipe> {

    @Override
    protected int getMaxToppings() {
        return 5;
    }

    @Override
    protected Optional<ItemStack> getFillingFromFluid(FluidStack fluid) {
        return Optional.empty();
    }

    @Override
    protected Optional<FluidIngredient> getFluidFromFilling(ItemStack filling) {
        return Optional.empty();
    }

    @Override
    protected SandwichingStationCategory.Recipe getRecipeForSandwich(ItemStack prefix, List<ItemStack> toppings, ItemStack result) {
        return new SandwichingStationCategory.Recipe(prefix, toppings, result);
    }
}