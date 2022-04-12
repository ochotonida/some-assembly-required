package someassemblyrequired.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import someassemblyrequired.SomeAssemblyRequired;

public class JsonHelper {

    public static JsonObject writeItemStack(ItemStack stack) {
        JsonObject result = new JsonObject();

        // noinspection ConstantConditions
        result.addProperty("item", stack.getItem().getRegistryName().toString());
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
}
