package someassemblyrequired.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
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
 * A block entity that contains items which should be synced to clients with a max stack size of 1
 */
public abstract class ItemHandlerBlockEntity extends BlockEntity {

    private final BlockEntityItemHandler inventory;
    private final LazyOptional<BlockEntityItemHandler> itemHandler;

    public ItemHandlerBlockEntity(BlockEntityType<? extends ItemHandlerBlockEntity> blockEntityType, BlockPos pos, BlockState state, int size) {
        super(blockEntityType, pos, state);
        inventory = createItemHandler(size);
        itemHandler = LazyOptional.of(() -> inventory);
    }

    protected BlockEntityItemHandler getInventory() {
        return inventory;
    }

    /**
     * @return all non-empty items contained by this block entity
     */
    public NonNullList<ItemStack> getItems() {
        return inventory.getItems();
    }

    /**
     * Removes all items contained by this block entity
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
     * Called when the contents of the block entity are loaded or changed
     */
    protected void onContentsUpdated() {
    }

    private void onContentsChanged() {
        if (level != null && !level.isClientSide) {
            BlockState state = level.getBlockState(worldPosition);
            // cause a block update to sync the change to clients
            level.sendBlockUpdated(worldPosition, state, state, 2);
            // make sure the block entity gets saved to disk
            setChanged();
        }
        onContentsUpdated();
    }

    private void onContentsLoaded() {
        onContentsUpdated();
    }

    @Override
    public void load(CompoundTag compoundNBT) {
        inventory.deserializeNBT(compoundNBT.getCompound("Ingredients"));
        super.load(compoundNBT);
    }

    @Override
    public void saveAdditional(CompoundTag compoundNBT) {
        compoundNBT.put("Ingredients", inventory.serializeNBT());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    protected BlockEntityItemHandler createItemHandler(int size) {
        return new BlockEntityItemHandler(size);
    }

    /**
     * Whether the items in this item handler can be modified using {@link BlockEntityItemHandler::extractItem(int, int, boolean)} or {@link BlockEntityItemHandler::insertItem(int, ItemStack, boolean)}
     */
    protected boolean canModifyItems() {
        return false;
    }

    protected class BlockEntityItemHandler extends ItemStackHandler {

        protected BlockEntityItemHandler(int size) {
            super(size);
        }

        /**
         * @return all non-empty items in this item handler
         */
        public NonNullList<ItemStack> getItems() {
            NonNullList<ItemStack> result = NonNullList.create();
            for (int slot = 0; slot < getSlots(); slot++) {
                ItemStack stack = getStackInSlot(slot);
                if (!stack.isEmpty()) {
                    result.add(stack);
                }
            }
            return result;
        }

        /**
         * Removes all items contained by this block entity
         *
         * @return the subset of the removed items that should be given back to the player in some way
         */
        public NonNullList<ItemStack> removeItems() {
            NonNullList<ItemStack> result = getItems();
            for (int slot = 0; slot < getSlots(); slot++) {
                setStackInSlot(slot, ItemStack.EMPTY);
            }
            return result;
        }

        @Override
        protected void onContentsChanged(int slot) {
            ItemHandlerBlockEntity.this.onContentsChanged();
        }

        @Override
        protected void onLoad() {
            ItemHandlerBlockEntity.this.onContentsLoaded();
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
