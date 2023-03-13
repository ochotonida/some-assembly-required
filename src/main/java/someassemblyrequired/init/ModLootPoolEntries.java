package someassemblyrequired.init;

import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.loot.OptionalLootItem;

public class ModLootPoolEntries {

    public static final DeferredRegister<LootPoolEntryType> LOOT_POOL_ENTRIES = DeferredRegister.create(Registry.LOOT_ENTRY_REGISTRY, SomeAssemblyRequired.MODID);

    public static final RegistryObject<LootPoolEntryType> OPTIONAL_ITEM = LOOT_POOL_ENTRIES.register("optional_item", () -> new LootPoolEntryType(new OptionalLootItem.Serializer()));
}
