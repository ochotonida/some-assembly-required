package someassemblyrequired.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import someassemblyrequired.item.sandwich.SandwichItemHandler;
import someassemblyrequired.registry.ModBlockEntityTypes;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModTags;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SandwichBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {

    public static final IntegerProperty SIZE = IntegerProperty.create("size", 1, 16);

    private static final VoxelShape[] SHAPES = createShapes();

    public SandwichBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(SIZE, 1)
        );
    }

    private static VoxelShape[] createShapes() {
        VoxelShape[] result = new VoxelShape[16];
        for (int i = 0; i < 16; i++) {
            result[i] = Block.box(4, 0, 4, 12, i + 1, 12);
        }
        return result;
    }

    public static ItemStack createSandwich(BlockEntity blockEntity) {
        if (!(blockEntity instanceof SandwichBlockEntity sandwichAssemblyTable)) {
            return ItemStack.EMPTY;
        }

        ItemStack sandwich = new ItemStack(ModItems.SANDWICH.get());
        sandwichAssemblyTable.saveAdditional(sandwich.getOrCreateTagElement("BlockEntityTag"));
        return sandwich;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.WATERLOGGED);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(SIZE);
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int size = SandwichItemHandler.get(context.getItemInHand())
                .map(SandwichBlock::getSizeFromSandwich)
                .orElse(1);
        boolean isWaterLogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;

        return defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, isWaterLogged)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection())
                .setValue(SIZE, size);
    }

    public static int getSizeFromSandwich(SandwichItemHandler sandwich) {
        int size = Math.min(32, Math.max(2, sandwich.getTotalHeight())) + 1;
        return size / 2;
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (level.getBlockState(pos.below()).is(ModTags.SANDWICHING_STATIONS)) {
            return level.getBlockEntity(pos, ModBlockEntityTypes.SANDWICH.get())
                    .map(blockEntity -> blockEntity.interact(player, hand))
                    .orElse(InteractionResult.FAIL);
        }
        return super.use(state, level, pos, player, hand, blockHitResult);
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = new ArrayList<>(super.getDrops(state, builder));
        SandwichItemHandler.get(builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY))
                .map(sandwich -> {
                    if (sandwich.getItemCount() != 1) {
                        return sandwich.getAsItem();
                    }
                    return sandwich.getStackInSlot(0);
                })
                .ifPresent(drops::add);
        return drops;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return createSandwich(world.getBlockEntity(pos));
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(SIZE) - 1];
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SandwichBlockEntity(pos, state);
    }

    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSupportCenter(level, pos.below(), Direction.UP);
    }

    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.DOWN && !this.canSurvive(state, level, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }
}
