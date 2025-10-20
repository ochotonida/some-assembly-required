package someassemblyrequired.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.item.sandwich.SandwichContents;

import java.util.Locale;

public class ModDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, SomeAssemblyRequired.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SandwichContents>> SANDWICH_CONTENTS = DATA_COMPONENT_TYPES.register("sandwich_contents", () -> new DataComponentType.Builder<SandwichContents>()
            .persistent(SandwichContents.CODEC)
            .networkSynchronized(SandwichContents.STREAM_CODEC)
            .cacheEncoding()
            .build()
    );

    private static final Codec<Integer> COLOR_CODEC = Codec.sizeLimitedString(9)
            .comapFlatMap(color -> {
                try {
                    int i = Integer.parseUnsignedInt(color.substring(1), 16);
                    if ((i & 0xFF000000) == 0x00000000) {
                        i |= 0xFF000000;
                    }
                    return DataResult.success(i);
                } catch (NumberFormatException e) {
                    return DataResult.error(() -> "Invalid color value: " + color);
                }
            }, i -> {
                if ((i & 0xFF000000) == 0xFF000000) {
                    i &= 0x00FFFFFF;
                    return String.format(Locale.ROOT, "#%06X", i);
                }
                return String.format(Locale.ROOT, "#%08X", i);
            });

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SPREAD_COLOR = DATA_COMPONENT_TYPES.register("spread_color", () -> new DataComponentType.Builder<Integer>()
            .persistent(Codec.withAlternative(COLOR_CODEC, Codec.INT))
            .networkSynchronized(ByteBufCodecs.VAR_INT)
            .build()
    );

}
