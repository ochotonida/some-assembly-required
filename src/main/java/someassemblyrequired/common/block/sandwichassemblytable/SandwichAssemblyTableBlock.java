package someassemblyrequired.common.block.sandwichassemblytable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import org.jetbrains.annotations.Nullable;
import someassemblyrequired.common.block.sandwich.SandwichBlock;
import someassemblyrequired.common.init.ModAdvancementTriggers;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;
import someassemblyrequired.common.item.spreadtype.SpreadType;
import someassemblyrequired.common.item.spreadtype.SpreadTypeManager;

public class SandwichAssemblyTableBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public SandwichAssemblyTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection());
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (!(tileEntity instanceof SandwichAssemblyTableBlockEntity sandwichTable)) {
            return InteractionResult.PASS;
        }

        if (player.isShiftKeyDown()) {
            // a sandwich must contain at least one ingredient
            if (sandwichTable.getAmountOfItems() == 0) {
                player.displayClientMessage(new TranslatableComponent("message.someassemblyrequired.bottom_bread"), true);
                // a sandwich must have bread as the top and bottom ingredient
            } else if (!sandwichTable.hasBreadAsTopIngredient()) {
                player.displayClientMessage(new TranslatableComponent("message.someassemblyrequired.top_bread"), true);
            } else {
                world.playSound(player, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 0.5F, 1.4F);
                ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, SandwichBlock.createSandwich(tileEntity));
                item.setPickUpDelay(5);
                world.addFreshEntity(item);
                sandwichTable.removeItems();
            }
            // remove the top ingredient if the player is not holding anything
        } else if (player.getItemInHand(InteractionHand.OFF_HAND).isEmpty() && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            if (sandwichTable.getAmountOfItems() > 0) {
                ItemStack ingredient = sandwichTable.removeTopIngredient();
                if (!ingredient.isEmpty()) {
                    if (!player.isCreative()) {
                        ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, ingredient);
                        item.setPickUpDelay(5);
                        world.addFreshEntity(item);
                    }
                    world.playSound(player, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.3F, 1.6F);
                } else {
                    world.playSound(player, pos, SoundEvents.HONEY_BLOCK_BREAK, SoundSource.BLOCKS, 0.3F, 1.6F);
                }
            }
        } else if (!addIngredient(sandwichTable, world, pos, player, hand)) {
            return InteractionResult.FAIL;
        }
        return InteractionResult.SUCCESS;
    }

    private boolean addIngredient(SandwichAssemblyTableBlockEntity sandwichTable, Level world, BlockPos pos, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        // try to add the ingredient
        if (sandwichTable.addIngredient(heldStack)) {
            SpreadType spreadType = SpreadTypeManager.INSTANCE.getSpreadType(heldStack.getItem());

            if (spreadType == null) {
                world.playSound(player, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.3F, 1.3F);
            } else {
                world.playSound(player, pos, SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS, 0.3F, 1.3F);

                if (player instanceof ServerPlayer && heldStack.getItem() == Items.POTION && PotionUtils.getPotion(heldStack) != Potions.WATER) {
                    ModAdvancementTriggers.ADD_POTION_TO_SANDWICH.trigger((ServerPlayer) player, heldStack);
                }
            }

            if (!player.isCreative()) {
                // decrease the player's held item by one if the ingredient successfully got added
                player.getItemInHand(hand).shrink(1);

                if (spreadType != null && spreadType.hasContainer(heldStack)) {
                    // add the container item of the spreadtype to the player's inventory
                    ItemStack container = new ItemStack(spreadType.getContainer(heldStack), 1);
                    if (player.getItemInHand(hand).isEmpty()) {
                        player.setItemInHand(hand, container);
                    } else {
                        player.addItem(container);
                    }
                }
            }
        } else if (heldStack.isEdible() || heldStack.getItem() == ModItems.SPREAD.get() || SpreadTypeManager.INSTANCE.hasSpreadType(heldStack.getItem())) {
            if (sandwichTable.getAmountOfItems() == 0 && !ModTags.SANDWICH_BREADS.contains(heldStack.getItem())) {
                player.displayClientMessage(new TranslatableComponent("message.someassemblyrequired.bottom_bread"), true);
            } else if (sandwichTable.getAmountOfItems() == sandwichTable.getInventorySize()) {
                player.displayClientMessage(new TranslatableComponent("message.someassemblyrequired.full_sandwich"), true);
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        // drop all contained items and update the comparator output if the block is removed
        if (!newState.is(state.getBlock())) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof SandwichAssemblyTableBlockEntity) {
                for (ItemStack ingredient : ((SandwichAssemblyTableBlockEntity) tileEntity).removeItems()) {
                    ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, ingredient);
                    world.addFreshEntity(item);
                }

                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
        // comparator input = max(15, <items on assembly table>)
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity != null) {
            IItemHandler handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(EmptyHandler.INSTANCE);
            for (int slot = 0; slot < handler.getSlots() && slot <= 15; slot++) {
                if (handler.getStackInSlot(slot).isEmpty()) {
                    return slot;
                }
            }
            return 15;
        }
        return 0;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SandwichAssemblyTableBlockEntity(pos, state);
    }
}
