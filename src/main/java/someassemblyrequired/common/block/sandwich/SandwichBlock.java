package someassemblyrequired.common.block.sandwich;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import someassemblyrequired.common.block.WaterLoggableHorizontalBlock;
import someassemblyrequired.common.ingredient.CustomIngredients;
import someassemblyrequired.common.init.ModBlockEntityTypes;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.item.sandwich.SandwichItemHandler;

import java.util.ArrayList;
import java.util.List;

public class SandwichBlock extends WaterLoggableHorizontalBlock implements EntityBlock {

    public static final IntegerProperty SIZE = IntegerProperty.create("size", 1, 8);

    private static final VoxelShape[] SHAPES = createShapes();

    public SandwichBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(SIZE, 1));
    }

    private static VoxelShape[] createShapes() {
        VoxelShape[] result = new VoxelShape[8];
        for (int i = 0; i < 8; i++) {
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
        builder.add(SIZE);
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        CompoundTag tag = context.getItemInHand().getTagElement("BlockEntityTag");
        if (tag != null) {
            int sandwichHeight = tag.getList("Sandwich", Tag.TAG_COMPOUND).size();
            return super.getStateForPlacement(context).setValue(SIZE, SandwichBlockEntity.getSizeFromHeight(sandwichHeight));
        }
        return super.getStateForPlacement(context);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        return level.getBlockEntity(pos, ModBlockEntityTypes.SANDWICH.get())
                .map(blockEntity -> blockEntity.interact(player, hand))
                .orElse(InteractionResult.FAIL);
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = new ArrayList<>(super.getDrops(state, builder));
        SandwichItemHandler.get(builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY))
                .ifPresent(sandwich -> {
                    if (sandwich.isValidSandwich()) {
                        drops.add(sandwich.getAsItem());
                    } else {
                        for (ItemStack stack : sandwich) {
                            if (!CustomIngredients.hasContainer(stack)) {
                                drops.add(stack);
                            }
                        }
                    }
                });
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
}
