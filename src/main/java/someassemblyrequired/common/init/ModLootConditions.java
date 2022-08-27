package someassemblyrequired.common.init;

import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.loot.SandwichLootEnabledCondition;

public class ModLootConditions {

    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Registry.LOOT_ITEM_REGISTRY, SomeAssemblyRequired.MODID);

    public static final RegistryObject<LootItemConditionType> SANDWICH_LOOT_ENABLED = LOOT_CONDITION_TYPES.register("sandwich_loot_enabled", () -> new LootItemConditionType(new SandwichLootEnabledCondition.Serializer()));

}
