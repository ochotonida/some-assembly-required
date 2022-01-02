package someassemblyrequired.common.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SandwichItemHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<ListTag> {

    protected List<ItemStack> stacks;

    public SandwichItemHandler() {
        this(new ArrayList<>());
    }

    public SandwichItemHandler(List<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            if (stack.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        this.stacks = new ArrayList<>(stacks);
    }

    protected static boolean isBread(ItemStack stack) {
        return stack.is(ModTags.SANDWICH_BREADS);
    }

    public boolean isEmpty() {
        return stacks.isEmpty();
    }

    public int size() {
        return stacks.size();
    }

    public void push(ItemStack stack) {
        if (stack.isEmpty()) {
            throw new IllegalArgumentException();
        }
        stacks.add(stack);
    }

    public void pop() {
        stacks.remove(stacks.size() - 1);
    }

    public ItemStack bottom() {
        return stacks.get(0);
    }

    public ItemStack top() {
        return stacks.get(stacks.size() - 1);
    }

    public boolean isValidSandwich() {
        boolean allowOpenFacedSandwiches = false; // TODO add config option
        return isBread(bottom()) && (allowOpenFacedSandwiches || isBread(top()));
    }

    public boolean hasTopAndBottomBread() {
        return isBread(bottom()) && isBread(top());
    }

    public boolean isDoubleDeckerSandwich() {
        if (size() < 5) {
            return false;
        }
        if (!hasTopAndBottomBread()) {
            return false;
        }

        boolean foundBread = false;
        for (int i = 1; i < size() - 1; i++) {
            if (isBread(stacks.get(i))) {
                if (foundBread) {
                    return false;
                }
                foundBread = true;
            }
        }

        return !ModTags.SANDWICH_BREADS.contains(stacks.get(1).getItem())
                && !ModTags.SANDWICH_BREADS.contains(stacks.get(size() - 2).getItem());
    }

    public boolean isBLT() {
        if (!hasTopAndBottomBread()) {
            return false;
        }

        boolean hasBacon = false;
        boolean hasLettuce = false;
        boolean hasTomato = false;

        for (int i = 1; i < size() - 1; i++) {
            ItemStack stack = stacks.get(i);

            if (ModTags.COOKED_BACON.contains(stack.getItem())) {
                hasBacon = true;
            } else if (ModTags.CROPS_LETTUCE.contains(stack.getItem())) {
                hasLettuce = true;
            } else if (stack.getItem() == ModItems.TOMATO_SLICES.get() || ModTags.CROPS_TOMATOES.contains(stack.getItem())) {
                hasTomato = true;
            } else {
                return false;
            }
        }

        return hasBacon && hasLettuce && hasTomato;
    }

    @Override
    public int getSlots() {
        return stacks.size();
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (stack.isEmpty()) {
            throw new IllegalArgumentException();
        }
        validateSlotIndex(slot);
        stacks.set(slot, stack);
        onContentsChanged();
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return stacks.get(slot);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return stack;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public ListTag serializeNBT() {
        ListTag result = new ListTag();
        for (ItemStack stack : stacks) {
            result.add(stack.save(new CompoundTag()));
        }
        return result;
    }

    @Override
    public void deserializeNBT(ListTag listTag) {
        stacks.clear();
        for (int i = 0; i < listTag.size(); i++) {
            stacks.add(ItemStack.of(listTag.getCompound(i)));
        }
        onLoad();
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= stacks.size()) {
            throw new IndexOutOfBoundsException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
        }
    }

    protected void onLoad() {

    }

    protected void onContentsChanged() {

    }
}
