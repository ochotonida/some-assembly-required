package someassemblyrequired.data.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModRecipeTypes;
import someassemblyrequired.common.init.ModTags;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CuttingRecipeBuilder {

    private final Ingredient ingredient;
    private final Ingredient tool;
    private final Map<Item, Integer> results = new LinkedHashMap<>();

    private CuttingRecipeBuilder(Ingredient ingredient, Ingredient tool) {
        this.ingredient = ingredient;
        this.tool = tool;
    }

    public static CuttingRecipeBuilder cuttingRecipe(Ingredient ingredient) {
        return cuttingRecipe(ingredient, Ingredient.fromTag(ModTags.TOOLS_KNIVES));
    }

    public static CuttingRecipeBuilder cuttingRecipe(Ingredient ingredient, Ingredient tool) {
        return new CuttingRecipeBuilder(ingredient, tool);
    }

    public CuttingRecipeBuilder addResult(IItemProvider result, int count) {
        results.put(result.asItem(), count);
        return this;
    }

    public void build(Consumer<IFinishedRecipe> consumer) {
        // noinspection ConstantConditions, OptionalGetWithoutIsPresent
        String name = results.keySet().stream().findFirst().get().getRegistryName().getPath();
        build(consumer, SomeAssemblyRequired.MODID + ":cutting/" + name);
    }

    public void build(Consumer<IFinishedRecipe> consumer, String save) {
        build(consumer, new ResourceLocation(save));
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new CuttingRecipeBuilder.Result(id, ingredient, tool, results));
    }

    public static class Result implements IFinishedRecipe {

        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final Ingredient tool;
        private final Map<Item, Integer> results;

        public Result(ResourceLocation id, Ingredient ingredient, Ingredient tool, Map<Item, Integer> results) {
            this.id = id;
            this.ingredient = ingredient;
            this.tool = tool;
            this.results = results;
        }

        @Override
        public void serialize(JsonObject object) {
            object.add("ingredient", ingredient.serialize());
            object.add("tool", tool.serialize());

            if (results.size() == 1) {
                object.add("result", serializeResult(results.entrySet().stream().findFirst().get()));
            } else {
                JsonArray array = new JsonArray();
                for (Map.Entry<Item, Integer> result : results.entrySet()) {
                    array.add(serializeResult(result));
                }
                object.add("results", array);
            }
        }

        private JsonObject serializeResult(Map.Entry<Item, Integer> result) {
            JsonObject object = new JsonObject();
            // noinspection ConstantConditions
            object.addProperty("item", result.getKey().getRegistryName().toString());
            if (result.getValue() > 1) {
                object.addProperty("count", result.getValue());
            }
            return object;
        }

        @Override
        public ResourceLocation getID() {
            return id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return ModRecipeTypes.CUTTING_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }
}
