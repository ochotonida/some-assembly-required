package someassemblyrequired.common.block.tileentity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import someassemblyrequired.common.init.ModTileEntityTypes;

public class SandwichTileEntity extends ItemHandlerTileEntity {

    public SandwichTileEntity() {
        // noinspection unchecked
        super((BlockEntityType<SandwichTileEntity>) ModTileEntityTypes.SANDWICH.get(), 0, false);
    }
}
