package someassemblyrequired.common.init;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.util.Util;

public class ModSoundEvents {

    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SomeAssemblyRequired.MODID);

    public static final RegistryObject<SoundEvent> ADD_ITEM = register("block.sandwich_assembly_table.add_item");
    public static final RegistryObject<SoundEvent> ADD_SPREAD = register("block.sandwich_assembly_table.add_spread");
    public static final RegistryObject<SoundEvent> ADD_LEAVES = register("block.sandwich_assembly_table.add_leaves");

    private static RegistryObject<SoundEvent> register(String id) {
        return REGISTRY.register(id, () -> new SoundEvent(Util.id(id)));
    }
}
