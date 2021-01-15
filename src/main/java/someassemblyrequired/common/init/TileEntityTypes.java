package someassemblyrequired.common.init;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.client.CuttingBoardRenderer;
import someassemblyrequired.client.RedstoneToasterRenderer;
import someassemblyrequired.client.SandwichAssemblyTableBlockRenderer;
import someassemblyrequired.client.SandwichBlockRenderer;
import someassemblyrequired.common.block.tileentity.CuttingBoardTileEntity;
import someassemblyrequired.common.block.tileentity.RedstoneToasterTileEntity;
import someassemblyrequired.common.block.tileentity.SandwichAssemblyTableTileEntity;
import someassemblyrequired.common.block.tileentity.SandwichTileEntity;

@SuppressWarnings("ConstantConditions")
public class TileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SomeAssemblyRequired.MODID);

    public static RegistryObject<TileEntityType<?>> SANDWICH_ASSEMBLY_TABLE = REGISTRY.register(
            "sandwich_assembly_table",
            () -> TileEntityType.Builder.create(SandwichAssemblyTableTileEntity::new,
                    Blocks.getSandwichAssemblyTables()
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
                    Blocks.getCuttingBoards()
            ).build(null)
    );

    public static RegistryObject<TileEntityType<?>> REDSTONE_TOASTER = REGISTRY.register(
            "redstone_toaster",
            () -> TileEntityType.Builder.create(RedstoneToasterTileEntity::new,
                    Blocks.getToasters()
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
