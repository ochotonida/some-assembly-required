package someasseblyrequired.common.init;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import someasseblyrequired.SomeAssemblyRequired;
import someasseblyrequired.client.CuttingBoardRenderer;
import someasseblyrequired.client.RedstoneToasterRenderer;
import someasseblyrequired.client.SandwichAssemblyTableBlockRenderer;
import someasseblyrequired.client.SandwichBlockRenderer;
import someasseblyrequired.common.block.tileentity.CuttingBoardTileEntity;
import someasseblyrequired.common.block.tileentity.RedstoneToasterTileEntity;
import someasseblyrequired.common.block.tileentity.SandwichAssemblyTableTileEntity;
import someasseblyrequired.common.block.tileentity.SandwichTileEntity;

@SuppressWarnings("ConstantConditions")
public class TileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SomeAssemblyRequired.MODID);

    public static RegistryObject<TileEntityType<?>> SANDWICH_ASSEMBLY_TABLE = REGISTRY.register(
            "sandwich_assembly_table",
            () -> TileEntityType.Builder.create(SandwichAssemblyTableTileEntity::new,
                    Blocks.ACACIA_SANDWICH_ASSEMBLY_TABLE.get(),
                    Blocks.BIRCH_SANDWICH_ASSEMBLY_TABLE.get(),
                    Blocks.CRIMSON_SANDWICH_ASSEMBLY_TABLE.get(),
                    Blocks.DARK_OAK_SANDWICH_ASSEMBLY_TABLE.get(),
                    Blocks.JUNGLE_SANDWICH_ASSEMBLY_TABLE.get(),
                    Blocks.OAK_SANDWICH_ASSEMBLY_TABLE.get(),
                    Blocks.SPRUCE_SANDWICH_ASSEMBLY_TABLE.get(),
                    Blocks.WARPED_SANDWICH_ASSEMBLY_TABLE.get()
            ).build(null)
    );

    public static RegistryObject<TileEntityType<?>> SANDWICH = REGISTRY.register(
            "sandwich",
            () -> TileEntityType.Builder.create(SandwichTileEntity::new,
                    Blocks.SANDWICH.get()
            ).build(null)
    );

    public static RegistryObject<TileEntityType<?>> CUTTING_BOARD = REGISTRY.register(
            "cutting_board",
            () -> TileEntityType.Builder.create(CuttingBoardTileEntity::new,
                    Blocks.ACACIA_CUTTING_BOARD.get(),
                    Blocks.BIRCH_CUTTING_BOARD.get(),
                    Blocks.CRIMSON_CUTTING_BOARD.get(),
                    Blocks.DARK_OAK_CUTTING_BOARD.get(),
                    Blocks.JUNGLE_CUTTING_BOARD.get(),
                    Blocks.OAK_CUTTING_BOARD.get(),
                    Blocks.SPRUCE_CUTTING_BOARD.get(),
                    Blocks.WARPED_CUTTING_BOARD.get()
            ).build(null)
    );

    public static RegistryObject<TileEntityType<?>> REDSTONE_TOASTER = REGISTRY.register(
            "redstone_toaster",
            () -> TileEntityType.Builder.create(RedstoneToasterTileEntity::new,
                    Blocks.STICKY_REDSTONE_TOASTER.get(),
                    Blocks.REDSTONE_TOASTER.get()
            ).build(null)
    );

    @SuppressWarnings("unchecked")
    public static void addRenderers() {
        ClientRegistry.bindTileEntityRenderer((TileEntityType<SandwichAssemblyTableTileEntity>) TileEntityTypes.SANDWICH_ASSEMBLY_TABLE.get(), SandwichAssemblyTableBlockRenderer::new);
        ClientRegistry.bindTileEntityRenderer((TileEntityType<CuttingBoardTileEntity>) TileEntityTypes.CUTTING_BOARD.get(), CuttingBoardRenderer::new);
        ClientRegistry.bindTileEntityRenderer((TileEntityType<SandwichTileEntity>) TileEntityTypes.SANDWICH.get(), SandwichBlockRenderer::new);
        ClientRegistry.bindTileEntityRenderer((TileEntityType<RedstoneToasterTileEntity>) TileEntityTypes.REDSTONE_TOASTER.get(), RedstoneToasterRenderer::new);
    }
}
