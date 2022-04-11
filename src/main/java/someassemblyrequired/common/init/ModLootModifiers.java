package someassemblyrequired.common.init;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.loot.RollLootTableLootModifier;

public class ModLootModifiers {

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, SomeAssemblyRequired.MODID);

    public static final RegistryObject<GlobalLootModifierSerializer<RollLootTableLootModifier>> ROLL_LOOT_TABLE = REGISTRY.register("roll_loot_table", RollLootTableLootModifier.Serializer::new);
}
