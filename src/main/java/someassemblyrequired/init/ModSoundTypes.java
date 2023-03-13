package someassemblyrequired.init;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;

public class ModSoundTypes {

    public static final SoundType SANDWICH_SOUND_TYPE = new ForgeSoundType(1.0F, 1.5F, () -> SoundEvents.WOOL_BREAK, () -> SoundEvents.WOOL_STEP, () -> SoundEvents.WOOL_PLACE, () -> SoundEvents.WOOL_HIT, () -> SoundEvents.WOOL_FALL);

}
