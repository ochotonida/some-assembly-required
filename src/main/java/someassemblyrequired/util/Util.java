package someassemblyrequired.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import someassemblyrequired.SomeAssemblyRequired;

public class Util {

    public static ResourceLocation id(String path) {
        return new ResourceLocation(SomeAssemblyRequired.MODID, path);
    }

    public static MutableComponent translate(String key, Object... args) {
        return Component.translatable("%s.%s".formatted(SomeAssemblyRequired.MODID, key), args);
    }
}
