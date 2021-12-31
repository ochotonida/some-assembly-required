package someassemblyrequired.common.util;

import net.minecraft.resources.ResourceLocation;
import someassemblyrequired.SomeAssemblyRequired;

public class Util {

    public static ResourceLocation id(String path) {
        return new ResourceLocation(SomeAssemblyRequired.MODID, path);
    }
}
