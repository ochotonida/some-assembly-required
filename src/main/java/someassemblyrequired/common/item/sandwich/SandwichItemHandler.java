package someassemblyrequired.common.item.sandwich;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import someassemblyrequired.common.config.ModConfig;
import someassemblyrequired.common.ingredient.Ingredients;
import someassemblyrequired.common.init.ModFoods;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;
import someassemblyrequired.mixin.FoodPropertiesMixin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class SandwichItemHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<ListTag>, Iterable<ItemStack> {

    protected final List<ItemStack> items;
    protected FoodProperties foodProperties;

    public SandwichItemHandler() {
        this.items = new ArrayList<>();
        this.foodProperties = ModFoods.EMPTY;
    }

    public static Optional<SandwichItemHandler> get(@Nullable ICapabilityProvider capabilityProvider) {
        if (capabilityProvider == null) {
            return Optional.empty();
        }
        return capabilityProvider.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .filter(handler -> handler instanceof SandwichItemHandler)
                .map(handler -> ((SandwichItemHandler) handler));
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public FoodProperties getFoodProperties() {
        return foodProperties;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int size() {
        return items.size();
    }

    public int getTotalNutrition() {
        int result = 0;
        for (ItemStack stack : items) {
            result += Ingredients.getFood(stack, null).getNutrition();
        }
        return result;
    }

    public float getAverageSaturation() {
        float totalSaturation = 0;
        for (ItemStack stack : items) {
            FoodProperties food = Ingredients.getFood(stack, null);
            totalSaturation += food.getSaturationModifier() * food.getNutrition();
        }
        return totalSaturation / getTotalNutrition();
    }

    private void updateFoodProperties() {
        if (isEmpty()) {
            foodProperties = ModFoods.EMPTY;
        } else {
            FoodProperties.Builder builder = new FoodProperties.Builder()
                    .nutrition(getTotalNutrition())
                    .saturationMod(getAverageSaturation());

            for (ItemStack item : items) {
                FoodProperties food = Ingredients.getFood(item, null);

                if (food.canAlwaysEat()) {
                    builder.alwaysEat();
                }

                for (Pair<Supplier<MobEffectInstance>, Float> pair : ((FoodPropertiesMixin) food).getEffectSuppliers()) {
                    builder.effect(pair.getFirst(), pair.getSecond());
                }
            }

            foodProperties = builder.build();
        }
    }

    public void add(ItemStack stack) {
        if (stack.isEmpty()) {
            throw new IllegalArgumentException();
        }
        items.add(stack);
        onContentsChanged();
    }

    public boolean canAdd(SandwichItemHandler sandwich) {
        return this.size() + sandwich.size() <= ModConfig.server.maximumSandwichHeight.get();
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

    public boolean hasTopAndBottomBread() {
        return size() > 0 && bottom().is(ModTags.SANDWICH_BREAD) && top().is(ModTags.SANDWICH_BREAD);
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
            if (items.get(i).is(ModTags.SANDWICH_BREAD)) {
                if (foundBread) {
                    return false;
                }
                foundBread = true;
            }
        }

        return foundBread && !items.get(1).is(ModTags.SANDWICH_BREAD) && !items.get(size() - 2).is(ModTags.SANDWICH_BREAD);
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
        return serializerItems(items);
    }

    public static ListTag serializerItems(List<ItemStack> items) {
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
        updateFoodProperties();
    }

    protected void onContentsChanged() {
        updateFoodProperties();
    }
}
