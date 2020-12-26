package someasseblyrequired.common.block.tileentity;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import someasseblyrequired.common.init.Items;
import someasseblyrequired.common.init.SpreadTypes;
import someasseblyrequired.common.init.Tags;
import someasseblyrequired.common.init.TileEntityTypes;
import someasseblyrequired.common.item.spreadtype.SpreadType;

import javax.annotation.Nullable;

public class SandwichAssemblyTableTileEntity extends ItemHandlerTileEntity {

    /**
     * An item handler that contains the table's contents as a sandwich when valid
     * Ingredients can still be inserted in slot 0
     */
    private final SandwichItemHandler sandwichInventory = createSandwichItemHandler();
    private final LazyOptional<SandwichItemHandler> sandwichItemHandler = LazyOptional.of(() -> sandwichInventory);

    public SandwichAssemblyTableTileEntity() {
        super(TileEntityTypes.SANDWICH_ASSEMBLY_TABLE, 16);
    }

    private static ItemStack createSpreadItem(SpreadType spreadType, ItemStack ingredient) {
        CompoundNBT spreadNBT = new CompoundNBT();
        spreadNBT.putInt("Color", spreadType.getColor(ingredient));
        spreadNBT.putBoolean("HasEffect", ingredient.hasEffect());
        spreadNBT.put("Ingredient", ingredient.write(new CompoundNBT()));
        ItemStack spread = new ItemStack(Items.SPREAD, ingredient.getCount());
        spread.setTag(spreadNBT);
        return spread;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        // completed sandwiches are extracted from the top or bottom
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != null && side.getAxis().isVertical()) {
            return sandwichItemHandler.cast();
        }
        return super.getCapability(capability, side);
    }

    public ItemStack removeTopIngredient() {
        if (getAmountOfItems() > 0) {
            return getInventory().extractItem(getAmountOfItems() - 1, 1, false);
        }
        return ItemStack.EMPTY;
    }

    public boolean hasBreadAsTopIngredient() {
        if (getAmountOfItems() > 0) {
            return Tags.BREADS.contains(getInventory().getStackInSlot(getAmountOfItems() - 1).getItem());
        }
        return false;
    }

    public int getInventorySize() {
        return getInventory().getSlots();
    }

    /**
     * Try to add an ingredient to the sandwich table, converting it into a spread when possible. Does not modify the specified itemStack
     *
     * @return whether the ingredient successfully got added
     */
    public boolean addIngredient(ItemStack stack) {
        SpreadType spreadType = SpreadTypes.findSpreadType(stack.getItem());
        ItemStack ingredient = spreadType == null ? stack : createSpreadItem(spreadType, stack);

        int nextEmptySlot = getAmountOfItems();
        if (nextEmptySlot >= getInventorySize() || !getInventory().isItemValid(nextEmptySlot, ingredient)) {
            return false;
        }
        int count = getInventory().insertItem(nextEmptySlot, ingredient, false).getCount();
        return count != ingredient.getCount();
    }

    @Override
    protected void onContentsUpdated() {
        // check whether the list of ingredients are valid as a sandwich and update the sandwich item handler
        sandwichInventory.update();
    }

    private SandwichItemHandler createSandwichItemHandler() {
        return new SandwichItemHandler();
    }

    @Override
    protected TileEntityItemHandler createItemHandler(int size) {
        return new IngredientHandler(size);
    }

    private class SandwichItemHandler implements IItemHandler {

        // contains the table's ingredients as a sandwich when valid
        private ItemStack sandwich = ItemStack.EMPTY;

        /**
         * Checks whether the ingredient handler contains a valid sandwich and updates the sandwich handler accordingly
         */
        public void update() {
            int nextEmptySlot = getAmountOfItems();

            // there must be at least 1 item and the top item must be bread
            if (nextEmptySlot < 2 || !Tags.BREADS.contains(getInventory().getStackInSlot(nextEmptySlot - 1).getItem())) {
                sandwich = ItemStack.EMPTY;
            } else {
                sandwich = new ItemStack(Items.SANDWICH);
                write(sandwich.getOrCreateChildTag("BlockEntityTag"));
            }
        }

        protected void validateSlotIndex(int slot) {
            if (slot != 0) {
                throw new RuntimeException("Slot " + slot + " not in valid range - [0,1)");
            }
        }

        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            validateSlotIndex(slot);
            return sandwich;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (sandwich.isEmpty() || amount <= 0) {
                return ItemStack.EMPTY;
            }
            validateSlotIndex(slot);

            ItemStack result = sandwich;

            if (!simulate) {
                removeItems();
            }

            return result;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            // check if the stack can be inserted in the regular item handler in the next available slot
            return slot == 0 && getInventory().isItemValid(getAmountOfItems(), stack);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            validateSlotIndex(slot);
            // insert the stack in the regular item handler in the next available slot
            return getInventory().insertItem(getAmountOfItems(), stack, simulate);
        }
    }

    private class IngredientHandler extends TileEntityItemHandler {

        private IngredientHandler(int size) {
            super(size);
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public NonNullList<ItemStack> removeItems() {
            NonNullList<ItemStack> result = NonNullList.create();
            for (ItemStack ingredient : super.removeItems()) {
                if (ingredient.getItem() != Items.SPREAD) {
                    result.add(ingredient);
                }
            }
            return result;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            validateSlotIndex(slot);
            if (slot < getSlots() - 1 && !getStackInSlot(slot + 1).isEmpty()) {
                return ItemStack.EMPTY;
            }
            ItemStack result = super.extractItem(slot, amount, simulate);
            // spreads cannot be obtained as items
            return result.getItem() == Items.SPREAD ? ItemStack.EMPTY : result;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (stack.getItem() == Items.SANDWICH) {
                return getStackInSlot(0).isEmpty(); // TODO: allow sandwiches to be stacked on top of one another
            }

            return (slot == 0 || !getStackInSlot(slot - 1).isEmpty()) // the previous slot must have an item
                    && slot < getSlots() && getStackInSlot(slot).isEmpty() // the slot must be empty
                    && (stack.isFood() || stack.getItem() == Items.SPREAD || SpreadTypes.hasSpreadType(stack.getItem())) // the item must be edible
                    && (slot > 0 || Tags.BREADS.contains(stack.getItem())); // the first item must be bread
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }

            if (!isItemValid(slot, stack)) {
                return stack;
            }

            validateSlotIndex(slot);

            // copy the sandwich's ingredients
            if (stack.getItem() == Items.SANDWICH) {
                stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(sandwichHandler -> {
                    for (int sandwichSlot = 0; sandwichSlot < sandwichHandler.getSlots(); sandwichSlot++) {
                        setStackInSlot(sandwichSlot, sandwichHandler.getStackInSlot(sandwichSlot));
                    }
                });
                return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - 1);
            }

            // convert ingredients to spreads when possible
            SpreadType spreadType = SpreadTypes.findSpreadType(stack.getItem());
            ItemStack ingredient = spreadType == null ? stack.copy() : createSpreadItem(spreadType, stack);

            // spawn the spread's container as an item
            if (!simulate && spreadType != null && SandwichAssemblyTableTileEntity.this.getWorld() != null && spreadType.hasContainer(ingredient)) {
                ItemEntity sandwichEntity = new ItemEntity(SandwichAssemblyTableTileEntity.this.getWorld(), SandwichAssemblyTableTileEntity.this.pos.getX() + 0.5, SandwichAssemblyTableTileEntity.this.pos.getY() + 1.2, SandwichAssemblyTableTileEntity.this.pos.getZ() + 0.5, new ItemStack(spreadType.getContainer(ingredient)));
                sandwichEntity.setDefaultPickupDelay();
                SandwichAssemblyTableTileEntity.this.getWorld().addEntity(sandwichEntity);
            }

            return ItemHandlerHelper.copyStackWithSize(stack, super.insertItem(slot, ingredient, simulate).getCount());
        }
    }
}
