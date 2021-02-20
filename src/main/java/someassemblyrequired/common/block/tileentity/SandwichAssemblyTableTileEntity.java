package someassemblyrequired.common.block.tileentity;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.EmptyHandler;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModRecipeTypes;
import someassemblyrequired.common.init.ModTags;
import someassemblyrequired.common.init.ModTileEntityTypes;
import someassemblyrequired.common.recipe.SpreadType;

import javax.annotation.Nullable;
import java.util.List;

public class SandwichAssemblyTableTileEntity extends ItemHandlerTileEntity {

    /**
     * An item handler that contains the table's contents as a sandwich when valid
     * Ingredients can still be inserted in slot 0
     */
    private final SandwichItemHandler sandwichInventory = createSandwichItemHandler();
    private final LazyOptional<SandwichItemHandler> sandwichItemHandler = LazyOptional.of(() -> sandwichInventory);

    public SandwichAssemblyTableTileEntity() {
        // noinspection unchecked
        super((TileEntityType<SandwichAssemblyTableTileEntity>) ModTileEntityTypes.SANDWICH_ASSEMBLY_TABLE.get(), 16);
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
            ItemStack topIngredient = getInventory().getStackInSlot(getAmountOfItems() - 1);
            ItemStack result = getInventory().extractItem(getAmountOfItems() - 1, 1, false);
            return result.isEmpty() ? topIngredient : result;
        }
        return ItemStack.EMPTY;
    }

    public boolean hasBreadAsTopIngredient() {
        if (getAmountOfItems() > 0) {
            return ModTags.isBread(getInventory().getStackInSlot(getAmountOfItems() - 1).getItem());
        }
        return false;
    }

    @Override
    public IngredientHandler getInventory() {
        return (IngredientHandler) super.getInventory();
    }

    public int getInventorySize() {
        return getInventory().getSlots();
    }

    /**
     * Try to add an ingredient to the sandwich table. Does not modify the specified itemStack
     *
     * @return whether the ingredient successfully got added
     */
    public boolean addIngredient(ItemStack stack) {
        ItemStack ingredient = stack.copy();

        int count = getInventory().insertItem(stack, false).getCount();
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
            if (nextEmptySlot < 2 || !ModTags.isBread(getInventory().getStackInSlot(nextEmptySlot - 1).getItem())) {
                sandwich = ItemStack.EMPTY;
            } else {
                sandwich = new ItemStack(ModItems.SANDWICH.get());
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
                ingredient.removeChildTag("IsOnSandwich");
                ingredient.removeChildTag("IsOnLoaf");
                result.add(ingredient);
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
            // spreads cannot be obtained as items when removed
            if (world != null && world.getRecipeManager().getRecipe(ModRecipeTypes.SPREAD_TYPE, new Inventory(result), world).isPresent()) {
                return ItemStack.EMPTY;
            }

            result.removeChildTag("IsOnSandwich");
            result.removeChildTag("IsOnLoaf");

            return result;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot != 0 && getStackInSlot(slot - 1).isEmpty() // the previous slot must have an item
                    || slot >= getSlots() || !getStackInSlot(slot).isEmpty()) { // the specified slot must be empty
                return false;
            }

            if (stack.getItem() == ModItems.SANDWICH.get()) {
                IItemHandler itemHandler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(EmptyHandler.INSTANCE);

                int sandwichSize;
                for (sandwichSize = 0; sandwichSize < itemHandler.getSlots() && !itemHandler.getStackInSlot(sandwichSize).isEmpty(); sandwichSize++)
                    ;

                return sandwichSize > 0 && sandwichSize <= getSlots() - getAmountOfItems();
            }

            return (stack.isFood() || world != null && ModRecipeTypes.getSpreadType(stack, world) != null) // the item must be edible
                    && (slot > 0 || ModTags.isBread(stack.getItem())); // the first item must be bread
        }

        public ItemStack insertItem(ItemStack stack, boolean spawnContainer) {
            int nextEmptySlot = getAmountOfItems();
            if (nextEmptySlot >= getSlots() || !isItemValid(nextEmptySlot, stack)) {
                return stack;
            }
            return insertItem(nextEmptySlot, stack, false, spawnContainer);
        }


        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return insertItem(slot, stack, simulate, true);
        }

        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate, boolean spawnContainer) {
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }

            if (!isItemValid(slot, stack)) {
                return stack;
            }

            validateSlotIndex(slot);

            // copy the sandwich's ingredients
            if (stack.getItem() == ModItems.SANDWICH.get()) {
                stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(sandwichItemHandler -> {
                    int slotOffset = getAmountOfItems();
                    for (int sandwichSlot = 0; sandwichSlot < sandwichItemHandler.getSlots() && !sandwichItemHandler.getStackInSlot(sandwichSlot).isEmpty(); sandwichSlot++) {
                        setStackInSlot(slotOffset + sandwichSlot, sandwichItemHandler.getStackInSlot(sandwichSlot).copy());
                    }
                });
                return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - 1);
            }

            ItemStack ingredient = stack.copy();

            CompoundNBT tag = ingredient.getOrCreateTag();
            tag.putBoolean("IsOnSandwich", true);

            if (!ModTags.isBread(ingredient.getItem())) {
                List<ItemStack> ingredients = getItems();
                for (int i = ingredients.size() - 1; i >= 0; i--) {
                    if (ModTags.isBread(ingredients.get(i).getItem())) {
                        if (ModTags.isLoaf(ingredients.get(i).getItem())) {
                            tag.putBoolean("IsOnLoaf", true);
                        }
                        break;
                    }
                }
            }

            // spawn the spread's container as an item
            if (spawnContainer && !simulate && world != null) {
                SpreadType spreadType = ModRecipeTypes.getSpreadType(ingredient, world);
                if (spreadType != null) {
                    ItemStack container = spreadType.getContainer(ingredient);
                    if (!container.isEmpty()) {
                        ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, container);
                        item.setPickupDelay(5);
                        world.addEntity(item);
                    }
                }
            }

            return ItemHandlerHelper.copyStackWithSize(stack, super.insertItem(slot, ingredient, simulate).getCount());
        }
    }
}
