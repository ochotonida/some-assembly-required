package someassemblyrequired.data.builder;

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
import java.util.function.Consumer;

public class ToastingRecipeBuilder {

    private final Ingredient ingredient;
    private final Item result;

    private ToastingRecipeBuilder(Ingredient ingredient, ItemLike result) {
        this.ingredient = ingredient;
        this.result = result.asItem();
    }

    public static void addToastingRecipes(Consumer<FinishedRecipe> consumer) {
        ToastingRecipeBuilder.toastingRecipe(
                Ingredient.of(ModItems.BREAD_SLICE.get()),
                ModItems.TOASTED_BREAD_SLICE.get()
        ).build(consumer);

        ToastingRecipeBuilder.toastingRecipe(
                Ingredient.of(ModItems.TOASTED_BREAD_SLICE.get()),
                ModItems.CHARRED_BREAD_SLICE.get()
        ).build(consumer);

        ToastingRecipeBuilder.toastingRecipe(
                Ingredient.of(Items.CRIMSON_FUNGUS),
                ModItems.TOASTED_CRIMSON_FUNGUS.get()
        ).build(consumer);

        ToastingRecipeBuilder.toastingRecipe(
                Ingredient.of(Items.WARPED_FUNGUS),
                ModItems.TOASTED_WARPED_FUNGUS.get()
        ).build(consumer);

        ToastingRecipeBuilder.toastingRecipe(
                Ingredient.of(ModTags.BURNT_FOOD),
                Items.CHARCOAL
        ).build(consumer);
    }

    public static ToastingRecipeBuilder toastingRecipe(Ingredient ingredient, ItemLike result) {
        return new ToastingRecipeBuilder(ingredient, result);
    }

    public void build(Consumer<FinishedRecipe> consumer) {
        // noinspection ConstantConditions
        String name = result.getRegistryName().getPath();
        build(consumer, SomeAssemblyRequired.MODID + ":toasting/" + name);
    }

    public void build(Consumer<FinishedRecipe> consumer, String save) {
        build(consumer, new ResourceLocation(save));
    }

    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new ToastingRecipeBuilder.Result(id, ingredient, result));
    }

    public static class Result implements FinishedRecipe {

        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final Item result;

        public Result(ResourceLocation id, Ingredient ingredient, Item result) {
            this.id = id;
            this.ingredient = ingredient;
            this.result = result;
        }

        @Override
        public void serializeRecipeData(JsonObject object) {
            object.add("ingredient", ingredient.toJson());
            JsonObject resultObject = new JsonObject();
            // noinspection ConstantConditions
            resultObject.addProperty("item", result.getRegistryName().toString());
            object.add("result", resultObject);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipeTypes.TOASTING_SERIALIZER.get();
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
