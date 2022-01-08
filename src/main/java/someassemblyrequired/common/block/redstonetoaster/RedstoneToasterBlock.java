package someassemblyrequired.common.block.redstonetoaster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import someassemblyrequired.common.block.WaterLoggableHorizontalBlock;

import javax.annotation.Nullable;
import java.util.Random;

public class RedstoneToasterBlock extends WaterLoggableHorizontalBlock implements EntityBlock {

    public static final BooleanProperty TOASTING = BooleanProperty.create("toasting");

    private static final VoxelShape SHAPE_NORTH_SOUTH = Shapes.or(
            Block.box(3, 0, 1, 13, 1, 15),
            Block.box(4, 1, 2, 12, 10, 14)
    );
    private static final VoxelShape SHAPE_EAST_WEST = Shapes.or(
            Block.box(1, 0, 3, 15, 1, 13),
            Block.box(2, 1, 4, 14, 10, 12)
    );

    private final boolean isSticky;

    public RedstoneToasterBlock(Properties properties, boolean isSticky) {
        super(properties);
        this.isSticky = isSticky;
        registerDefaultState(defaultBlockState().setValue(TOASTING, false).setValue(BlockStateProperties.POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TOASTING, BlockStateProperties.POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context)
                .setValue(TOASTING, false)
                .setValue(BlockStateProperties.POWERED, false)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getClockWise());
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof RedstoneToasterBlockEntity toaster)) {
            return InteractionResult.PASS;
        }

        ItemStack heldItem = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            return toaster.startToasting(player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        } else {
            if (!heldItem.isEmpty() && toaster.addItem(player.isCreative() ? heldItem.copy() : heldItem)) {
                level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.2F, 0.8F);
                return InteractionResult.SUCCESS;
            }
            return removeIngredient(toaster, level, pos, player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
    }

    private boolean removeIngredient(RedstoneToasterBlockEntity toaster, Level level, BlockPos pos, Player player) {
        ItemStack ingredient = toaster.removeItem();
        if (!ingredient.isEmpty()) {
            if (!player.isCreative()) {
                ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.75, pos.getZ() + 0.5, ingredient);
                item.setPickUpDelay(5);
                level.addFreshEntity(item);
            }
            level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.2F, 1F);
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        // start toasting when receiving a redstone signal
        if (blockEntity instanceof RedstoneToasterBlockEntity toaster) {
            toaster.startToasting();
        }
    }

    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        boolean isPowered = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
        boolean wasPowered = state.getValue(BlockStateProperties.POWERED);

        if (isPowered != wasPowered) {
            level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, isPowered), Block.UPDATE_INVISIBLE);
            if (isPowered && level.getBlockEntity(pos) instanceof RedstoneToasterBlockEntity toaster) {
                toaster.startToasting();
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() == Direction.Axis.X ? SHAPE_EAST_WEST : SHAPE_NORTH_SOUTH;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RedstoneToasterBlockEntity toaster) {
            return 8 * toaster.getAmountOfItems() - 1;
        }
        return 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!newState.is(state.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RedstoneToasterBlockEntity toaster) {
                toaster.dropItems();
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return RedstoneToasterBlockEntity.create(pos, state, isSticky);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        //noinspection unchecked
        return (BlockEntityTicker<T>) RedstoneToasterBlockEntity.TICKER;
    }
}
