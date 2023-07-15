package someassemblyrequired.registry;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.loot.RollLootTableModifier;

public class ModLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SomeAssemblyRequired.MOD_ID);

    public static final RegistryObject<Codec<RollLootTableModifier>> ROLL_LOOT_TABLE = LOOT_MODIFIERS.register("roll_loot_table", RollLootTableModifier.CODEC);
}
