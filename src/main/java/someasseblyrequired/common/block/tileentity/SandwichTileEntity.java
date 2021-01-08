package someasseblyrequired.common.block.tileentity;

import net.minecraft.tileentity.TileEntityType;
import someasseblyrequired.common.init.TileEntityTypes;

public class SandwichTileEntity extends ItemHandlerTileEntity {

    public SandwichTileEntity() {
        // noinspection unchecked
        super((TileEntityType<SandwichTileEntity>) TileEntityTypes.SANDWICH.get(), 0, false);
    }
}
