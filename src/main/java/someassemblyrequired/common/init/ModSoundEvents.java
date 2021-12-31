package someassemblyrequired.common.init;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.util.Util;

public class ModSoundEvents {

    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SomeAssemblyRequired.MODID);

    public static final RegistryObject<SoundEvent> ADD_INGREDIENT = REGISTRY.register("block.sandwich_assembly_table.add_ingredient", () -> new SoundEvent(Util.id("block.sandwich_assembly_table.add_ingredient")));
    public static final RegistryObject<SoundEvent> ADD_SPREAD = REGISTRY.register("block.sandwich_assembly_table.add_spread", () -> new SoundEvent(Util.id("block.sandwich_assembly_table.add_spread")));

}
