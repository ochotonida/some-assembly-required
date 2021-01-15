package someassemblyrequired.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import someassemblyrequired.common.init.Blocks;
import someassemblyrequired.common.init.Items;

public class TomatoBlock extends CropsBlock {

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.makeCuboidShape(4, -1, 4, 12, 14, 12),
            Block.makeCuboidShape(4, -1, 4, 12, 14, 12),
            Block.makeCuboidShape(3, -1, 3, 13, 14, 13),
            Block.makeCuboidShape(3, -1, 3, 13, 14, 13),
            Block.makeCuboidShape(2, -1, 2, 14, 14, 14),
            Block.makeCuboidShape(2, -1, 2, 14, 14, 14)
    };

    public TomatoBlock(Properties builder) {
        super(builder);
    }

    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        return Blocks.TOMATOES.get().getDefaultState();
    }

    @Override
    protected IItemProvider getSeedsItem() {
        return Items.TOMATO_SEEDS.get();
    }

    @Override
    public int getMaxAge() {
        return 5;
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return BlockStateProperties.AGE_0_5;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(getAgeProperty());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE_BY_AGE[getAge(state)];
    }
}
