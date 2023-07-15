package someassemblyrequired.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;
import someassemblyrequired.config.ModConfig;
import someassemblyrequired.registry.ModLootConditions;

public class SandwichLootEnabledCondition implements LootItemCondition {

    private static final SandwichLootEnabledCondition INSTANCE = new SandwichLootEnabledCondition();

    @Override
    public LootItemConditionType getType() {
        return ModLootConditions.SANDWICH_LOOT_ENABLED.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        return ModConfig.server.generateChestLoot.get();
    }

    public static SandwichLootEnabledCondition sandwichLootEnabled() {
        return INSTANCE;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<SandwichLootEnabledCondition> {

        @Override
        public void serialize(JsonObject object, SandwichLootEnabledCondition condition, JsonSerializationContext context) {

        }

        @NotNull
        public SandwichLootEnabledCondition deserialize(JsonObject object, @NotNull JsonDeserializationContext context) {
            return new SandwichLootEnabledCondition();
        }
    }
}
