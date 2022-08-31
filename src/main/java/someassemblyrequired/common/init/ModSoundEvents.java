package someassemblyrequired.common.init;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.util.Util;

public class ModSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SomeAssemblyRequired.MODID);

    public static final RegistryObject<SoundEvent> ADD_ITEM = register("block.sandwich.add_item.generic");
    public static final RegistryObject<SoundEvent> ADD_ITEM_WET = register("block.sandwich.add_item.wet");
    public static final RegistryObject<SoundEvent> ADD_ITEM_MOIST = register("block.sandwich.add_item.moist");
    public static final RegistryObject<SoundEvent> ADD_ITEM_SLIMY = register("block.sandwich.add_item.slimy");
    public static final RegistryObject<SoundEvent> ADD_ITEM_LEAFY = register("block.sandwich.add_item.leafy");

    private static RegistryObject<SoundEvent> register(String id) {
        return SOUND_EVENTS.register(id, () -> new SoundEvent(Util.id(id)));
    }
}
