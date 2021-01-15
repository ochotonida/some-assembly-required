package someassemblyrequired.common.block.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import someassemblyrequired.common.block.CuttingBoardBlock;
import someassemblyrequired.common.init.RecipeTypes;
import someassemblyrequired.common.init.TileEntityTypes;
import someassemblyrequired.common.recipe.CuttingRecipe;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CuttingBoardTileEntity extends ItemHandlerTileEntity {

    private boolean hasKnife;

    public CuttingBoardTileEntity() {
        // noinspection unchecked
        super((TileEntityType<CuttingBoardTileEntity>) TileEntityTypes.CUTTING_BOARD.get(), 1);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        // for items, only allow inserting/extracting from the bottom
        // return super.getCapability for any other capability
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

    public boolean hasKnife() {
        return hasKnife;
    }

    public ItemStack removeIngredient() {
        return getInventory().extractItem(0, 1, false);
    }

    public List<ItemStack> cutIngredient(ItemStack tool) {
        if (world != null) {
            CuttingRecipe recipe = world.getRecipeManager().getRecipe(RecipeTypes.CUTTING, new RecipeWrapper(getInventory()), world).orElse(null);
            if (recipe != null && recipe.getTool().test(tool)) {
                getInventory().extractItem(0, 1, false);
                return recipe.getRecipeOutputs();
            }
        }
        return Collections.emptyList();
    }

    @Override
    protected void onContentsUpdated() {
        hasKnife = CuttingBoardBlock.isKnife(getIngredient());
    }

    @Override
    protected TileEntityItemHandler createItemHandler(int size) {
        return new IngredientHandler(size);
    }

    private class IngredientHandler extends TileEntityItemHandler {

        protected IngredientHandler(int size) {
            super(size);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack result = super.extractItem(slot, amount, simulate);
            result.removeChildTag("IsOnSandwich");
            return result;
        }

        @Override
        public NonNullList<ItemStack> removeItems() {
            NonNullList<ItemStack> result = NonNullList.create();
            for (ItemStack ingredient : super.removeItems()) {
                ingredient.removeChildTag("IsOnSandwich");
                result.add(ingredient);
            }
            return result;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            ItemStack ingredient = stack.copy();
            ingredient.getOrCreateTag().putBoolean("IsOnSandwich", true);
            return super.insertItem(slot, ingredient, simulate);
        }
    }
}
