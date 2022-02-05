package someassemblyrequired.common.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.block.SandwichAssemblyTableBlock;
import someassemblyrequired.common.block.sandwich.SandwichBlock;

public class ModBlocks {

    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, SomeAssemblyRequired.MODID);

    // misc blocks
    public static final RegistryObject<Block> SANDWICH = REGISTRY.register(
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

    public static final RegistryObject<Block> SANDWICHING_STATION = REGISTRY.register(
            "sandwiching_station",
            () -> createSandwichAssemblyTable(Material.WOOD)
    );

    private static Block createSandwichAssemblyTable(Material material) {
        return new SandwichAssemblyTableBlock(
                BlockBehaviour.Properties
                        .of(material, MaterialColor.STONE)
                        .strength(2.5F)
                        .sound(SoundType.WOOD)
        );
    }
}
