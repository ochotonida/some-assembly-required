package someassemblyrequired.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import someassemblyrequired.common.init.ModLootFunctions;
import someassemblyrequired.common.item.sandwich.SandwichItem;

public class CreateSandwichFunction extends LootItemConditionalFunction {

    protected CreateSandwichFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack item, LootContext context) {
        return SandwichItem.makeSandwich(item);
    }

    @Override
    public LootItemFunctionType getType() {
        return ModLootFunctions.CREATE_SANDWICH;
    }

    public static Builder<?> create() {
        return simpleBuilder(CreateSandwichFunction::new);
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<CreateSandwichFunction> {

        public CreateSandwichFunction deserialize(JsonObject object, JsonDeserializationContext context, LootItemCondition[] conditions) {
            return new CreateSandwichFunction(conditions);
        }
    }
}
