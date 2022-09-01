package someassemblyrequired.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;

import java.util.ArrayList;
import java.util.List;

public class JsonHelper {

    public static JsonObject writeItemStack(ItemStack stack) {
        JsonObject result = new JsonObject();

        // noinspection ConstantConditions
        result.addProperty("item", ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
        if (stack.getCount() != 1) {
            result.addProperty("count", stack.getCount());
        }
        if (stack.hasTag()) {
            result.add("nbt", writeNbt(stack.getTag()));
        }

        return result;
    }

    public static JsonElement writeNbt(CompoundTag tag) {
        return CompoundTag.CODEC
                .encodeStart(JsonOps.INSTANCE, tag)
                .resultOrPartial(SomeAssemblyRequired.LOGGER::error)
                .orElseThrow();
    }

    public static void addModLoadedCondition(JsonObject object, String modid) {
        JsonArray conditions;
        if (object.has("conditions")) {
            conditions = object.getAsJsonArray("conditions");
        } else {
            conditions = new JsonArray();
            object.add("conditions", conditions);
        }
        JsonObject condition1 = CraftingHelper.serialize(new ModLoadedCondition(modid));
        for (JsonElement condition : conditions) {
            if (condition.equals(condition1)) {
                return;
            }
        }
        conditions.add(condition1);
    }

    public static List<ICondition> deserializeConditions(JsonObject object, String memberName) {
        JsonArray conditions = GsonHelper.getAsJsonArray(object, memberName);
        List<ICondition> result = new ArrayList<>();
        for (JsonElement condition : conditions) {
            if (!condition.isJsonObject()) {
                throw new JsonSyntaxException("Conditions must be an array of JsonObjects");
            }

            result.add(CraftingHelper.getCondition(condition.getAsJsonObject()));
        }
        return result;
    }

    public static JsonElement serializeConditions(List<ICondition> conditions) {
        JsonArray result = new JsonArray();
        for (ICondition condition : conditions) {
            result.add(CraftingHelper.serialize(condition));
        }
        return result;
    }
}
