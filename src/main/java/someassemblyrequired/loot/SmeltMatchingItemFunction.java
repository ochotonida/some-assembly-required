package someassemblyrequired.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.init.ModLootFunctions;

import java.util.Optional;

public class SmeltMatchingItemFunction extends LootItemConditionalFunction {

    private final Item item;

    SmeltMatchingItemFunction(LootItemCondition[] conditions, Item item) {
        super(conditions);
        this.item = item;
    }

    public LootItemFunctionType getType() {
        return ModLootFunctions.SMELT_MATCHING_ITEM.get();
    }

    public ItemStack run(ItemStack stack, LootContext context) {
        if (!stack.isEmpty() && stack.getItem() == item) {
            Optional<SmeltingRecipe> optional = context.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), context.getLevel());
            if (optional.isPresent()) {
                ItemStack smelted = optional.get().getResultItem();
                if (!smelted.isEmpty()) {
                    ItemStack result = smelted.copy();
                    result.setCount(stack.getCount() * smelted.getCount());
                    return result;
                }
            }

            SomeAssemblyRequired.LOGGER.warn("Couldn't smelt {} because there is no smelting recipe", stack);
        }
        return stack;
    }

    public static LootItemConditionalFunction.Builder<?> smeltMatching(Item item) {
        return simpleBuilder(conditions -> new SmeltMatchingItemFunction(conditions, item));
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SmeltMatchingItemFunction> {

        public void serialize(JsonObject object, SmeltMatchingItemFunction function, JsonSerializationContext context) {
            super.serialize(object, function, context);
            // noinspection ConstantConditions
            object.addProperty("item", ForgeRegistries.ITEMS.getKey(function.item).toString());
        }

        public SmeltMatchingItemFunction deserialize(JsonObject object, JsonDeserializationContext context, LootItemCondition[] conditions) {
            Item item = GsonHelper.getAsItem(object, "item");
            return new SmeltMatchingItemFunction(conditions, item);
        }
    }
}
