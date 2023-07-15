package someassemblyrequired.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.registry.ModLootPoolEntries;
import someassemblyrequired.util.JsonHelper;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class OptionalLootItem extends LootPoolSingletonContainer {

    private final ResourceLocation item;
    private final List<ICondition> loadingConditions;

    OptionalLootItem(ResourceLocation item, List<ICondition> loadingConditions, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions) {
        super(weight, quality, conditions, functions);
        this.item = item;
        this.loadingConditions = loadingConditions;
    }

    public LootPoolEntryType getType() {
        return ModLootPoolEntries.OPTIONAL_ITEM.get();
    }

    @Override
    public boolean expand(LootContext context, Consumer<LootPoolEntry> entries) {
        if (!canRun(context) || !testConditions(loadingConditions)) {
            return false;
        }
        entries.accept(new LootPoolSingletonContainer.EntryBase() {
            public void createItemStack(Consumer<ItemStack> items, LootContext context1) {
                items.accept(new ItemStack(ForgeRegistries.ITEMS.getValue(item)));
            }
        });
        return true;
    }

    public static boolean testConditions(List<ICondition> conditions) {
        for (ICondition condition : conditions) {
            if (!condition.test(ICondition.IContext.EMPTY)) {
                return false;
            }
        }
        return true;
    }

    public void createItemStack(Consumer<ItemStack> pStackConsumer, LootContext pLootContext) {
        pStackConsumer.accept(new ItemStack(ForgeRegistries.ITEMS.getValue(item)));
    }

    public static LootPoolSingletonContainer.Builder<?> whenLoaded(Item item) {
        return whenLoaded(ForgeRegistries.ITEMS.getKey(item));
    }

    public static LootPoolSingletonContainer.Builder<?> whenLoaded(ResourceLocation item) {
        return optionalLootItem(item, new ModLoadedCondition(item.getNamespace()));
    }

    public static LootPoolSingletonContainer.Builder<?> optionalLootItem(ResourceLocation item, ICondition... loadingConditions) {
        return simpleBuilder((weight, quality, conditions, functions) -> new OptionalLootItem(item, Arrays.asList(loadingConditions), weight, quality, conditions, functions));
    }

    public static class Serializer extends LootPoolSingletonContainer.Serializer<OptionalLootItem> {

        public void serializeCustom(JsonObject object, OptionalLootItem lootItem, JsonSerializationContext context) {
            super.serializeCustom(object, lootItem, context);
            object.add("when", JsonHelper.serializeConditions(lootItem.loadingConditions));
            object.addProperty("name", lootItem.item.toString());
        }

        protected OptionalLootItem deserialize(JsonObject object, JsonDeserializationContext context, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions) {
            ResourceLocation item = new ResourceLocation(GsonHelper.getAsString(object, "name"));
            List<ICondition> loadingConditions = JsonHelper.deserializeConditions(object, "when");
            if (testConditions(loadingConditions) && !ForgeRegistries.ITEMS.containsKey(item)) {
                throw new JsonParseException("Could not find unknown item " + item);
            }
            return new OptionalLootItem(item, loadingConditions, weight, quality, conditions, functions);
        }
    }
}
