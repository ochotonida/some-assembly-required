package someassemblyrequired.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import someassemblyrequired.common.util.SandwichNameHelper;

import javax.annotation.Nullable;
import java.util.List;

public class SandwichItem extends BlockItem {

    public SandwichItem(Block block, Properties builder) {
        super(block, builder);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            int size;
            for (size = 0; size < handler.getSlots() && !handler.getStackInSlot(size).isEmpty(); size++) ;

            for (int slot = size - 1; slot >= 0; slot--) {
                // only show 8 ingredients (or 9, if there are exactly 9 ingredients)
                if (slot < size - 8 && size >= 10) {
                    tooltip.add(new TranslationTextComponent("tooltip.someassemblyrequired.truncate_info", size - 8).mergeStyle(TextFormatting.GRAY).mergeStyle(TextFormatting.ITALIC));
                    return;
                }
                tooltip.add(handler.getStackInSlot(slot).getDisplayName().copyRaw().mergeStyle(TextFormatting.GRAY));
            }
        });
    }

    @Override
    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        if (context.getPlayer() != null && context.getPlayer().isSneaking()) {
            return super.placeBlock(context, state);
        }
        return false;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entity) {
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            for (int slot = 0; slot < handler.getSlots() && !handler.getStackInSlot(slot).isEmpty(); slot++) {
                ItemStack ingredient = handler.getStackInSlot(slot);
                ItemStack finishStack = ingredient.getItem().onItemUseFinish(ingredient, world, entity);
                if (entity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) entity;
                    if (player.getCooldownTracker().hasCooldown(ingredient.getItem())) {
                        player.getCooldownTracker().setCooldown(this, 20);
                    }
                    if (!player.isCreative() && !finishStack.isEmpty()) {
                        player.addItemStackToInventory(finishStack);
                    }
                }
            }
        });
        return super.onItemUseFinish(stack, world, entity);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (stack.hasDisplayName()) {
            return super.getDisplayName(stack);
        }
        return SandwichNameHelper.getSandwichDisplayName(stack);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT compoundNBT) {
        return new ICapabilityProvider() {

            private final LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

            private IItemHandler createHandler() {
                ItemStackHandler handler = new IngredientItemHandler(stack);
                handler.deserializeNBT(stack.getOrCreateChildTag("BlockEntityTag").getCompound("Ingredients"));
                return handler;
            }

            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
                if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                    return handler.cast();
                }
                return LazyOptional.empty();
            }
        };
    }

    private static class IngredientItemHandler extends ItemStackHandler {

        private final ItemStack sandwichItem;

        private IngredientItemHandler(ItemStack sandwichItem) {
            super(0);
            this.sandwichItem = sandwichItem;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        protected void onContentsChanged(int slot) {
            sandwichItem.getOrCreateChildTag("BlockEntityTag").put("Ingredients", serializeNBT());
        }
    }
}
