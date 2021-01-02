package someasseblyrequired.common.block.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * A tile entity that contains items which should be synced to clients with a max stack size of 1
 */
public class ItemHandlerTileEntity extends TileEntity {

    private final TileEntityItemHandler inventory;
    private final LazyOptional<TileEntityItemHandler> itemHandler;
    private final boolean canModifyItems;

    public ItemHandlerTileEntity(TileEntityType<? extends ItemHandlerTileEntity> tileEntityType, int size) {
        this(tileEntityType, size, true);
    }

    public ItemHandlerTileEntity(TileEntityType<? extends ItemHandlerTileEntity> tileEntityType, int size, boolean canModifyItems) {
        super(tileEntityType);
        this.canModifyItems = canModifyItems;
        inventory = createItemHandler(size);
        itemHandler = LazyOptional.of(() -> inventory);
    }

    protected TileEntityItemHandler getInventory() {
        return inventory;
    }

    /**
     * @return all non-empty items contained by this tile entity
     */
    public NonNullList<ItemStack> getItems() {
        return inventory.getItems();
    }

    /**
     * Removes all items contained by this tile entity
     *
     * @return the subset of the removed items that should be given back to the player in some way
     */
    public NonNullList<ItemStack> removeItems() {
        return inventory.removeItems();
    }

    public int getAmountOfItems() {
        int result = 0;
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                result++;
            }
        }
        return result;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
        }
        return super.getCapability(capability, side);
    }

    /**
     * Called when the contents of the tile entity are loaded or changed
     */
    protected void onContentsUpdated() {
    }

    private void onContentsChanged() {
        if (world != null && !world.isRemote) {
            BlockState state = world.getBlockState(pos);
            // cause a block update to sync the change to clients
            world.notifyBlockUpdate(pos, state, state, 2);
            // make sure the tile entity gets saved to disk
            markDirty();
        }
        onContentsUpdated();
    }

    private void onContentsLoaded() {
        onContentsUpdated();
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

    // sync on chunk load
    @Override
    public CompoundNBT getUpdateTag() {
        return write(super.getUpdateTag());
    }

    // sync on chunk load
    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        read(state, tag);
    }

    // sync on block update
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), -1, write(new CompoundNBT()));
    }

    // sync on block update
    @Override
    public void onDataPacket(NetworkManager networkManager, SUpdateTileEntityPacket packet) {
        if (world != null) {
            read(world.getBlockState(pos), packet.getNbtCompound());
        }
    }

    protected TileEntityItemHandler createItemHandler(int size) {
        return new TileEntityItemHandler(size);
    }

    /**
     * Whether the items in this item handler can be modified using {@link TileEntityItemHandler::extractItem(int, int, boolean)} or {@link TileEntityItemHandler::insertItem(int, ItemStack, boolean)}
     */
    protected boolean canModifyItems() {
        return canModifyItems;
    }

    protected class TileEntityItemHandler extends ItemStackHandler {

        protected TileEntityItemHandler(int size) {
            super(size);
        }

        /**
         * @return all non-empty items in this item handler
         */
        public NonNullList<ItemStack> getItems() {
            NonNullList<ItemStack> result = NonNullList.create();
            for (int slot = 0; slot < inventory.getSlots(); slot++) {
                ItemStack stack = inventory.getStackInSlot(slot);
                if (!stack.isEmpty()) {
                    result.add(stack);
                }
            }
            return result;
        }

        /**
         * Removes all items contained by this tile entity
         *
         * @return the subset of the removed items that should be given back to the player in some way
         */
        public NonNullList<ItemStack> removeItems() {
            NonNullList<ItemStack> result = getItems();
            for (int slot = 0; slot < inventory.getSlots(); slot++) {
                setStackInSlot(slot, ItemStack.EMPTY);
            }
            return result;
        }

        @Override
        protected void onContentsChanged(int slot) {
            ItemHandlerTileEntity.this.onContentsChanged();
        }

        @Override
        protected void onLoad() {
            ItemHandlerTileEntity.this.onContentsLoaded();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return canModifyItems();
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return canModifyItems() ? super.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
    }
}
