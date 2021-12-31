package someassemblyrequired.common.loot;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.util.Util;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = SomeAssemblyRequired.MODID)
public class LootInjectHandler {

    public static final List<String> LOOT_TABLE_LOCATIONS = Arrays.asList(
            "chests/village/village_desert_house",
            "chests/village/village_plains_house",
            "chests/village/village_savanna_house",
            "chests/village/village_snowy_house",
            "chests/village/village_taiga_house",
            "chests/igloo_chest",
            "chests/shipwreck_supply"
    );

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        String prefix = "minecraft:";
        String name = event.getName().toString();

        if (name.startsWith(prefix)) {
            String location = name.substring(name.indexOf(prefix) + prefix.length());
            if (LOOT_TABLE_LOCATIONS.contains(location)) {
                SomeAssemblyRequired.LOGGER.debug("Adding loot to " + name);
                event.getTable().addPool(getInjectPool(location));
            }
        }
    }

    public static LootPool getInjectPool(String entryName) {
        return LootPool.lootPool()
                .add(getInjectEntry(entryName))
                .name("someassemblyrequired_inject")
                .build();
    }

    private static LootPoolEntryContainer.Builder<?> getInjectEntry(String name) {
        return LootTableReference.lootTableReference(Util.id("inject/" + name)).setWeight(1);
    }
}
