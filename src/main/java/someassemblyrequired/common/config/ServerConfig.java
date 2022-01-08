package someassemblyrequired.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import someassemblyrequired.SomeAssemblyRequired;

public class ServerConfig {

    public final ForgeConfigSpec.IntValue maximumSandwichHeight;

    ServerConfig(ForgeConfigSpec.Builder builder) {
        maximumSandwichHeight = builder
                .comment("The maximum amount of items a sandwich can contain")
                .translation(SomeAssemblyRequired.MODID + ".config.server.maximum_sandwich_height")
                .defineInRange("maximum_sandwich_height", 32, 2, Integer.MAX_VALUE);
    }
}
