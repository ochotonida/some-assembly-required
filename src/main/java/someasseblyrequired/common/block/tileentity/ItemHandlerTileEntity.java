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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class ItemHandlerTileEntity extends TileEntity {

    private final ItemStackHandler inventory;
    private final LazyOptional<ItemStackHandler> itemHandler;

    public ItemHandlerTileEntity(TileEntityType<? extends ItemHandlerTileEntity> tileEntityType) {
        this(tileEntityType, 0);
    }

    public ItemHandlerTileEntity(TileEntityType<? extends ItemHandlerTileEntity> tileEntityType, int size) {
        this(tileEntityType, size, true);
    }

    public ItemHandlerTileEntity(TileEntityType<? extends ItemHandlerTileEntity> tileEntityType, int size, boolean canExtract) {
        super(tileEntityType);
        inventory = createItemHandler(size, canExtract);
        itemHandler = LazyOptional.of(() -> inventory);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    protected int getAmountOfItems() {
        int size;
        for (size = 0; size < inventory.getSlots() && !inventory.getStackInSlot(size).isEmpty(); size++) ;
        return size;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
        }
        return super.getCapability(capability, side);
    }

    protected void onContentsChanged() {
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

    protected TileEntityItemHandler createItemHandler(int size, boolean canExtract) {
        return new TileEntityItemHandler(size, canExtract);
    }

    protected class TileEntityItemHandler extends ItemStackHandler {

        private final boolean canExtract;

        protected TileEntityItemHandler(int size, boolean canExtract) {
            super(size);
            this.canExtract = canExtract;
        }

        @Override
        protected void onContentsChanged(int slot) {
            ItemHandlerTileEntity.this.onContentsChanged();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return canExtract;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return canExtract ? super.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
    }
}
