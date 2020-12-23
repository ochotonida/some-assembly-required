package someasseblyrequired.common.block.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import someasseblyrequired.common.init.RecipeTypes;
import someasseblyrequired.common.init.TileEntityTypes;
import someasseblyrequired.common.recipe.CuttingRecipe;

import javax.annotation.Nullable;

public class CuttingBoardTileEntity extends ItemHandlerTileEntity {

    public CuttingBoardTileEntity() {
        super(TileEntityTypes.CUTTING_BOARD, 1);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (side == null || side == Direction.DOWN || capability != CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return super.getCapability(capability, side);
        }
        return LazyOptional.empty();
    }

    public ItemStack getIngredient() {
        return getInventory().getStackInSlot(0);
    }

    public boolean addIngredient(ItemStack stack) {
        if (!stack.isEmpty() && getIngredient().isEmpty()) {
            getInventory().insertItem(0, stack.split(1), false);
            return true;
        }
        return false;
    }

    public boolean hasIngredient() {
        return !getIngredient().isEmpty();
    }

    public ItemStack removeIngredient() {
        return getInventory().extractItem(0, 1, false);
    }

    public ItemStack cutIngredient() {
        if (world != null) {
            CuttingRecipe recipe = world.getRecipeManager().getRecipe(RecipeTypes.CUTTING, new RecipeWrapper(getInventory()), world).orElse(null);
            if (recipe != null) {
                getInventory().extractItem(0, 1, false);
                return recipe.getRecipeOutput().copy();
            }
        }
        return ItemStack.EMPTY;
    }
}
