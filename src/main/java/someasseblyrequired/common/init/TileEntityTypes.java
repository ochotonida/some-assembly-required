package someasseblyrequired.common.init;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import someasseblyrequired.SomeAssemblyRequired;
import someasseblyrequired.client.SandwichAssemblyTableBlockRenderer;
import someasseblyrequired.client.SandwichBlockRenderer;
import someasseblyrequired.common.block.tileentity.SandwichAssemblyTableTileEntity;
import someasseblyrequired.common.block.tileentity.SandwichTileEntity;

@ObjectHolder(SomeAssemblyRequired.MODID)
public class TileEntityTypes {

    @ObjectHolder("sandwich_assembly_table")
    public static TileEntityType<SandwichAssemblyTableTileEntity> SANDWICH_ASSEMBLY_TABLE;
    @ObjectHolder("sandwich")
    public static TileEntityType<SandwichTileEntity> SANDWICH;

    public static void register(IForgeRegistry<TileEntityType<?>> registry) {
        // noinspection ConstantConditions
        registry.registerAll(
                TileEntityType.Builder.create(SandwichAssemblyTableTileEntity::new,
                        Blocks.ACACIA_SANDWICH_ASSEMBLY_TABLE,
                        Blocks.BIRCH_SANDWICH_ASSEMBLY_TABLE,
                        Blocks.CRIMSON_SANDWICH_ASSEMBLY_TABLE,
                        Blocks.DARK_OAK_SANDWICH_ASSEMBLY_TABLE,
                        Blocks.JUNGLE_SANDWICH_ASSEMBLY_TABLE,
                        Blocks.OAK_SANDWICH_ASSEMBLY_TABLE,
                        Blocks.SPRUCE_SANDWICH_ASSEMBLY_TABLE,
                        Blocks.WARPED_SANDWICH_ASSEMBLY_TABLE
                ).build(null).setRegistryName(SomeAssemblyRequired.MODID, "sandwich_assembly_table"),
                TileEntityType.Builder.create(() -> new SandwichTileEntity(SANDWICH), Blocks.SANDWICH).build(null).setRegistryName(SomeAssemblyRequired.MODID, "sandwich")
        );
    }

    public static void addRenderers() {
        ClientRegistry.bindTileEntityRenderer(TileEntityTypes.SANDWICH_ASSEMBLY_TABLE, SandwichAssemblyTableBlockRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypes.SANDWICH, SandwichBlockRenderer::new);
    }
}
