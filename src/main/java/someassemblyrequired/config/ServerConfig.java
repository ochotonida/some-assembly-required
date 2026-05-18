package someassemblyrequired.config;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.common.ModConfigSpec;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServerConfig {

    public final ModConfigSpec.IntValue maximumSandwichHeight;

    public final ModConfigSpec.BooleanValue generateChestLoot;

    public final ModConfigSpec.ConfigValue<String> sandwichBonusEffect;
    public final ModConfigSpec.ConfigValue<String> burgerBonusEffect;

    public final ModConfigSpec.ConfigValue<List<Integer>> sandwichEffectDurations;
    public final ModConfigSpec.ConfigValue<List<Integer>> burgerEffectDurations;

    ServerConfig(ModConfigSpec.Builder builder) {
        maximumSandwichHeight = builder
                .comment("The maximum amount of items a sandwich can contain")
                .translation(translate("maximum_sandwich_height"))
                .defineInRange("maximum_sandwich_height", 32, 2, 256);

        generateChestLoot = builder
                .comment("Whether randomly generated sandwiches should be added to chests in villages and some other locations")
                .translation(translate("generate_chest_loot"))
                .define("generate_chest_loot", true);

        sandwichBonusEffect = builder
                .comment("The effect applied by sandwiches, depending on the number of unique ingredients")
                .translation(translate("sandwich_bonus_effect"))
                .define("sandwich_effect", ResourceLocation.fromNamespaceAndPath(ModCompat.FARMERSDELIGHT, "nourishment").toString());
        burgerBonusEffect = builder
                .comment("The effect applied by burgers, depending on the number of unique ingredients")
                .translation(translate("burger_bonus_effect"))
                .define("burger_effect", ResourceLocation.fromNamespaceAndPath(ModCompat.FARMERSDELIGHT, "nourishment").toString());

        sandwichEffectDurations = builder
                .comment("The durations of the effect applied by sandwiches in seconds, depending on the number of unique ingredients")
                .translation(translate("sandwich_bonus_effect_durations"))
                .define("sandwich_effect_durations", new ArrayList<>(List.of(0, 0, 60, 120, 180, 240, 300)));
        burgerEffectDurations = builder
                .comment("The durations of the effect applied by burgers in seconds, depending on the number of unique ingredients")
                .translation(translate("burger_bonus_effect_durations"))
                .define("burger_effect_durations", new ArrayList<>(List.of(0, 0, 60, 120, 180, 240, 300)));
    }

    public Optional<Holder.Reference<MobEffect>> getSandwichBonusEffect(boolean burger) {
        String effectName = burger ? burgerBonusEffect.get() : sandwichBonusEffect.get();
        ResourceLocation effectId;
        try {
            effectId = ResourceLocation.parse(effectName);
        } catch (ResourceLocationException e) {
            return Optional.empty();
        }
        return BuiltInRegistries.MOB_EFFECT.getHolder(effectId);
    }

    private String translate(String name) {
        return "%s.config.server.%s".formatted(SomeAssemblyRequired.MOD_ID, name);
    }
}
