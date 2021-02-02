package someassemblyrequired.common.init;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.util.Util;

public class ModSoundEvents {

    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SomeAssemblyRequired.MODID);

    public static final RegistryObject<SoundEvent> ADD_INGREDIENT = REGISTRY.register("block.sandwich_assembly_table.add_ingredient", () -> new SoundEvent(Util.prefix("block.sandwich_assembly_table.add_ingredient")));
    public static final RegistryObject<SoundEvent> ADD_SPREAD = REGISTRY.register("block.sandwich_assembly_table.add_spread", () -> new SoundEvent(Util.prefix("block.sandwich_assembly_table.add_spread")));

}
