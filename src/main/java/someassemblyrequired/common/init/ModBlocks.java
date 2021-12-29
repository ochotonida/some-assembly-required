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
import someassemblyrequired.common.block.*;

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

    // cutting boards
    public static final RegistryObject<Block> OAK_CUTTING_BOARD = REGISTRY.register(
            "oak_cutting_board",
            () -> createCuttingBoard(Material.WOOD, MaterialColor.WOOD, SoundType.WOOD)
    );

    public static final RegistryObject<Block> SPRUCE_CUTTING_BOARD = REGISTRY.register(
            "spruce_cutting_board",
            () -> createCuttingBoard(Material.WOOD, MaterialColor.PODZOL, SoundType.WOOD)
    );

    public static final RegistryObject<Block> BIRCH_CUTTING_BOARD = REGISTRY.register(
            "birch_cutting_board",
            () -> createCuttingBoard(Material.WOOD, MaterialColor.SAND, SoundType.WOOD)
    );

    public static final RegistryObject<Block> JUNGLE_CUTTING_BOARD = REGISTRY.register(
            "jungle_cutting_board",
            () -> createCuttingBoard(Material.WOOD, MaterialColor.DIRT, SoundType.WOOD)
    );

    public static final RegistryObject<Block> ACACIA_CUTTING_BOARD = REGISTRY.register(
            "acacia_cutting_board",
            () -> createCuttingBoard(Material.WOOD, MaterialColor.COLOR_ORANGE, SoundType.WOOD)
    );

    public static final RegistryObject<Block> DARK_OAK_CUTTING_BOARD = REGISTRY.register(
            "dark_oak_cutting_board",
            () -> createCuttingBoard(Material.WOOD, MaterialColor.COLOR_BROWN, SoundType.WOOD)
    );

    public static final RegistryObject<Block> CRIMSON_CUTTING_BOARD = REGISTRY.register(
            "crimson_cutting_board",
            () -> createCuttingBoard(Material.NETHER_WOOD, MaterialColor.CRIMSON_HYPHAE, SoundType.STEM)
    );

    public static final RegistryObject<Block> WARPED_CUTTING_BOARD = REGISTRY.register(
            "warped_cutting_board",
            () -> createCuttingBoard(Material.NETHER_WOOD, MaterialColor.WARPED_HYPHAE, SoundType.STEM)
    );

    // toasters
    public static final RegistryObject<Block> REDSTONE_TOASTER = REGISTRY.register(
            "redstone_toaster",
            () -> createToaster(true)
    );

    public static final RegistryObject<Block> STICKY_REDSTONE_TOASTER = REGISTRY.register(
            "sticky_redstone_toaster",
            () -> createToaster(false)
    );

    // crops
    public static final RegistryObject<Block> LETTUCE = REGISTRY.register(
            "lettuce",
            () -> new LettuceBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT))
    );

    public static final RegistryObject<Block> TOMATOES = REGISTRY.register(
            "tomatoes",
            () -> new TomatoBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT))
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

    public static Block[] getCuttingBoards() {
        return new Block[]{
                ModBlocks.OAK_CUTTING_BOARD.get(),
                ModBlocks.SPRUCE_CUTTING_BOARD.get(),
                ModBlocks.BIRCH_CUTTING_BOARD.get(),
                ModBlocks.JUNGLE_CUTTING_BOARD.get(),
                ModBlocks.ACACIA_CUTTING_BOARD.get(),
                ModBlocks.DARK_OAK_CUTTING_BOARD.get(),
                ModBlocks.CRIMSON_CUTTING_BOARD.get(),
                ModBlocks.WARPED_CUTTING_BOARD.get()
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

    private static Block createCuttingBoard(Material material, MaterialColor color, SoundType soundType) {
        return new CuttingBoardBlock(
                BlockBehaviour.Properties
                        .of(material, color)
                        .strength(2)
                        .sound(soundType)
        );
    }

    private static Block createToaster(boolean isEjectionToaster) {
        return new RedstoneToasterBlock(BlockBehaviour.Properties
                .of(Material.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.5F),
                isEjectionToaster);
    }
}
