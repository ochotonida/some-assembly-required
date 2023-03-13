package someassemblyrequired.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.block.SandwichBlock;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SomeAssemblyRequired.MODID);

    // misc blocks
    public static final RegistryObject<Block> SANDWICH = BLOCKS.register(
            "sandwich",
            () -> new SandwichBlock(
                    BlockBehaviour.Properties
                            .of(Material.DECORATION, MaterialColor.WOOD)
                            .strength(0.15F)
                            .noCollission()
                            .isRedstoneConductor((state, world, pos) -> false)
                            .sound(ModSoundTypes.SANDWICH_SOUND_TYPE)
            )
    );

    public static final RegistryObject<Block> SANDWICHING_STATION = BLOCKS.register(
            "sandwiching_station",
            () -> createSandwichAssemblyTable(Material.WOOD)
    );

    private static Block createSandwichAssemblyTable(Material material) {
        return new Block(
                BlockBehaviour.Properties
                        .of(material, MaterialColor.STONE)
                        .strength(2.5F)
                        .sound(SoundType.WOOD)
        );
    }
}
