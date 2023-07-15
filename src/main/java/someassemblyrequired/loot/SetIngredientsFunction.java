package someassemblyrequired.loot;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.core.NonNullList;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.item.sandwich.SandwichItemHandler;
import someassemblyrequired.registry.ModLootFunctions;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class SetIngredientsFunction extends LootItemConditionalFunction {

    private final List<LootPoolEntryContainer> entries;

    protected SetIngredientsFunction(LootItemCondition[] conditions, List<LootPoolEntryContainer> entries) {
        super(conditions);
        this.entries = entries;
    }

    @Override
    protected ItemStack run(ItemStack item, LootContext context) {
        NonNullList<ItemStack> ingredients = NonNullList.create();

        entries.forEach(
                entry -> entry.expand(context,
                        (lootPoolEntry) -> lootPoolEntry.createItemStack(splitStacks(ingredients::add), context)
                )
        );

        for (ItemStack ingredient : ingredients) {
            if (ingredient.isEmpty() || ingredient.getCount() != 1) {
                SomeAssemblyRequired.LOGGER.warn("Tried to generate sandwich with invalid ingredient '{}', " +
                        "ingredients must have a stack size of 1", ingredient.toString());
                return ItemStack.EMPTY;
            }
        }

        SandwichItemHandler.get(item).ifPresent(sandwich -> sandwich.setItems(ingredients));

        return item;
    }

    private Consumer<ItemStack> splitStacks(Consumer<ItemStack> consumer) {
        return (stack) -> {
            if (stack.getCount() == 1) {
                consumer.accept(stack);
            } else if (stack.getCount() != 0) {
                int count = stack.getCount();
                stack.setCount(1);
                for (int i = 0; i < count; i++) {
                    consumer.accept(stack.copy());
                }
            }
        };
    }

    @Override
    public LootItemFunctionType getType() {
        return ModLootFunctions.SET_INGREDIENTS.get();
    }

    public static SetIngredientsFunction.Builder setIngredients() {
        return new SetIngredientsFunction.Builder();
    }

    public static class Builder extends LootItemConditionalFunction.Builder<SetIngredientsFunction.Builder> {

        private final List<LootPoolEntryContainer> entries = Lists.newArrayList();

        protected SetIngredientsFunction.Builder getThis() {
            return this;
        }

        public SetIngredientsFunction.Builder withEntry(LootPoolEntryContainer.Builder<?> entry) {
            entries.add(entry.build());
            return this;
        }

        public LootItemFunction build() {
            return new SetIngredientsFunction(getConditions(), entries);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetIngredientsFunction> {

        public void serialize(JsonObject object, SetIngredientsFunction function, JsonSerializationContext context) {
            super.serialize(object, function, context);
            object.add("entries", context.serialize(function.entries));
        }

        public SetIngredientsFunction deserialize(JsonObject object, JsonDeserializationContext context, LootItemCondition[] conditions) {
            LootPoolEntryContainer[] entries = GsonHelper.getAsObject(object, "entries", context, LootPoolEntryContainer[].class);
            return new SetIngredientsFunction(conditions, Arrays.asList(entries));
        }
    }
}
