package someasseblyrequired.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import someasseblyrequired.common.block.tileentity.CuttingBoardTileEntity;
import someasseblyrequired.common.init.Tags;
import someasseblyrequired.common.init.TileEntityTypes;

public class CuttingBoardBlock extends WaterLoggableHorizontalBlock {

    private static final VoxelShape SHAPE_NORTH_SOUTH = Block.makeCuboidShape(1, 0, 2, 15, 1, 14);
    private static final VoxelShape SHAPE_WEAST = Block.makeCuboidShape(2, 0, 1, 14, 1, 15);

    public CuttingBoardBlock(Properties properties) {
        super(properties);
    }

    private static boolean isKnife(ItemStack stack) {
        return Tags.KNIVES.contains(stack.getItem()) || stack.getItem() instanceof SwordItem;
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof CuttingBoardTileEntity) {
            CuttingBoardTileEntity cuttingBoard = (CuttingBoardTileEntity) tileEntity;
            ItemStack heldStack = player.getHeldItem(hand);
            if (!cuttingBoard.hasIngredient()) {
                if (isKnife(heldStack)) {
                    return ActionResultType.PASS;
                }
                if (cuttingBoard.addIngredient(player.isCreative() ? heldStack.copy() : heldStack)) {
                    return ActionResultType.SUCCESS;
                }
            } else if (isKnife(heldStack)) {
                ItemStack result = cuttingBoard.cutIngredient();
                if (!result.isEmpty()) {
                    ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, result);
                    world.addEntity(item);
                    return ActionResultType.SUCCESS;
                }
            } else if (heldStack.isEmpty() && hand == Hand.MAIN_HAND) {
                ItemStack ingredient = cuttingBoard.removeIngredient();
                if (!ingredient.isEmpty()) {
                    if (!player.isCreative()) {
                        ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, ingredient);
                        world.addEntity(item);
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityTypes.CUTTING_BOARD.create();
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
        if (tileEntity instanceof CuttingBoardTileEntity) {
            if (((CuttingBoardTileEntity) tileEntity).hasIngredient()) {
                return 15;
            }
        }
        return 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return state.get(BlockStateProperties.HORIZONTAL_FACING).getAxis() == Direction.Axis.X ? SHAPE_WEAST : SHAPE_NORTH_SOUTH;
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
