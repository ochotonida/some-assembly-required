package someassemblyrequired.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.block.SandwichBlockEntity;

@SuppressWarnings("ConstantConditions")
public class ModBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SomeAssemblyRequired.MOD_ID);

    public static final RegistryObject<BlockEntityType<SandwichBlockEntity>> SANDWICH = ENTITY_TYPES.register(
            "sandwich",
            () -> BlockEntityType.Builder.of(SandwichBlockEntity::new,
                    ModBlocks.SANDWICH.get()
            ).build(null)
    );
}
