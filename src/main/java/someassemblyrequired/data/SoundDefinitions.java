package someassemblyrequired.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModSoundEvents;

public class SoundDefinitions extends SoundDefinitionsProvider {

    protected SoundDefinitions(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, SomeAssemblyRequired.MODID, helper);
    }

    @Override
    public void registerSounds() {
        add(ModSoundEvents.ADD_ITEM.get(), definition()
                .with(sound("dig/cloth1").pitch(1.5))
                .with(sound("dig/cloth2").pitch(1.5))
                .with(sound("dig/cloth3").pitch(1.5))
                .with(sound("dig/cloth4").pitch(1.5))
        );
        add(ModSoundEvents.ADD_ITEM_WET.get(), definition()
                .with(sound("entity/fish/flop1").pitch(0.9))
                .with(sound("entity/fish/flop2").pitch(0.9))
                .with(sound("entity/fish/flop3").pitch(0.9))
                .with(sound("entity/fish/flop4").pitch(0.9))
        );
        add(ModSoundEvents.ADD_ITEM_MOIST.get(), definition()
                .with(sound("block/honeyblock/break1").pitch(1.5))
                .with(sound("block/honeyblock/break2").pitch(1.5))
                .with(sound("block/honeyblock/break3").pitch(1.5))
                .with(sound("block/honeyblock/break4").pitch(1.5))
                .with(sound("block/honeyblock/break5").pitch(1.5))
        );
        add(ModSoundEvents.ADD_ITEM_SLIMY.get(), definition()
                .with(sound("item/ink_sac/ink_sac1").pitch(1.3))
                .with(sound("item/ink_sac/ink_sac2").pitch(1.3))
                .with(sound("item/ink_sac/ink_sac3").pitch(1.3))
        );
        add(ModSoundEvents.ADD_ITEM_LEAFY.get(), definition()
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
