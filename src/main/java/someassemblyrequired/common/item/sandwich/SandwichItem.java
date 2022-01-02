package someassemblyrequired.common.item.sandwich;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModAdvancementTriggers;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class SandwichItem extends BlockItem {

    public SandwichItem(Block block, Properties builder) {
        super(block, builder);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        SandwichItemHandler.get(stack).ifPresent(handler -> {
            int size = handler.size();
            int ingredientsToShow = Math.min(size, 8);
            if (size == ingredientsToShow + 1) {
                ingredientsToShow++;
            }

            for (int i = 0; i < ingredientsToShow; i++) {
                tooltip.add(handler.getStackInSlot(size - i - 1).getHoverName().plainCopy().withStyle(ChatFormatting.GRAY));
            }

            if (size > ingredientsToShow) {
                tooltip.add(
                        new TranslatableComponent("tooltip.%s.truncate_info".formatted(SomeAssemblyRequired.MODID), size - ingredientsToShow)
                                .withStyle(ChatFormatting.GRAY)
                                .withStyle(ChatFormatting.ITALIC)
                );
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
            if (super.placeBlock(context, state)) {
                Level level = context.getLevel();
                BlockEntity blockEntity = level.getBlockEntity(context.getClickedPos());
                CompoundTag tag = context.getItemInHand().getTag();
                if (context.getLevel().isClientSide() && blockEntity != null && tag != null) {
                    blockEntity.load(tag.getCompound("BlockEntityTag"));
                    level.sendBlockUpdated(context.getClickedPos(), state, level.getBlockState(context.getClickedPos()), Block.UPDATE_CLIENTS);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        triggerAdvancements(stack, entity);

        SandwichItemHandler.get(stack).ifPresent(sandwich -> {
            for (ItemStack item : sandwich.items) {
                ItemStack finishStack = item.getItem().finishUsingItem(item.copy(), world, entity);
                if (entity instanceof Player player) {
                    if (player.getCooldowns().isOnCooldown(item.getItem())) {
                        player.getCooldowns().addCooldown(this, 20);
                    }
                    if (!player.isCreative() && !finishStack.isEmpty()) {
                        player.addItem(finishStack);
                    }
                }
            }
        });

        return super.finishUsingItem(stack, world, entity);
    }

    private void triggerAdvancements(ItemStack stack, LivingEntity entity) {
        SandwichItemHandler.get(stack).ifPresent(sandwich -> {
            if (entity instanceof ServerPlayer) {
                if (sandwich.isDoubleDeckerSandwich()) {
                    ModAdvancementTriggers.CONSUME_DOUBLE_DECKER_SANDWICH.trigger((ServerPlayer) entity, stack);
                } else if (sandwich.isBLT()) {
                    ModAdvancementTriggers.CONSUME_BLT_SANDWICH.trigger((ServerPlayer) entity, stack);
                }
            }
        });
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            return super.getName(stack);
        }
        return SandwichNameHelper.getSandwichDisplayName(stack);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {

            private final BlockEntityWithoutLevelRenderer renderer = new SandwichItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag compoundNBT) {
        return new ICapabilityProvider() {

            private final LazyOptional<SandwichItemHandler> handler = LazyOptional.of(this::createHandler);

            private SandwichItemHandler createHandler() {
                SandwichItemHandler handler = new ItemHandler(stack);
                handler.deserializeNBT(stack.getOrCreateTagElement("BlockEntityTag").getList("Sandwich", Tag.TAG_COMPOUND));
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

    private static class ItemHandler extends SandwichItemHandler {

        private final ItemStack sandwich;

        private ItemHandler(ItemStack sandwich) {
            this.sandwich = sandwich;
        }

        @Override
        protected void onContentsChanged() {
            sandwich.getOrCreateTagElement("BlockEntityTag").put("Sandwich", serializeNBT());
        }
    }
}
