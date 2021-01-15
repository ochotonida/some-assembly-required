package someassemblyrequired.common.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.block.*;

public class Blocks {

    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, SomeAssemblyRequired.MODID);

    // misc blocks
    public static final RegistryObject<Block> SANDWICH = REGISTRY.register(
            "sandwich",
            () -> new SandwichBlock(
                    AbstractBlock.Properties
                            .create(Material.MISCELLANEOUS, MaterialColor.WOOD)
                            .hardnessAndResistance(0.5F)
                            .doesNotBlockMovement()
                            .setOpaque((state, world, pos) -> false)
                            .sound(SoundType.CLOTH)
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
            () -> createCuttingBoard(Material.WOOD, MaterialColor.OBSIDIAN, SoundType.WOOD)
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
            () -> createCuttingBoard(Material.WOOD, MaterialColor.ADOBE, SoundType.WOOD)
    );

    public static final RegistryObject<Block> DARK_OAK_CUTTING_BOARD = REGISTRY.register(
            "dark_oak_cutting_board",
            () -> createCuttingBoard(Material.WOOD, MaterialColor.BROWN, SoundType.WOOD)
    );

    public static final RegistryObject<Block> CRIMSON_CUTTING_BOARD = REGISTRY.register(
            "crimson_cutting_board",
            () -> createCuttingBoard(Material.NETHER_WOOD, MaterialColor.CRIMSON_HYPHAE, SoundType.HYPHAE)
    );

    public static final RegistryObject<Block> WARPED_CUTTING_BOARD = REGISTRY.register(
            "warped_cutting_board",
            () -> createCuttingBoard(Material.NETHER_WOOD, MaterialColor.WARPED_HYPHAE, SoundType.HYPHAE)
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
            () -> new LettuceBlock(AbstractBlock.Properties.from(net.minecraft.block.Blocks.WHEAT))
    );

    public static final RegistryObject<Block> TOMATOES = REGISTRY.register(
            "tomatoes",
            () -> new TomatoBlock(AbstractBlock.Properties.from(net.minecraft.block.Blocks.WHEAT))
    );

    public static Block[] getSandwichAssemblyTables() {
        return new Block[]{
                Blocks.OAK_SANDWICH_ASSEMBLY_TABLE.get(),
                Blocks.SPRUCE_SANDWICH_ASSEMBLY_TABLE.get(),
                Blocks.BIRCH_SANDWICH_ASSEMBLY_TABLE.get(),
                Blocks.JUNGLE_SANDWICH_ASSEMBLY_TABLE.get(),
                Blocks.ACACIA_SANDWICH_ASSEMBLY_TABLE.get(),
                Blocks.DARK_OAK_SANDWICH_ASSEMBLY_TABLE.get(),
                Blocks.CRIMSON_SANDWICH_ASSEMBLY_TABLE.get(),
                Blocks.WARPED_SANDWICH_ASSEMBLY_TABLE.get()
        };
    }

    public static Block[] getCuttingBoards() {
        return new Block[]{
                Blocks.OAK_CUTTING_BOARD.get(),
                Blocks.SPRUCE_CUTTING_BOARD.get(),
                Blocks.BIRCH_CUTTING_BOARD.get(),
                Blocks.JUNGLE_CUTTING_BOARD.get(),
                Blocks.ACACIA_CUTTING_BOARD.get(),
                Blocks.DARK_OAK_CUTTING_BOARD.get(),
                Blocks.CRIMSON_CUTTING_BOARD.get(),
                Blocks.WARPED_CUTTING_BOARD.get()
        };
    }

    public static Block[] getToasters() {
        return new Block[]{
                Blocks.REDSTONE_TOASTER.get(),
                Blocks.STICKY_REDSTONE_TOASTER.get()
        };
    }

    private static Block createSandwichAssemblyTable(Material material) {
        return new SandwichAssemblyTableBlock(
                AbstractBlock.Properties
                        .create(material, MaterialColor.STONE)
                        .hardnessAndResistance(2.5F)
                        .sound(SoundType.WOOD)
        );
    }

    private static Block createCuttingBoard(Material material, MaterialColor color, SoundType soundType) {
        return new CuttingBoardBlock(
                AbstractBlock.Properties
                        .create(material, color)
                        .hardnessAndResistance(2)
                        .sound(soundType)
        );
    }

    private static Block createToaster(boolean isEjectionToaster) {
        return new RedstoneToasterBlock(AbstractBlock.Properties
                .create(Material.ROCK)
                .setRequiresTool()
                .hardnessAndResistance(3.5F),
                isEjectionToaster);
    }
}
