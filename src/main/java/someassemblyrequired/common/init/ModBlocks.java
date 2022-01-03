package someassemblyrequired.common.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.block.LettuceBlock;
import someassemblyrequired.common.block.SandwichAssemblyTableBlock;
import someassemblyrequired.common.block.redstonetoaster.RedstoneToasterBlock;
import someassemblyrequired.common.block.sandwich.SandwichBlock;

public class ModBlocks {

    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, SomeAssemblyRequired.MODID);

    // misc blocks
    public static final RegistryObject<Block> SANDWICH = REGISTRY.register(
            "sandwich",
            () -> new SandwichBlock(
                    BlockBehaviour.Properties
                            .of(Material.DECORATION, MaterialColor.WOOD)
                            .strength(0.5F)
                            .noCollission()
                            .isRedstoneConductor((state, world, pos) -> false)
                            .sound(SoundType.WOOL)
            )
    );

    // sandwich assembly tables
    public static final RegistryObject<Block> OAK_SANDWICH_ASSEMBLY_TABLE = REGISTRY.register(
            "oak_sandwich_assembly_table",
            () -> createSandwichAssemblyTable(Material.WOOD)
    );

    public static final RegistryObject<Block> SPRUCE_SANDWICH_ASSEMBLY_TABLE = REGISTRY.register(
            "spruce_sandwich_assembly_table",
            () -> createSandwichAssemblyTable(Material.WOOD)
    );

    public static final RegistryObject<Block> BIRCH_SANDWICH_ASSEMBLY_TABLE = REGISTRY.register(
            "birch_sandwich_assembly_table",
            () -> createSandwichAssemblyTable(Material.WOOD)
    );

    public static final RegistryObject<Block> JUNGLE_SANDWICH_ASSEMBLY_TABLE = REGISTRY.register(
            "jungle_sandwich_assembly_table",
            () -> createSandwichAssemblyTable(Material.WOOD)
    );

    public static final RegistryObject<Block> ACACIA_SANDWICH_ASSEMBLY_TABLE = REGISTRY.register(
            "acacia_sandwich_assembly_table",
            () -> createSandwichAssemblyTable(Material.WOOD)
    );

    public static final RegistryObject<Block> DARK_OAK_SANDWICH_ASSEMBLY_TABLE = REGISTRY.register(
            "dark_oak_sandwich_assembly_table",
            () -> createSandwichAssemblyTable(Material.WOOD)
    );

    public static final RegistryObject<Block> CRIMSON_SANDWICH_ASSEMBLY_TABLE = REGISTRY.register(
            "crimson_sandwich_assembly_table",
            () -> createSandwichAssemblyTable(Material.NETHER_WOOD)
    );

    public static final RegistryObject<Block> WARPED_SANDWICH_ASSEMBLY_TABLE = REGISTRY.register(
            "warped_sandwich_assembly_table",
            () -> createSandwichAssemblyTable(Material.NETHER_WOOD)
    );

    // toasters
    public static final RegistryObject<Block> REDSTONE_TOASTER = REGISTRY.register(
            "redstone_toaster",
            () -> createToaster(false)
    );

    public static final RegistryObject<Block> STICKY_REDSTONE_TOASTER = REGISTRY.register(
            "sticky_redstone_toaster",
            () -> createToaster(true)
    );

    // crops
    public static final RegistryObject<Block> LETTUCE = REGISTRY.register(
            "lettuce",
            () -> new LettuceBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT))
    );

    public static Block[] getSandwichAssemblyTables() {
        return new Block[]{
                ModBlocks.OAK_SANDWICH_ASSEMBLY_TABLE.get(),
                ModBlocks.SPRUCE_SANDWICH_ASSEMBLY_TABLE.get(),
                ModBlocks.BIRCH_SANDWICH_ASSEMBLY_TABLE.get(),
                ModBlocks.JUNGLE_SANDWICH_ASSEMBLY_TABLE.get(),
                ModBlocks.ACACIA_SANDWICH_ASSEMBLY_TABLE.get(),
                ModBlocks.DARK_OAK_SANDWICH_ASSEMBLY_TABLE.get(),
                ModBlocks.CRIMSON_SANDWICH_ASSEMBLY_TABLE.get(),
                ModBlocks.WARPED_SANDWICH_ASSEMBLY_TABLE.get()
        };
    }

    public static Block[] getToasters() {
        return new Block[]{
                ModBlocks.REDSTONE_TOASTER.get(),
                ModBlocks.STICKY_REDSTONE_TOASTER.get()
        };
    }

    private static Block createSandwichAssemblyTable(Material material) {
        return new SandwichAssemblyTableBlock(
                BlockBehaviour.Properties
                        .of(material, MaterialColor.STONE)
                        .strength(2.5F)
                        .sound(SoundType.WOOD)
        );
    }

    private static Block createToaster(boolean isSticky) {
        return new RedstoneToasterBlock(BlockBehaviour.Properties
                .of(Material.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.5F),
                isSticky
        );
    }
}
