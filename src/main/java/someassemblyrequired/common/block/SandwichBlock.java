package someassemblyrequired.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import someassemblyrequired.common.block.tileentity.ItemHandlerTileEntity;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTileEntityTypes;

import java.util.ArrayList;
import java.util.List;

public class SandwichBlock extends WaterLoggableHorizontalBlock {

    private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 8, 12);

    public SandwichBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return !(world.getBlockState(pos.below()).getBlock() instanceof SandwichAssemblyTableBlock) && super.canSurvive(state, world, pos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public static ItemStack createSandwich(BlockEntity tileEntity) {
        if (!(tileEntity instanceof ItemHandlerTileEntity)) {
            return ItemStack.EMPTY;
        }

        ItemStack sandwich = new ItemStack(ModItems.SANDWICH.get());
        sandwich.getOrCreateTagElement("BlockEntityTag").put("Ingredients", tileEntity.save(new CompoundTag()).getCompound("Ingredients"));
        return sandwich;
    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return ModTileEntityTypes.SANDWICH.get().create();
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = new ArrayList<>(super.getDrops(state, builder));
        drops.add(createSandwich(builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY)));
        return drops;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return createSandwich(world.getBlockEntity(pos));
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
