package someassemblyrequired.common.item.sandwich;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import someassemblyrequired.common.ingredient.Ingredients;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class SandwichItemHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<ListTag>, Iterable<ItemStack> {

    protected List<ItemStack> items;

    public SandwichItemHandler() {
        this(new ArrayList<>());
    }

    public SandwichItemHandler(ItemStack... items) {
        this(Arrays.asList(items));
    }

    public SandwichItemHandler(List<ItemStack> items) {
        for (ItemStack stack : items) {
            if (stack.isEmpty() || stack.getCount() != 1) {
                throw new IllegalArgumentException();
            }
        }
        this.items = new ArrayList<>(items);
    }

    public static Optional<SandwichItemHandler> get(@Nullable ICapabilityProvider capabilityProvider) {
        if (capabilityProvider == null) {
            return Optional.empty();
        }
        return capabilityProvider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                .filter(handler -> handler instanceof SandwichItemHandler)
                .map(handler -> ((SandwichItemHandler) handler));
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int size() {
        return items.size();
    }

    public void add(ItemStack stack) {
        if (stack.isEmpty()) {
            throw new IllegalArgumentException();
        }
        items.add(stack);
        onContentsChanged();
    }

    private static int getMaxSize() {
        return 16; // TODO
    }

    public boolean canAdd(SandwichItemHandler sandwich) {
        return this.size() + sandwich.size() <= getMaxSize();
    }

    public void add(SandwichItemHandler sandwich) {
        items.addAll(sandwich.items);
        onContentsChanged();
    }

    public void pop() {
        items.remove(items.size() - 1);
        onContentsChanged();
    }

    public ItemStack bottom() {
        return items.get(0);
    }

    public ItemStack top() {
        return items.get(items.size() - 1);
    }

    public boolean isValidSandwich() {
        boolean allowOpenFacedSandwiches = false; // TODO add config option
        return size() > 0 && bottom().is(ModTags.BREAD_SLICES) && (allowOpenFacedSandwiches || top().is(ModTags.BREAD_SLICES));
    }

    public boolean hasTopAndBottomBread() {
        return size() > 0 && bottom().is(ModTags.BREAD_SLICES) && bottom().is(ModTags.BREAD_SLICES);
    }

    public boolean isDoubleDeckerSandwich() {
        if (size() < 5) {
            return false;
        }
        if (!hasTopAndBottomBread()) {
            return false;
        }

        boolean foundBread = false;
        for (int i = 1; i < size() - 2; i++) {
            if (items.get(i).is(ModTags.BREAD_SLICES)) {
                if (foundBread) {
                    return false;
                }
                foundBread = true;
            }
        }

        return foundBread && !items.get(1).is(ModTags.BREAD_SLICES) && !items.get(size() - 2).is(ModTags.BREAD_SLICES);
    }

    public boolean isBLT() {
        if (!hasTopAndBottomBread()) {
            return false;
        }

        boolean hasBacon = false;
        boolean hasLettuce = false;
        boolean hasTomato = false;

        for (ItemStack stack : items) {
            if (stack.is(ModTags.COOKED_BACON)) {
                hasBacon = true;
            } else if (stack.is(ModTags.SALAD_INGREDIENTS)) {
                hasLettuce = true;
            } else if (stack.is(ModTags.VEGETABLES_TOMATO)) {
                hasTomato = true;
            } else if (!stack.is(ModTags.BREAD_SLICES)) {
                return false;
            }
        }

        return hasBacon && hasLettuce && hasTomato;
    }

    public boolean canAlwaysEat() {
        for (ItemStack item : items) {
            FoodProperties food = Ingredients.getFood(item);
            if (food != null && food.canAlwaysEat()) {
                return true;
            }
        }
        return false;
    }

    public ItemStack getAsItem() {
        ItemStack result = new ItemStack(ModItems.SANDWICH.get());
        result.getOrCreateTagElement("BlockEntityTag").put("Sandwich", serializeNBT());
        return result;
    }

    @Override
    public int getSlots() {
        return items.size();
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (stack.isEmpty()) {
            throw new IllegalArgumentException();
        }
        validateSlotIndex(slot);
        items.set(slot, stack);
        onContentsChanged();
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return items.get(slot);
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
        for (ItemStack stack : items) {
            result.add(stack.save(new CompoundTag()));
        }
        return result;
    }

    @Override
    public void deserializeNBT(ListTag listTag) {
        items.clear();
        for (int i = 0; i < listTag.size(); i++) {
            items.add(ItemStack.of(listTag.getCompound(i)));
        }
        onLoad();
    }

    @Override
    public Iterator<ItemStack> iterator() {
        return items.iterator();
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= items.size()) {
            throw new IndexOutOfBoundsException("Slot " + slot + " not in valid range - [0," + items.size() + ")");
        }
    }

    protected void onLoad() {

    }

    protected void onContentsChanged() {

    }
}
