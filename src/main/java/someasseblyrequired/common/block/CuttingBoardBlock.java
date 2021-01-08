package someasseblyrequired.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import someasseblyrequired.common.block.tileentity.CuttingBoardTileEntity;
import someasseblyrequired.common.init.Tags;
import someasseblyrequired.common.init.TileEntityTypes;

import java.util.List;

public class CuttingBoardBlock extends WaterLoggableHorizontalBlock {

    private static final VoxelShape SHAPE_NORTH_SOUTH = Block.makeCuboidShape(1, 0, 2, 15, 1, 14);
    private static final VoxelShape SHAPE_EAST_WEST = Block.makeCuboidShape(2, 0, 1, 14, 1, 15);

    public CuttingBoardBlock(Properties properties) {
        super(properties);
    }

    public static boolean isKnife(ItemStack stack) {
        return Tags.KNIVES.contains(stack.getItem()) || stack.getItem() instanceof SwordItem;
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof CuttingBoardTileEntity)) {
            return ActionResultType.PASS;
        }

        CuttingBoardTileEntity cuttingBoard = (CuttingBoardTileEntity) tileEntity;
        ItemStack heldItem = player.getHeldItem(hand);

        if (!cuttingBoard.hasIngredient() && !heldItem.isEmpty()) {
            return addIngredient(heldItem, cuttingBoard, world, pos, player, hand) ? ActionResultType.SUCCESS : ActionResultType.PASS;
        } else if (heldItem.isEmpty() && hand == Hand.MAIN_HAND) {
            return removeIngredient(cuttingBoard, world, pos, player) ? ActionResultType.SUCCESS : ActionResultType.PASS;
        } else {
            return cutIngredient(heldItem, cuttingBoard, world, pos, player) ? ActionResultType.SUCCESS : ActionResultType.PASS;
        }
    }

    private boolean addIngredient(ItemStack ingredient, CuttingBoardTileEntity cuttingBoard, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        if (!player.getHeldItem(Hand.OFF_HAND).isEmpty() && hand.equals(Hand.MAIN_HAND) && !(player.getHeldItem(hand).getItem() instanceof BlockItem)) {
            // prefer off-hand placement of items
            return false;
        }

        // play block place sound if the item is a block
        if (ingredient.getItem() instanceof BlockItem) {
            Block block = ((BlockItem) ingredient.getItem()).getBlock();
            SoundType soundType = block.getSoundType(block.getDefaultState(), world, pos, player);
            world.playSound(player, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS, 0.5F * (soundType.getVolume() + 1) / 2, soundType.getPitch() * 0.8F);
        } else if (isKnife(ingredient)) {
            world.playSound(player, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 0.5F, 0.8F);
        } else {
            world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.2F, 0.8F);
        }

        boolean hasAddedIngredient = cuttingBoard.addIngredient(player.isCreative() ? ingredient.copy() : ingredient);
        assert hasAddedIngredient;

        return true;
    }

    private boolean cutIngredient(ItemStack tool, CuttingBoardTileEntity cuttingBoard, World world, BlockPos pos, PlayerEntity player) {
        List<ItemStack> results = cuttingBoard.cutIngredient(tool);
        if (results.isEmpty()) {
            return false;
        }

        for (ItemStack result : results) {
            ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, result);
            item.setPickupDelay(5);
            world.addEntity(item);
        }

        world.playSound(player, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, SoundEvents.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 0.7F, 0.8F);

        return true;
    }

    private boolean removeIngredient(CuttingBoardTileEntity cuttingBoard, World world, BlockPos pos, PlayerEntity player) {
        ItemStack ingredient = cuttingBoard.removeIngredient();
        if (!ingredient.isEmpty()) {
            if (!player.isCreative()) {
                ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, ingredient);
                item.setPickupDelay(5);
                world.addEntity(item);
            }
            world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.2F, 1F);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityTypes.CUTTING_BOARD.get().create();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(BlockState blockState, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof CuttingBoardTileEntity)) {
            return 0;
        }

        return ((CuttingBoardTileEntity) tileEntity).hasIngredient() ? 15 : 0;

    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return state.get(BlockStateProperties.HORIZONTAL_FACING).getAxis() == Direction.Axis.X ? SHAPE_EAST_WEST : SHAPE_NORTH_SOUTH;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return getShape(state, worldIn, pos, context);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!newState.isIn(state.getBlock())) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof CuttingBoardTileEntity) {
                // drop contained ingredient
                ItemStack ingredient = ((CuttingBoardTileEntity) tileEntity).removeIngredient();
                if (!ingredient.isEmpty()) {
                    ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, ingredient);
                    world.addEntity(item);
                }

                world.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }
}
