package someassemblyrequired.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import someassemblyrequired.common.init.ModAdvancementTriggers;
import someassemblyrequired.common.util.SandwichBuilder;
import someassemblyrequired.common.util.SandwichIngredientHelper;
import someassemblyrequired.common.util.SandwichNameHelper;

import javax.annotation.Nullable;
import java.util.List;

public class SandwichItem extends BlockItem {

    public SandwichItem(Block block, Properties builder) {
        super(block, builder);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            int size;
            for (size = 0; size < handler.getSlots() && !handler.getStackInSlot(size).isEmpty(); size++) ;

            for (int slot = size - 1; slot >= 0; slot--) {
                // only show 8 ingredients (or 9, if there are exactly 9 ingredients)
                if (slot < size - 8 && size >= 10) {
                    tooltip.add(new TranslatableComponent("tooltip.someassemblyrequired.truncate_info", size - 8).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
                    return;
                }
                tooltip.add(handler.getStackInSlot(slot).getHoverName().plainCopy().withStyle(ChatFormatting.GRAY));
            }
        });
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (allowdedIn(group)) {
            items.add(SandwichBuilder.blt());
        }
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            return super.placeBlock(context, state);
        }
        return false;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        List<ItemStack> ingredients = SandwichIngredientHelper.getIngredients(stack);

        if (entity instanceof ServerPlayer) {
            if (SandwichIngredientHelper.isDoubleDeckerSandwich(ingredients)) {
                ModAdvancementTriggers.CONSUME_DOUBLE_DECKER_SANDWICH.trigger((ServerPlayer) entity, stack);
            } else if (SandwichIngredientHelper.isBLT(SandwichIngredientHelper.getUniqueIngredientsExcludingBread(ingredients))) {
                ModAdvancementTriggers.CONSUME_BLT_SANDWICH.trigger((ServerPlayer) entity, stack);
            }
        }

        for (ItemStack ingredient : ingredients) {
            ItemStack finishStack = ingredient.getItem().finishUsingItem(ingredient, world, entity);
            if (entity instanceof Player) {
                Player player = (Player) entity;
                if (player.getCooldowns().isOnCooldown(ingredient.getItem())) {
                    player.getCooldowns().addCooldown(this, 20);
                }
                if (!player.isCreative() && !finishStack.isEmpty()) {
                    player.addItem(finishStack);
                }
            }
        }

        return super.finishUsingItem(stack, world, entity);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            return super.getName(stack);
        }
        return SandwichNameHelper.getSandwichDisplayName(stack);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag compoundNBT) {
        return new ICapabilityProvider() {

            private final LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

            private IItemHandler createHandler() {
                ItemStackHandler handler = new IngredientItemHandler(stack);
                handler.deserializeNBT(stack.getOrCreateTagElement("BlockEntityTag").getCompound("Ingredients"));
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
            sandwichItem.getOrCreateTagElement("BlockEntityTag").put("Ingredients", serializeNBT());
        }
    }
}
