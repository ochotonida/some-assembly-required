package someassemblyrequired.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import someassemblyrequired.config.ModConfig;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.init.ModBlockEntityTypes;
import someassemblyrequired.init.ModBlocks;
import someassemblyrequired.init.ModItems;
import someassemblyrequired.init.ModSoundEvents;
import someassemblyrequired.item.sandwich.SandwichItemHandler;
import someassemblyrequired.util.Util;

import javax.annotation.Nullable;

public class SandwichBlockEntity extends BlockEntity {

    private final SandwichItemHandler sandwich = new ItemHandler();
    private final LazyOptional<SandwichItemHandler> itemHandler = LazyOptional.of(() -> sandwich);

    public SandwichBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.SANDWICH.get(), pos, state);
    }

    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.getItemInHand(InteractionHand.OFF_HAND).isEmpty() && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            removeItem(player);
            return InteractionResult.SUCCESS;
        }
        
        return addItem(player, hand);
    }

    private void removeItem(Player player) {
        if (level == null) {
            return;
        }
        ItemStack stack = sandwich.top();
        Ingredients.playRemoveSound(stack, level, player, getBlockPos());
        if (level.isClientSide()) {
            return;
        }

        sandwich.pop();
        BlockPos pos = getBlockPos();
        if (!Ingredients.hasContainer(stack) && !player.isCreative()) {
            double y = pos.getY() + Math.max(0.2, sandwich.getTotalHeight() * (1 / 32D) - 0.2);
            ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, y, pos.getZ() + 0.5, stack);
            item.setPickUpDelay(5);
            level.addFreshEntity(item);
        }

        updateHeight();
    }

    private InteractionResult addItem(Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).isEmpty()) {
            return InteractionResult.PASS;
        }
        ItemStack itemToAdd = player.getItemInHand(hand).copy();
        itemToAdd.setCount(1);
        if (itemToAdd.is(ModItems.SANDWICH.get())) {
            addSandwich(player, hand, itemToAdd);
            updateHeight();
            return InteractionResult.SUCCESS;
        } else if (!Ingredients.canAddToSandwich(itemToAdd)) {
            return InteractionResult.PASS;
        } else if (sandwich.getTotalHeight() + Ingredients.getHeight(itemToAdd) > ModConfig.server.maximumSandwichHeight.get()) {
            player.displayClientMessage(Util.translate("message.full_sandwich"), true);
            return InteractionResult.SUCCESS;
        } else {
            addSingleItem(player, hand, itemToAdd);
            updateHeight();
            return InteractionResult.SUCCESS;
        }
    }

    private void addSingleItem(Player player, InteractionHand hand, ItemStack stack) {
        if (level == null) {
            return;
        }
        Ingredients.playApplySound(stack, level, player, getBlockPos());
        if (level.isClientSide()) {
            return;
        }

        sandwich.add(stack);
        shrinkHeldItem(player, hand);
    }

    private void addSandwich(Player player, InteractionHand hand, ItemStack stack) {
        if (level == null || level.isClientSide()) {
            return;
        }
        SandwichItemHandler.get(stack).ifPresent(handler -> {
            if (!sandwich.canAdd(handler)) {
                player.displayClientMessage(Util.translate("message.full_sandwich"), true);
            } else {
                sandwich.add(handler);
                shrinkHeldItem(player, hand);
                level.playSound(null, getBlockPos(), ModSoundEvents.ADD_ITEM.get(), SoundSource.BLOCKS, 1, 1);
            }
        });
    }

    private static void shrinkHeldItem(Player player, InteractionHand hand) {
        if (!player.isCreative()) {
            ItemStack item = player.getItemInHand(hand);
            ItemStack container = Ingredients.getContainer(item).copy();
            item.shrink(1);
            if (!container.isEmpty()) {
                if (player.getItemInHand(hand).isEmpty()) {
                    player.setItemInHand(hand, container);
                } else if (!player.getInventory().add(container)) {
                    player.drop(container, false);
                }
            }
        }
    }

    public void updateHeight() {
        if (level == null) {
            return;
        }
        if (sandwich.isEmpty()) {
            level.removeBlock(getBlockPos(), false);
        } else {
            SandwichItemHandler.get(level.getBlockEntity(getBlockPos()))
                    .ifPresent(sandwich -> {
                        BlockState state = level.getBlockState(getBlockPos());
                        if (state.is(ModBlocks.SANDWICH.get())) {
                            BlockState newState = state.setValue(SandwichBlock.SIZE, SandwichBlock.getSizeFromSandwich(sandwich));
                            if (!newState.getValue(SandwichBlock.SIZE).equals(state.getValue(SandwichBlock.SIZE))) {
                                level.setBlock(getBlockPos(), state.setValue(SandwichBlock.SIZE, SandwichBlock.getSizeFromSandwich(sandwich)), Block.UPDATE_ALL);
                            }
                        }
                    });
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        sandwich.deserializeNBT(tag.getList("Sandwich", Tag.TAG_COMPOUND));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.put("Sandwich", sandwich.serializeNBT());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandler.cast();
        }
        return super.getCapability(capability, side);
    }

    private class ItemHandler extends SandwichItemHandler {

        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            if (getLevel() instanceof ServerLevel level) {
                level.getChunkSource().blockChanged(getBlockPos());
                setChanged();
            }
        }
    }
}
