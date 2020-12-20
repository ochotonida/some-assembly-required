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

@ObjectHolder(SomeAssemblyRequired.MODID)
public class Blocks {

    @ObjectHolder("sandwich")
    public static Block SANDWICH;
    @ObjectHolder("sandwich_assembly_table")
    public static Block SANDWICH_ASSEMBLY_TABLE;

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                new SandwichAssemblyTableBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.STONE).hardnessAndResistance(2.5F).sound(SoundType.WOOD)).setRegistryName(new ResourceLocation(SomeAssemblyRequired.MODID, "sandwich_assembly_table")),
                new SandwichBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS, MaterialColor.WOOD).hardnessAndResistance(0.5F).doesNotBlockMovement().setOpaque((state, world, pos) -> false).sound(SoundType.CLOTH).sound(SoundType.CLOTH)).setRegistryName(new ResourceLocation(SomeAssemblyRequired.MODID, "sandwich"))
        );
    }
}
