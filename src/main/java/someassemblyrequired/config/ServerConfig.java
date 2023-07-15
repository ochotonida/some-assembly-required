package someassemblyrequired.config;

import net.minecraftforge.common.ForgeConfigSpec;
import someassemblyrequired.SomeAssemblyRequired;

public class ServerConfig {

    public final ForgeConfigSpec.IntValue maximumSandwichHeight;

    public final ForgeConfigSpec.BooleanValue generateChestLoot;

    ServerConfig(ForgeConfigSpec.Builder builder) {
        maximumSandwichHeight = builder
                .comment("The maximum amount of items a sandwich can contain")
                .translation(SomeAssemblyRequired.MOD_ID + ".config.server.maximum_sandwich_height")
                .defineInRange("maximum_sandwich_height", 32, 2, Integer.MAX_VALUE);

        generateChestLoot = builder
                .comment("Whether randomly generated sandwiches should be added to chests in villages and some other locations")
                .translation(SomeAssemblyRequired.MOD_ID + ".config.server.generate_chest_loot")
                .define("generate_chest_loot", true);
    }
}
