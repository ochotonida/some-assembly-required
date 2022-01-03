package someassemblyrequired.common.block.redstonetoaster;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import someassemblyrequired.common.init.ModItems;

import java.util.Iterator;

public class RedstoneToasterItemHandler extends ItemStackHandler implements Iterable<ItemStack> {

    private final RedstoneToasterBlockEntity toaster;

    protected RedstoneToasterItemHandler(RedstoneToasterBlockEntity toaster) {
        super(2);
        this.toaster = toaster;
    }

    public void clear() {
        stacks.clear();
        toaster.onContentsChanged();
    }

    @Override
    protected void onContentsChanged(int slot) {
        toaster.onContentsChanged();
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (stack.getItem() == ModItems.SANDWICH.get()) {
            return false;
        }
        return stack.getItem().isEdible()
                || !(stack.getItem() instanceof BlockItem)
                || RedstoneToasterBlockEntity.hasToastingResult(toaster.getLevel(), stack);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (toaster.isToasting()) {
            return stack;
        }
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (toaster.isToasting()) {
            return ItemStack.EMPTY;
        }
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public Iterator<ItemStack> iterator() {
        return stacks.iterator();
    }
}
