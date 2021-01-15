package someassemblyrequired.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import someassemblyrequired.common.block.tileentity.RedstoneToasterTileEntity;
import someassemblyrequired.common.init.TileEntityTypes;

import java.util.Random;

public class RedstoneToasterBlock extends WaterLoggableHorizontalBlock {

    public static final BooleanProperty TOASTING = BooleanProperty.create("toasting");

    private static final VoxelShape SHAPE_NORTH_SOUTH = VoxelShapes.or(
            Block.makeCuboidShape(3, 0, 1, 13, 1, 15),
            Block.makeCuboidShape(4, 1, 2, 12, 10, 14)
    );
    private static final VoxelShape SHAPE_EAST_WEST = VoxelShapes.or(
            Block.makeCuboidShape(1, 0, 3, 15, 1, 13),
            Block.makeCuboidShape(2, 1, 4, 14, 10, 12)
    );

    private final boolean isEjectionToaster;

    public RedstoneToasterBlock(Properties properties, boolean isEjectionToaster) {
        super(properties);
        this.isEjectionToaster = isEjectionToaster;
        setDefaultState(getDefaultState().with(TOASTING, false).with(BlockStateProperties.POWERED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(TOASTING, BlockStateProperties.POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).with(TOASTING, false).with(BlockStateProperties.POWERED, false).with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().rotateY());
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof RedstoneToasterTileEntity)) {
            return ActionResultType.PASS;
        }

        RedstoneToasterTileEntity toaster = (RedstoneToasterTileEntity) tileEntity;
        ItemStack heldItem = player.getHeldItem(hand);

        if (player.isSneaking()) {
            return toaster.startToasting(player) ? ActionResultType.SUCCESS : ActionResultType.PASS;
        } else {
            if (!heldItem.isEmpty() && toaster.addItem(player.isCreative() ? heldItem.copy() : heldItem)) {
                world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.2F, 0.8F);
                return ActionResultType.SUCCESS;
            }
            return removeIngredient(toaster, world, pos, player) ? ActionResultType.SUCCESS : ActionResultType.PASS;
        }
    }

    private boolean removeIngredient(RedstoneToasterTileEntity toaster, World world, BlockPos pos, PlayerEntity player) {
        ItemStack ingredient = toaster.removeItem();
        if (!ingredient.isEmpty()) {
            if (!player.isCreative()) {
                ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.75, pos.getZ() + 0.5, ingredient);
                item.setPickupDelay(5);
                world.addEntity(item);
            }
            world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.2F, 1F);
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        TileEntity tileEntity = world.getTileEntity(pos);
        // start toasting when receiving a redstone signal
        if (tileEntity instanceof RedstoneToasterTileEntity) {
            ((RedstoneToasterTileEntity) tileEntity).startToasting();
        }
    }

    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        boolean isPowered = world.isBlockPowered(pos) || world.isBlockPowered(pos.up());
        boolean wasPowered = state.get(BlockStateProperties.POWERED);
        if (isPowered && !wasPowered) {
            world.getPendingBlockTicks().scheduleTick(pos, this, 4);
            world.setBlockState(pos, state.with(BlockStateProperties.POWERED, true), 4);
        } else if (!isPowered && wasPowered) {
            world.setBlockState(pos, state.with(BlockStateProperties.POWERED, false), 4);
        }

        if (world.getBlockState(pos).get(BlockStateProperties.WATERLOGGED)) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof RedstoneToasterTileEntity && ((RedstoneToasterTileEntity) tileEntity).isToasting()) {
                ((RedstoneToasterTileEntity) tileEntity).explode();
            }
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        TileEntity tileEntity = TileEntityTypes.REDSTONE_TOASTER.get().create();
        if (tileEntity instanceof RedstoneToasterTileEntity) {
            ((RedstoneToasterTileEntity) tileEntity).setAutoEject(isEjectionToaster);
        }
        return tileEntity;
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
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(BlockState blockState, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof RedstoneToasterTileEntity) {
            return 2 * ((RedstoneToasterTileEntity) tileEntity).getAmountOfItems() - 1;
        }
        return 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!newState.isIn(state.getBlock())) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof RedstoneToasterTileEntity) {
                // drop contained ingredient
                NonNullList<ItemStack> items = ((RedstoneToasterTileEntity) tileEntity).removeItems();
                for (ItemStack stack : items) {
                    ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
                    world.addEntity(item);
                }

                world.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }
}
