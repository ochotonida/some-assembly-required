package someasseblyrequired.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import someasseblyrequired.common.block.tileentity.SandwichTileEntity;
import someasseblyrequired.common.init.Items;
import someasseblyrequired.common.init.TileEntityTypes;

import java.util.ArrayList;
import java.util.List;

public class SandwichBlock extends WaterLoggableHorizontalBlock {

    private static final VoxelShape SHAPE = Block.makeCuboidShape(4, 0, 4, 12, 8, 12);

    public SandwichBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        return !(world.getBlockState(pos.down()).getBlock() instanceof SandwichAssemblyTableBlock) && super.isValidPosition(state, world, pos);
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
    @SuppressWarnings("deprecation")
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = new ArrayList<>(super.getDrops(state, builder));
        TileEntity tileEntity = builder.get(LootParameters.BLOCK_ENTITY);
        if (tileEntity instanceof SandwichTileEntity) {
            ItemStack sandwich = new ItemStack(Items.SANDWICH);
            sandwich.getOrCreateChildTag("BlockEntityTag").put("Ingredients", tileEntity.write(new CompoundNBT()).getCompound("Ingredients"));
            drops.add(sandwich);
        }
        return drops;
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
