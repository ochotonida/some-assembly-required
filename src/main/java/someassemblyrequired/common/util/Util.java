package someassemblyrequired.common.util;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import someassemblyrequired.SomeAssemblyRequired;

public class Util {

    public static ResourceLocation id(String path) {
        return new ResourceLocation(SomeAssemblyRequired.MODID, path);
    }

    public static TranslatableComponent translate(String key, Object... args) {
        return new TranslatableComponent("%s.%s".formatted(SomeAssemblyRequired.MODID, key), args);
    }
}
