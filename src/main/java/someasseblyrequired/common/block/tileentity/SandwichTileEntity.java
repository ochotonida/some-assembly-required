package someasseblyrequired.common.block.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class SandwichTileEntity extends TileEntity {

    protected final LazyOptional<IItemHandler> ingredientHandler = LazyOptional.of(this::createIngredientHandler);

    public SandwichTileEntity(TileEntityType<? extends SandwichTileEntity> tileEntityType) {
        super(tileEntityType);
    }

    protected IItemHandler createIngredientHandler() {
        return new ItemStackHandler(16) {

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return false;
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }
        };
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return ingredientHandler.cast();
        }
        return super.getCapability(capability, side);
    }

    protected void sync() {
        if (world == null || world.isRemote) {
            return;
        }
        BlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 2);
        markDirty();
    }

    @Override
    public void read(BlockState state, CompoundNBT compoundNBT) {
        // noinspection unchecked
        ingredientHandler.ifPresent(handler -> ((INBTSerializable<CompoundNBT>) handler).deserializeNBT(compoundNBT.getCompound("Ingredients")));
        super.read(state, compoundNBT);
    }

    @Override
    public CompoundNBT write(CompoundNBT compoundNBT) {
        // noinspection unchecked
        ingredientHandler.ifPresent(handler -> compoundNBT.put("Ingredients", ((INBTSerializable<CompoundNBT>) handler).serializeNBT()));
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
}
