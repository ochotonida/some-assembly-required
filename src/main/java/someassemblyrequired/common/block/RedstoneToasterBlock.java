package someassemblyrequired.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import someassemblyrequired.common.block.tileentity.RedstoneToasterTileEntity;
import someassemblyrequired.common.init.ModTileEntityTypes;

import java.util.Random;

public class RedstoneToasterBlock extends WaterLoggableHorizontalBlock {

    public static final BooleanProperty TOASTING = BooleanProperty.create("toasting");

    private static final VoxelShape SHAPE_NORTH_SOUTH = Shapes.or(
            Block.box(3, 0, 1, 13, 1, 15),
            Block.box(4, 1, 2, 12, 10, 14)
    );
    private static final VoxelShape SHAPE_EAST_WEST = Shapes.or(
            Block.box(1, 0, 3, 15, 1, 13),
            Block.box(2, 1, 4, 14, 10, 12)
    );

    private final boolean isEjectionToaster;

    public RedstoneToasterBlock(Properties properties, boolean isEjectionToaster) {
        super(properties);
        this.isEjectionToaster = isEjectionToaster;
        registerDefaultState(defaultBlockState().setValue(TOASTING, false).setValue(BlockStateProperties.POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TOASTING, BlockStateProperties.POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(TOASTING, false).setValue(BlockStateProperties.POWERED, false).setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getClockWise());
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (!(tileEntity instanceof RedstoneToasterTileEntity)) {
            return InteractionResult.PASS;
        }

        RedstoneToasterTileEntity toaster = (RedstoneToasterTileEntity) tileEntity;
        ItemStack heldItem = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            return toaster.startToasting(player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        } else {
            if (!heldItem.isEmpty() && toaster.addItem(player.isCreative() ? heldItem.copy() : heldItem)) {
                world.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.2F, 0.8F);
                return InteractionResult.SUCCESS;
            }
            return removeIngredient(toaster, world, pos, player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
    }

    private boolean removeIngredient(RedstoneToasterTileEntity toaster, Level world, BlockPos pos, Player player) {
        ItemStack ingredient = toaster.removeItem();
        if (!ingredient.isEmpty()) {
            if (!player.isCreative()) {
                ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.75, pos.getZ() + 0.5, ingredient);
                item.setPickUpDelay(5);
                world.addFreshEntity(item);
            }
            world.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.2F, 1F);
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        // start toasting when receiving a redstone signal
        if (tileEntity instanceof RedstoneToasterTileEntity) {
            ((RedstoneToasterTileEntity) tileEntity).startToasting();
        }
    }

    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        boolean isPowered = world.hasNeighborSignal(pos) || world.hasNeighborSignal(pos.above());
        boolean wasPowered = state.getValue(BlockStateProperties.POWERED);
        if (isPowered && !wasPowered) {
            world.getBlockTicks().scheduleTick(pos, this, 4);
            world.setBlock(pos, state.setValue(BlockStateProperties.POWERED, true), 4);
        } else if (!isPowered && wasPowered) {
            world.setBlock(pos, state.setValue(BlockStateProperties.POWERED, false), 4);
        }

        if (world.getBlockState(pos).getValue(BlockStateProperties.WATERLOGGED)) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
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
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        BlockEntity tileEntity = ModTileEntityTypes.REDSTONE_TOASTER.get().create();
        if (tileEntity instanceof RedstoneToasterTileEntity) {
            ((RedstoneToasterTileEntity) tileEntity).setAutoEject(isEjectionToaster);
        }
        return tileEntity;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() == Direction.Axis.X ? SHAPE_EAST_WEST : SHAPE_NORTH_SOUTH;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return getShape(state, worldIn, pos, context);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof RedstoneToasterTileEntity) {
            return 2 * ((RedstoneToasterTileEntity) tileEntity).getAmountOfItems() - 1;
        }
        return 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!newState.is(state.getBlock())) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof RedstoneToasterTileEntity) {
                // drop contained ingredient
                NonNullList<ItemStack> items = ((RedstoneToasterTileEntity) tileEntity).removeItems();
                for (ItemStack stack : items) {
                    ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
                    world.addFreshEntity(item);
                }

                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }
}
