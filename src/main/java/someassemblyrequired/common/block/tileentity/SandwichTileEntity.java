package someassemblyrequired.common.block.tileentity;

import net.minecraft.tileentity.TileEntityType;
import someassemblyrequired.common.init.ModTileEntityTypes;

public class SandwichTileEntity extends ItemHandlerTileEntity {

    public SandwichTileEntity() {
        // noinspection unchecked
        super((TileEntityType<SandwichTileEntity>) ModTileEntityTypes.SANDWICH.get(), 0, false);
    }
}
