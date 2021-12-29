package someassemblyrequired.common.block.tileentity;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * A tile entity that contains items which should be synced to clients with a max stack size of 1
 */
public class ItemHandlerTileEntity extends BlockEntity {

    private final TileEntityItemHandler inventory;
    private final LazyOptional<TileEntityItemHandler> itemHandler;
    private final boolean canModifyItems;

    public ItemHandlerTileEntity(BlockEntityType<? extends ItemHandlerTileEntity> tileEntityType, int size) {
        this(tileEntityType, size, true);
    }

    public ItemHandlerTileEntity(BlockEntityType<? extends ItemHandlerTileEntity> tileEntityType, int size, boolean canModifyItems) {
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
        if (level != null && !level.isClientSide) {
            BlockState state = level.getBlockState(worldPosition);
            // cause a block update to sync the change to clients
            level.sendBlockUpdated(worldPosition, state, state, 2);
            // make sure the tile entity gets saved to disk
            setChanged();
        }
        onContentsUpdated();
    }

    private void onContentsLoaded() {
        onContentsUpdated();
    }

    @Override
    public void load(BlockState state, CompoundTag compoundNBT) {
        inventory.deserializeNBT(compoundNBT.getCompound("Ingredients"));
        super.load(state, compoundNBT);
    }

    @Override
    public CompoundTag save(CompoundTag compoundNBT) {
        compoundNBT.put("Ingredients", inventory.serializeNBT());
        return super.save(compoundNBT);
    }

    // sync on chunk load
    @Override
    public CompoundTag getUpdateTag() {
        return save(super.getUpdateTag());
    }

    // sync on chunk load
    @Override
    public void handleUpdateTag(BlockState state, CompoundTag tag) {
        load(state, tag);
    }

    // sync on block update
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(getBlockPos(), -1, save(new CompoundTag()));
    }

    // sync on block update
    @Override
    public void onDataPacket(Connection networkManager, ClientboundBlockEntityDataPacket packet) {
        if (level != null) {
            load(level.getBlockState(worldPosition), packet.getTag());
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
