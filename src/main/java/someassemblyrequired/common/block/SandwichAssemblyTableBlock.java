package someassemblyrequired.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import someassemblyrequired.common.block.tileentity.SandwichAssemblyTableTileEntity;
import someassemblyrequired.common.init.*;
import someassemblyrequired.common.recipe.SpreadType;

public class SandwichAssemblyTableBlock extends HorizontalBlock {

    public SandwichAssemblyTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntityTypes.SANDWICH_ASSEMBLY_TABLE.get().create();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing());
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof SandwichAssemblyTableTileEntity)) {
            return ActionResultType.PASS;
        }
        SandwichAssemblyTableTileEntity sandwichTable = (SandwichAssemblyTableTileEntity) tileEntity;

        if (player.isSneaking()) {
            createSandwich(sandwichTable, world, pos, player);
        } else if (player.getHeldItem(Hand.OFF_HAND).isEmpty() && player.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
            removeIngredient(sandwichTable, world, pos, player, hand);
        } else if (!addIngredient(sandwichTable, world, pos, player, hand)) {
            return ActionResultType.FAIL;
        }
        return ActionResultType.SUCCESS;
    }

    private void createSandwich(SandwichAssemblyTableTileEntity sandwichTable, World world, BlockPos pos, PlayerEntity player) {
        if (sandwichTable.getAmountOfItems() == 0) {
            // a sandwich must contain at least one ingredient
            player.sendStatusMessage(new TranslationTextComponent("message.someassemblyrequired.bottom_bread"), true);
        } else if (!sandwichTable.hasBreadAsTopIngredient()) {
            // a sandwich must have bread as the top and bottom ingredient
            player.sendStatusMessage(new TranslationTextComponent("message.someassemblyrequired.top_bread"), true);
        } else {
            world.playSound(player, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 0.5F, 1.4F);
            ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, SandwichBlock.createSandwich(sandwichTable));
            item.setPickupDelay(5);
            world.addEntity(item);
            sandwichTable.removeItems();
        }
    }

    private void removeIngredient(SandwichAssemblyTableTileEntity sandwichTable, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack topIngredient = sandwichTable.removeTopIngredient();
        if (!topIngredient.isEmpty()) {
            SpreadType spreadType = ModRecipeTypes.getSpreadType(topIngredient, world);
            if (spreadType != null) {
                world.playSound(player, pos, spreadType.getSoundEvent(), SoundCategory.BLOCKS, 0.3F, 1);
            } else {
                if (!player.isCreative()) {
                    addItemStackToInventory(topIngredient, player, hand);
                }
                world.playSound(player, pos, ModSoundEvents.ADD_INGREDIENT.get(), SoundCategory.BLOCKS, 0.3F, 1);
            }
        }
    }

    private boolean addIngredient(SandwichAssemblyTableTileEntity sandwichTable, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        // try to add the ingredient
        if (sandwichTable.addIngredient(heldStack)) {
            SpreadType spreadType = ModRecipeTypes.getSpreadType(heldStack, world);

            if (spreadType == null) {
                world.playSound(player, pos, ModSoundEvents.ADD_INGREDIENT.get(), SoundCategory.BLOCKS, 0.3F, 0.8F);
            } else {
                world.playSound(player, pos, spreadType.getSoundEvent(), SoundCategory.BLOCKS, 0.3F, 0.8F);

                if (player instanceof ServerPlayerEntity && heldStack.getItem() == Items.POTION && PotionUtils.getPotionFromItem(heldStack) != Potions.WATER) {
                    ModAdvancementTriggers.ADD_POTION_TO_SANDWICH.trigger((ServerPlayerEntity) player, heldStack);
                }
            }

            if (!player.isCreative()) {
                ItemStack container = spreadType == null ? ItemStack.EMPTY : spreadType.getContainer(heldStack);
                player.getHeldItem(hand).shrink(1);
                addItemStackToInventory(container, player, hand);
            }
        } else if (heldStack.isFood() || ModRecipeTypes.getSpreadType(heldStack, world) != null) {
            if (sandwichTable.getAmountOfItems() == 0 && !ModTags.isBread(heldStack.getItem())) {
                player.sendStatusMessage(new TranslationTextComponent("message.someassemblyrequired.bottom_bread"), true);
            } else if (sandwichTable.getAmountOfItems() == sandwichTable.getInventorySize()) {
                player.sendStatusMessage(new TranslationTextComponent("message.someassemblyrequired.full_sandwich"), true);
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private static void addItemStackToInventory(ItemStack stack, PlayerEntity player, Hand hand) {
        if (!stack.isEmpty()) {
            if (player.getHeldItem(hand).isEmpty()) {
                player.setHeldItem(hand, stack);
            } else {
                player.addItemStackToInventory(stack);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        // drop all contained items and update the comparator output if the block is removed
        if (!newState.isIn(state.getBlock())) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof SandwichAssemblyTableTileEntity) {
                for (ItemStack ingredient : ((SandwichAssemblyTableTileEntity) tileEntity).removeItems()) {
                    ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, ingredient);
                    world.addEntity(item);
                }

                world.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(BlockState blockState, World world, BlockPos pos) {
        // comparator input = max(15, <items on assembly table>)
        TileEntity tileEntity = world.getTileEntity(pos);
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
}
