package someasseblyrequired.common.block.tileentity;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import someasseblyrequired.common.block.SandwichBlock;
import someasseblyrequired.common.init.Items;
import someasseblyrequired.common.init.SpreadTypes;
import someasseblyrequired.common.init.Tags;
import someasseblyrequired.common.init.TileEntityTypes;
import someasseblyrequired.common.item.spreadtype.SpreadType;

import javax.annotation.Nullable;

public class SandwichAssemblyTableTileEntity extends ItemHandlerTileEntity {

    private final SandwichItemHandler sandwichInventory = createSandwichItemHandler();
    private final LazyOptional<SandwichItemHandler> sandwichHandler = LazyOptional.of(() -> sandwichInventory);

    public SandwichAssemblyTableTileEntity() {
        super(TileEntityTypes.SANDWICH_ASSEMBLY_TABLE);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != null && side.getAxis().isVertical()) {
            return sandwichHandler.cast();
        }
        return super.getCapability(capability, side);
    }

    private void clearIngredients() {
        for (int slot = getInventory().getSlots() - 1; slot >= 0; slot--) {
            getInventory().extractItem(slot, 1, false);
        }
    }

    public void removeTopIngredient(PlayerEntity player) {
        int slot = getAmountOfItems() - 1;
        if (slot >= 0) {
            ItemStack ingredient = getInventory().extractItem(slot, 1, false);
            if (ingredient.getItem() != Items.SPREAD) {
                if (world != null && !player.isCreative()) {
                    ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, ingredient);
                    world.addEntity(item);
                }
            }
        }
    }

    public void dropIngredients() {
        if (world != null) {
            for (int slot = 0; slot < getInventory().getSlots(); slot++) {
                ItemStack ingredient = getInventory().getStackInSlot(slot);
                if (!ingredient.isEmpty() && ingredient.getItem() != Items.SPREAD) {
                    ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ingredient);
                    world.addEntity(item);
                }
            }
        }
        clearIngredients();
    }

    public void buildSandwich(PlayerEntity player) {
        if (world != null) {
            int nextEmptySlot = getAmountOfItems();

            if (nextEmptySlot == 0) {
                player.sendStatusMessage(new TranslationTextComponent("message.someassemblyrequired.bottom_bread"), true);
            } else if (!Tags.BREADS.contains(getInventory().getStackInSlot(nextEmptySlot - 1).getItem())) {
                player.sendStatusMessage(new TranslationTextComponent("message.someassemblyrequired.top_bread"), true);
            } else {
                ItemEntity sandwichEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, SandwichBlock.createSandwich(this));
                sandwichEntity.setDefaultPickupDelay();
                world.addEntity(sandwichEntity);
                clearIngredients();
            }
        }
    }

    public boolean addIngredient(PlayerEntity player, Hand hand) {
        ItemStack ingredient = player.getHeldItem(hand).copy();
        ingredient.setCount(1);

        int nextEmptySlot = getAmountOfItems();
        if (getInventory().isItemValid(nextEmptySlot, ingredient)) {
            ((IngredientHandler) getInventory()).insertItem(ingredient, player, hand);
            if (!player.isCreative()) {
                player.getHeldItem(hand).shrink(1);
            }
        } else if (ingredient.isFood() || ingredient.getItem() == Items.SPREAD || SpreadTypes.hasSpreadType(ingredient.getItem())) {
            if (nextEmptySlot == 0 && !Tags.BREADS.contains(ingredient.getItem())) {
                player.sendStatusMessage(new TranslationTextComponent("message.someassemblyrequired.bottom_bread"), true);
            } else if (nextEmptySlot >= getInventory().getSlots()) {
                player.sendStatusMessage(new TranslationTextComponent("message.someassemblyrequired.full_sandwich"), true);
            } else {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    private static ItemStack createSpread(SpreadType spreadType, ItemStack ingredient) {
        CompoundNBT spreadNBT = new CompoundNBT();
        spreadNBT.putInt("Color", spreadType.getColor(ingredient));
        spreadNBT.putBoolean("HasEffect", ingredient.hasEffect());
        spreadNBT.put("Ingredient", ingredient.write(new CompoundNBT()));
        ItemStack spread = new ItemStack(Items.SPREAD, ingredient.getCount());
        spread.setTag(spreadNBT);
        return spread;
    }

    @Override
    protected void onContentsChanged() {
        super.onContentsChanged();
        sandwichInventory.update();
    }

    private SandwichItemHandler createSandwichItemHandler() {
        return new SandwichItemHandler();
    }

    @Override
    protected TileEntityItemHandler createItemHandler(int size, boolean canExtract) {
        return new IngredientHandler(16);
    }

    private class SandwichItemHandler implements IItemHandler {

        private ItemStack sandwich = ItemStack.EMPTY;

        public void update() {
            int nextEmptySlot = SandwichAssemblyTableTileEntity.this.getAmountOfItems();

            if (nextEmptySlot < 2 || !Tags.BREADS.contains(SandwichAssemblyTableTileEntity.this.getInventory().getStackInSlot(nextEmptySlot - 1).getItem())) {
                sandwich = ItemStack.EMPTY;
            } else {
                sandwich = new ItemStack(Items.SANDWICH);
                SandwichAssemblyTableTileEntity.this.write(sandwich.getOrCreateChildTag("BlockEntityTag"));
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
                SandwichAssemblyTableTileEntity.this.clearIngredients();
            }

            return result;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            validateSlotIndex(slot);
            return stack;
        }
    }

    private class IngredientHandler extends TileEntityItemHandler {

        private IngredientHandler(int size) {
            super(size, true);
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            validateSlotIndex(slot);
            if (slot < getSlots() - 1 && !getStackInSlot(slot + 1).isEmpty()) {
                return ItemStack.EMPTY;
            }
            return super.extractItem(slot, amount, simulate);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (stack.getItem() == Items.SANDWICH) {
                return getStackInSlot(0).isEmpty();
            }

            return (slot == 0 || !getStackInSlot(slot - 1).isEmpty())
                    && slot < getSlots() && getStackInSlot(slot).isEmpty()
                    && (stack.isFood() || stack.getItem() == Items.SPREAD || SpreadTypes.hasSpreadType(stack.getItem()))
                    && (slot > 0 || Tags.BREADS.contains(stack.getItem()));
        }

        private void insertItem(ItemStack stack, PlayerEntity player, Hand hand) {
            SpreadType spreadType = SpreadTypes.findSpreadType(stack.getItem());
            ItemStack ingredient = spreadType == null ? stack : createSpread(spreadType, stack);
            if (spreadType != null && spreadType.hasContainer(ingredient) && !player.isCreative()) {
                ItemStack container = new ItemStack(spreadType.getContainer(ingredient), 1);
                if (player.getHeldItem(hand).isEmpty()) {
                    player.setHeldItem(hand, container);
                } else {
                    player.addItemStackToInventory(container);
                }
            }

            for (int slot = 0; slot < getSlots(); slot++) {
                if (getStackInSlot(slot).isEmpty()) {
                    insertItem(slot, ingredient, false);
                    return;
                }
            }
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (stack.isEmpty())
                return ItemStack.EMPTY;

            if (!isItemValid(slot, stack))
                return stack;

            validateSlotIndex(slot);

            if (stack.getItem() == Items.SANDWICH) {
                stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(sandwichHandler -> {
                    for (int sandwichSlot = 0; sandwichSlot < sandwichHandler.getSlots(); sandwichSlot++) {
                        setStackInSlot(sandwichSlot, sandwichHandler.getStackInSlot(sandwichSlot));
                    }
                });
                return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - 1);
            }

            SpreadType spreadType = SpreadTypes.findSpreadType(stack.getItem());
            ItemStack ingredient = spreadType == null ? stack : createSpread(spreadType, stack);

            if (!simulate && spreadType != null && SandwichAssemblyTableTileEntity.this.getWorld() != null && spreadType.hasContainer(ingredient)) {
                ItemEntity sandwichEntity = new ItemEntity(SandwichAssemblyTableTileEntity.this.getWorld(), SandwichAssemblyTableTileEntity.this.pos.getX() + 0.5, SandwichAssemblyTableTileEntity.this.pos.getY() + 1.2, SandwichAssemblyTableTileEntity.this.pos.getZ() + 0.5, new ItemStack(spreadType.getContainer(ingredient)));
                sandwichEntity.setDefaultPickupDelay();
                SandwichAssemblyTableTileEntity.this.getWorld().addEntity(sandwichEntity);
            }

            return ItemHandlerHelper.copyStackWithSize(stack, super.insertItem(slot, ingredient, simulate).getCount());
        }
    }
}
