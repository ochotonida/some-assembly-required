package someassemblyrequired.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.loot.OptionalLootItem;

public class ModLootPoolEntries {

    public static final DeferredRegister<LootPoolEntryType> LOOT_POOL_ENTRY_TYPES = DeferredRegister.create(Registries.LOOT_POOL_ENTRY_TYPE, SomeAssemblyRequired.MODID);

    public static final RegistryObject<LootPoolEntryType> OPTIONAL_ITEM = LOOT_POOL_ENTRY_TYPES.register("optional_item", () -> new LootPoolEntryType(new OptionalLootItem.Serializer()));
}
