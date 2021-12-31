package someassemblyrequired.common.block.sandwich;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import someassemblyrequired.common.block.ItemHandlerBlockEntity;
import someassemblyrequired.common.init.ModBlockEntityTypes;

public class SandwichBlockEntity extends ItemHandlerBlockEntity {

    public SandwichBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.SANDWICH.get(), pos, state, 0);
    }

    @Override
    protected boolean canModifyItems() {
        return false;
    }
}
