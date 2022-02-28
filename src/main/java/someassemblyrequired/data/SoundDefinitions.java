package someassemblyrequired.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModSoundEvents;

public class SoundDefinitions extends SoundDefinitionsProvider {

    protected SoundDefinitions(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, SomeAssemblyRequired.MODID, helper);
    }

    @Override
    public void registerSounds() {
        add(ModSoundEvents.ADD_ITEM.get(), SoundDefinition.definition()
                .with(sound("dig/cloth1").pitch(1.5))
                .with(sound("dig/cloth2").pitch(1.5))
                .with(sound("dig/cloth3").pitch(1.5))
                .with(sound("dig/cloth4").pitch(1.5))
        );
        add(ModSoundEvents.ADD_SPREAD.get(), SoundDefinition.definition()
                .with(sound("block/honeyblock/break1").pitch(1.5))
                .with(sound("block/honeyblock/break2").pitch(1.5))
                .with(sound("block/honeyblock/break3").pitch(1.5))
                .with(sound("block/honeyblock/break4").pitch(1.5))
                .with(sound("block/honeyblock/break5").pitch(1.5))
        );
        add(ModSoundEvents.ADD_LEAVES.get(), SoundDefinition.definition()
                .with(sound("block/azalea_leaves/break1").volume(0.85))
                .with(sound("block/azalea_leaves/break2").volume(0.85))
                .with(sound("block/azalea_leaves/break3").volume(0.85))
                .with(sound("block/azalea_leaves/break4").volume(0.85))
                .with(sound("block/azalea_leaves/break5").volume(0.85))
                .with(sound("block/azalea_leaves/break6").volume(0.85))
                .with(sound("block/azalea_leaves/break7").volume(0.85))
        );
    }
}
