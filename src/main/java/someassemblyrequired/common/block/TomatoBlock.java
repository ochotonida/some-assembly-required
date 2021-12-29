package someassemblyrequired.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;

public class TomatoBlock extends CropBlock {

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(4, -1, 4, 12, 14, 12),
            Block.box(4, -1, 4, 12, 14, 12),
            Block.box(3, -1, 3, 13, 14, 13),
            Block.box(3, -1, 3, 13, 14, 13),
            Block.box(2, -1, 2, 14, 14, 14),
            Block.box(2, -1, 2, 14, 14, 14)
    };

    public TomatoBlock(Properties builder) {
        super(builder);
    }

    @Override
    public BlockState getPlant(BlockGetter world, BlockPos pos) {
        return ModBlocks.TOMATOES.get().defaultBlockState();
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.TOMATO_SEEDS.get();
    }

    @Override
    public int getMaxAge() {
        return 5;
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return BlockStateProperties.AGE_5;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(getAgeProperty());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[getAge(state)];
    }
}
