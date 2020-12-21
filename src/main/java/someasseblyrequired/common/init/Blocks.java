package someasseblyrequired.common.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import someasseblyrequired.SomeAssemblyRequired;
import someasseblyrequired.common.block.SandwichAssemblyTableBlock;
import someasseblyrequired.common.block.SandwichBlock;

import static net.minecraft.block.Blocks.AIR;

@ObjectHolder(SomeAssemblyRequired.MODID)
public class Blocks {

    public static final Block SANDWICH = AIR;
    public static final Block ACACIA_SANDWICH_ASSEMBLY_TABLE = AIR;
    public static final Block BIRCH_SANDWICH_ASSEMBLY_TABLE = AIR;
    public static final Block CRIMSON_SANDWICH_ASSEMBLY_TABLE = AIR;
    public static final Block DARK_OAK_SANDWICH_ASSEMBLY_TABLE = AIR;
    public static final Block JUNGLE_SANDWICH_ASSEMBLY_TABLE = AIR;
    public static final Block OAK_SANDWICH_ASSEMBLY_TABLE = AIR;
    public static final Block SPRUCE_SANDWICH_ASSEMBLY_TABLE = AIR;
    public static final Block WARPED_SANDWICH_ASSEMBLY_TABLE = AIR;


    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                new SandwichAssemblyTableBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.STONE).hardnessAndResistance(2.5F).sound(SoundType.WOOD)).setRegistryName(new ResourceLocation(SomeAssemblyRequired.MODID, "oak_sandwich_assembly_table")),
                new SandwichAssemblyTableBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.STONE).hardnessAndResistance(2.5F).sound(SoundType.WOOD)).setRegistryName(new ResourceLocation(SomeAssemblyRequired.MODID, "spruce_sandwich_assembly_table")),
                new SandwichAssemblyTableBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.STONE).hardnessAndResistance(2.5F).sound(SoundType.WOOD)).setRegistryName(new ResourceLocation(SomeAssemblyRequired.MODID, "birch_sandwich_assembly_table")),
                new SandwichAssemblyTableBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.STONE).hardnessAndResistance(2.5F).sound(SoundType.WOOD)).setRegistryName(new ResourceLocation(SomeAssemblyRequired.MODID, "jungle_sandwich_assembly_table")),
                new SandwichAssemblyTableBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.STONE).hardnessAndResistance(2.5F).sound(SoundType.WOOD)).setRegistryName(new ResourceLocation(SomeAssemblyRequired.MODID, "acacia_sandwich_assembly_table")),
                new SandwichAssemblyTableBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.STONE).hardnessAndResistance(2.5F).sound(SoundType.WOOD)).setRegistryName(new ResourceLocation(SomeAssemblyRequired.MODID, "dark_oak_sandwich_assembly_table")),
                new SandwichAssemblyTableBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.STONE).hardnessAndResistance(2.5F).sound(SoundType.WOOD)).setRegistryName(new ResourceLocation(SomeAssemblyRequired.MODID, "crimson_sandwich_assembly_table")),
                new SandwichAssemblyTableBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.STONE).hardnessAndResistance(2.5F).sound(SoundType.WOOD)).setRegistryName(new ResourceLocation(SomeAssemblyRequired.MODID, "warped_sandwich_assembly_table")),

                new SandwichBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, MaterialColor.WOOD).hardnessAndResistance(0.5F).doesNotBlockMovement().setOpaque((state, world, pos) -> false).sound(SoundType.CLOTH).sound(SoundType.CLOTH)).setRegistryName(new ResourceLocation(SomeAssemblyRequired.MODID, "sandwich"))
        );
    }
}
