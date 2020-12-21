package someasseblyrequired.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import someasseblyrequired.common.block.tileentity.SandwichTileEntity;
import someasseblyrequired.common.init.Items;
import someasseblyrequired.common.init.TileEntityTypes;

import javax.annotation.Nullable;

public class SandwichBlock extends HorizontalBlock {

    private static final VoxelShape SHAPE = Block.makeCuboidShape(4, 0, 4, 12, 8, 12);

    public SandwichBlock(Properties properties) {
        super(properties);
        setDefaultState(getDefaultState().with(BlockStateProperties.WATERLOGGED, false).with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED, BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(BlockStateProperties.WATERLOGGED, context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER).with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing());
    }

    @SuppressWarnings("deprecation")
    public BlockState updatePostPlacement(BlockState blockState, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        if (blockState.get(BlockStateProperties.WATERLOGGED)) {
            world.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.updatePostPlacement(blockState, facing, facingState, world, currentPos, facingPos);
    }

    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.get(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        return !(world.getBlockState(pos.down()).getBlock() instanceof SandwichAssemblyTableBlock) && hasEnoughSolidSide(world, pos.down(), Direction.UP);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityTypes.SANDWICH.create();
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity tileEntity, ItemStack stack) {
        super.harvestBlock(world, player, pos, state, tileEntity, stack);
        if (tileEntity instanceof SandwichTileEntity && !player.isCreative() && tileEntity.getWorld() != null) {
            ItemStack sandwich = new ItemStack(Items.SANDWICH);
            sandwich.getOrCreateChildTag("BlockEntityTag").put("Ingredients", tileEntity.write(new CompoundNBT()).getCompound("Ingredients"));
            ItemEntity sandwichEntity = new ItemEntity(tileEntity.getWorld(), pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, sandwich);
            sandwichEntity.setDefaultPickupDelay();
            tileEntity.getWorld().addEntity(sandwichEntity);
        }
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof SandwichTileEntity && tileEntity.getWorld() != null) {
            ItemStack sandwich = new ItemStack(Items.SANDWICH);
            sandwich.getOrCreateChildTag("BlockEntityTag").put("Ingredients", tileEntity.write(new CompoundNBT()).getCompound("Ingredients"));
            return sandwich;
        }
        return ItemStack.EMPTY;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
