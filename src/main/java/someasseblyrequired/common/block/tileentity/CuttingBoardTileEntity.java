package someasseblyrequired.common.block.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import someasseblyrequired.common.init.RecipeTypes;
import someasseblyrequired.common.init.TileEntityTypes;
import someasseblyrequired.common.recipe.CuttingRecipe;

import javax.annotation.Nullable;

public class CuttingBoardTileEntity extends TileEntity {

    private final ItemStackHandler inventory = createItemHandler();
    private final LazyOptional<IItemHandler> ingredientHandler = LazyOptional.of(() -> inventory);

    public CuttingBoardTileEntity() {
        super(TileEntityTypes.CUTTING_BOARD);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && (side == null || side == Direction.DOWN)) {
            return ingredientHandler.cast();
        }
        return super.getCapability(capability, side);
    }

    public ItemStack getIngredient() {
        return inventory.getStackInSlot(0);
    }

    public boolean addIngredient(ItemStack stack) {
        if (!stack.isEmpty() && getIngredient().isEmpty()) {
            inventory.insertItem(0, stack.split(1), false);
            return true;
        }
        return false;
    }

    public boolean hasIngredient() {
        return !getIngredient().isEmpty();
    }

    public ItemStack removeIngredient() {
        return inventory.extractItem(0, 1, false);
    }

    public ItemStack cutIngredient() {
        if (world != null) {
            CuttingRecipe recipe = world.getRecipeManager().getRecipe(RecipeTypes.CUTTING, new RecipeWrapper(inventory), world).orElse(null);
            if (recipe != null) {
                inventory.extractItem(0, 1, false);
                return recipe.getRecipeOutput().copy();
            }
        }
        return ItemStack.EMPTY;
    }

    private void onContentsChanged() {
        if (world == null || world.isRemote) {
            return;
        }
        BlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 2);
        markDirty();
    }

    @Override
    public void read(BlockState state, CompoundNBT compoundNBT) {
        inventory.deserializeNBT(compoundNBT.getCompound("Ingredients"));
        super.read(state, compoundNBT);
    }

    @Override
    public CompoundNBT write(CompoundNBT compoundNBT) {
        compoundNBT.put("Ingredients", inventory.serializeNBT());
        return super.write(compoundNBT);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(super.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        read(state, tag);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), -1, write(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SUpdateTileEntityPacket packet) {
        if (world != null) {
            read(world.getBlockState(pos), packet.getNbtCompound());
        }
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(1) {

            @Override
            protected void onContentsChanged(int slot) {
                CuttingBoardTileEntity.this.onContentsChanged();
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }
}
