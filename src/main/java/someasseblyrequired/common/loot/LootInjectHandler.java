package someasseblyrequired.common.loot;

import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import someasseblyrequired.SomeAssemblyRequired;

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
        return LootPool.builder()
                .addEntry(getInjectEntry(entryName))
                .name("someassemblyrequired_inject")
                .build();
    }

    private static LootEntry.Builder<?> getInjectEntry(String name) {
        ResourceLocation table = new ResourceLocation(SomeAssemblyRequired.MODID, "inject/" + name);
        return TableLootEntry.builder(table).weight(1);
    }
}
