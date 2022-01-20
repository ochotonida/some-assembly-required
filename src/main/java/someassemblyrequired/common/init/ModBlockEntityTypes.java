package someassemblyrequired.common.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.block.sandwich.SandwichBlockEntity;

@SuppressWarnings("ConstantConditions")
public class ModBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, SomeAssemblyRequired.MODID);

    public static RegistryObject<BlockEntityType<SandwichBlockEntity>> SANDWICH = REGISTRY.register(
            "sandwich",
            () -> BlockEntityType.Builder.of(SandwichBlockEntity::new,
                    ModBlocks.SANDWICH.get()
            ).build(null)
    );
}
