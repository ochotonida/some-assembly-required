package someassemblyrequired.data.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModItems;
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

    public static void addCuttingRecipes(Consumer<FinishedRecipe> consumer) {
        CuttingRecipeBuilder.cuttingRecipe(Ingredient.of(Items.APPLE))
                .addResult(ModItems.APPLE_SLICES.get(), 2)
                .build(consumer);

        CuttingRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BREAD))
                .addResult(ModItems.BREAD_SLICE.get(), 4)
                .build(consumer);

        CuttingRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BEETROOT))
                .addResult(ModItems.CHOPPED_BEETROOT.get(), 2)
                .build(consumer);

        CuttingRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CARROT))
                .addResult(ModItems.CHOPPED_CARROT.get(), 2)
                .build(consumer);

        CuttingRecipeBuilder.cuttingRecipe(Ingredient.of(Items.GOLDEN_CARROT))
                .addResult(ModItems.CHOPPED_GOLDEN_CARROT.get(), 2)
                .build(consumer);

        CuttingRecipeBuilder.cuttingRecipe(Ingredient.of(Items.ENCHANTED_GOLDEN_APPLE))
                .addResult(ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(), 2)
                .build(consumer);

        CuttingRecipeBuilder.cuttingRecipe(Ingredient.of(Items.GOLDEN_APPLE))
                .addResult(ModItems.GOLDEN_APPLE_SLICES.get(), 2)
                .build(consumer);

        CuttingRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.LETTUCE_HEAD.get()))
                .addResult(ModItems.LETTUCE_LEAF.get(), 3)
                .build(consumer);

        CuttingRecipeBuilder.cuttingRecipe(Ingredient.of(Items.MELON))
                .addResult(Items.MELON_SLICE, 9)
                .build(consumer);

        CuttingRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PORKCHOP))
                .addResult(ModItems.PORK_CUTS.get(), 3)
                .build(consumer);

        CuttingRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.TOASTED_CRIMSON_FUNGUS.get()))
                .addResult(ModItems.SLICED_TOASTED_CRIMSON_FUNGUS.get(), 2)
                .build(consumer);

        CuttingRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.TOASTED_WARPED_FUNGUS.get()))
                .addResult(ModItems.SLICED_TOASTED_WARPED_FUNGUS.get(), 2)
                .build(consumer);

        CuttingRecipeBuilder.cuttingRecipe(Ingredient.of(ModTags.CROPS_TOMATOES))
                .addResult(ModItems.TOMATO_SLICES.get(), 2)
                .build(consumer, SomeAssemblyRequired.MODID + ":cutting/tomato_slices");
    }

    public static CuttingRecipeBuilder cuttingRecipe(Ingredient ingredient) {
        return cuttingRecipe(ingredient, Ingredient.of(ModTags.TOOLS_KNIVES));
    }

    public static CuttingRecipeBuilder cuttingRecipe(Ingredient ingredient, Ingredient tool) {
        return new CuttingRecipeBuilder(ingredient, tool);
    }

    public CuttingRecipeBuilder addResult(ItemLike result, int count) {
        results.put(result.asItem(), count);
        return this;
    }

    public void build(Consumer<FinishedRecipe> consumer) {
        // noinspection ConstantConditions, OptionalGetWithoutIsPresent
        String name = results.keySet().stream().findFirst().get().getRegistryName().getPath();
        build(consumer, SomeAssemblyRequired.MODID + ":cutting/" + name);
    }

    public void build(Consumer<FinishedRecipe> consumer, String save) {
        build(consumer, new ResourceLocation(save));
    }

    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new CuttingRecipeBuilder.Result(id, ingredient, tool, results));
    }

    public static class Result implements FinishedRecipe {

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
        public void serializeRecipeData(JsonObject object) {
            object.add("ingredient", ingredient.toJson());
            object.add("tool", tool.toJson());

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
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipeTypes.CUTTING_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
