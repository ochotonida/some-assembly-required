package someassemblyrequired.common.block.sandwich;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import someassemblyrequired.common.block.WaterLoggableHorizontalBlock;
import someassemblyrequired.common.init.ModBlockEntityTypes;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.item.sandwich.SandwichItemHandler;

import java.util.ArrayList;
import java.util.List;

public class SandwichBlock extends WaterLoggableHorizontalBlock implements EntityBlock {

    // TODO height block state
    private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 8, 12);

    public SandwichBlock(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
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
                        sandwich.forEach(drops::add);
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
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SandwichBlockEntity(pos, state);
    }
}
