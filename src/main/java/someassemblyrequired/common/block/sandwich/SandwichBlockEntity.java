package someassemblyrequired.common.block.sandwich;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.ingredient.Ingredients;
import someassemblyrequired.common.init.ModBlockEntityTypes;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.item.sandwich.SandwichItemHandler;

import javax.annotation.Nullable;

public class SandwichBlockEntity extends BlockEntity {

    private final SandwichItemHandler sandwich = new ItemHandler();
    private final LazyOptional<SandwichItemHandler> itemHandler = LazyOptional.of(() -> sandwich);

    public SandwichBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.SANDWICH.get(), pos, state);
    }

    private int getMaxHeight() {
        return 16; // TODO config
    }

    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.getItemInHand(InteractionHand.OFF_HAND).isEmpty() && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            removeItem(player);
            return InteractionResult.SUCCESS;
        } else {
            return addItem(player, hand);
        }
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
            ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, stack);
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
        } else if (sandwich.size() >= getMaxHeight()) {
            player.displayClientMessage(new TranslatableComponent("message.%s.full_sandwich".formatted(SomeAssemblyRequired.MODID)), true);
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
                player.displayClientMessage(new TranslatableComponent("message.%s.full_sandwich".formatted(SomeAssemblyRequired.MODID)), true);
            } else {
                sandwich.add(handler);
                shrinkHeldItem(player, hand);
                level.playSound(null, getBlockPos(), SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.3F, 1.3F);
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
                            BlockState newState = state.setValue(SandwichBlock.SIZE, getSizeFromHeight(sandwich.size()));
                            if (!newState.getValue(SandwichBlock.SIZE).equals(state.getValue(SandwichBlock.SIZE))) {
                                level.setBlock(getBlockPos(), state.setValue(SandwichBlock.SIZE, getSizeFromHeight(sandwich.size())), Block.UPDATE_ALL);
                            }
                        }
                    });
        }
    }

    static int getSizeFromHeight(int sandwichHeight) {
        int size = Math.min(16, Math.max(2, sandwichHeight)) + 1;
        return size / 2;
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
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
        }
        return super.getCapability(capability, side);
    }

    private class ItemHandler extends SandwichItemHandler {

        @Override
        protected void onContentsChanged() {
            if (getLevel() instanceof ServerLevel level) {
                level.getChunkSource().blockChanged(getBlockPos());
                setChanged();
            }
        }
    }
}
