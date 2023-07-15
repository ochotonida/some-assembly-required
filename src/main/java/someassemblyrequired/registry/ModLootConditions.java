package someassemblyrequired.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.loot.SandwichLootEnabledCondition;

public class ModLootConditions {

    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, SomeAssemblyRequired.MOD_ID);

    public static final RegistryObject<LootItemConditionType> SANDWICH_LOOT_ENABLED = LOOT_CONDITION_TYPES.register("sandwich_loot_enabled", () -> new LootItemConditionType(new SandwichLootEnabledCondition.Serializer()));

}
