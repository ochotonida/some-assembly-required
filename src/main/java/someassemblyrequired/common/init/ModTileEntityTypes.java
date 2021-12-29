package someassemblyrequired.common.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
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
public class ModTileEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, SomeAssemblyRequired.MODID);

    public static RegistryObject<BlockEntityType<?>> SANDWICH_ASSEMBLY_TABLE = REGISTRY.register(
            "sandwich_assembly_table",
            () -> BlockEntityType.Builder.of(SandwichAssemblyTableTileEntity::new,
                    ModBlocks.getSandwichAssemblyTables()
            ).build(null)
    );

    public static RegistryObject<BlockEntityType<?>> SANDWICH = REGISTRY.register(
            "sandwich",
            () -> BlockEntityType.Builder.of(SandwichTileEntity::new,
                    ModBlocks.SANDWICH.get()
            ).build(null)
    );

    public static RegistryObject<BlockEntityType<?>> CUTTING_BOARD = REGISTRY.register(
            "cutting_board",
            () -> BlockEntityType.Builder.of(CuttingBoardTileEntity::new,
                    ModBlocks.getCuttingBoards()
            ).build(null)
    );

    public static RegistryObject<BlockEntityType<?>> REDSTONE_TOASTER = REGISTRY.register(
            "redstone_toaster",
            () -> BlockEntityType.Builder.of(RedstoneToasterTileEntity::new,
                    ModBlocks.getToasters()
            ).build(null)
    );

    @SuppressWarnings("unchecked")
    public static void addRenderers() {
        ClientRegistry.bindTileEntityRenderer((BlockEntityType<SandwichAssemblyTableTileEntity>) ModTileEntityTypes.SANDWICH_ASSEMBLY_TABLE.get(), SandwichAssemblyTableBlockRenderer::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType<CuttingBoardTileEntity>) ModTileEntityTypes.CUTTING_BOARD.get(), CuttingBoardRenderer::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType<SandwichTileEntity>) ModTileEntityTypes.SANDWICH.get(), SandwichBlockRenderer::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType<RedstoneToasterTileEntity>) ModTileEntityTypes.REDSTONE_TOASTER.get(), RedstoneToasterRenderer::new);
    }
}
