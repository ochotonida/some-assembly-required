package someassemblyrequired.data.builder;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModRecipeTypes;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ToastingRecipeBuilder {

    private final Ingredient ingredient;
    private final Item result;

    private ToastingRecipeBuilder(Ingredient ingredient, IItemProvider result) {
        this.ingredient = ingredient;
        this.result = result.asItem();
    }

    public static ToastingRecipeBuilder toastingRecipe(Ingredient ingredient, IItemProvider result) {
        return new ToastingRecipeBuilder(ingredient, result);
    }

    public void build(Consumer<IFinishedRecipe> consumer) {
        // noinspection ConstantConditions
        String name = result.getRegistryName().getPath();
        build(consumer, SomeAssemblyRequired.MODID + ":toasting/" + name);
    }

    public void build(Consumer<IFinishedRecipe> consumer, String save) {
        build(consumer, new ResourceLocation(save));
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new ToastingRecipeBuilder.Result(id, ingredient, result));
    }

    public static class Result implements IFinishedRecipe {

        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final Item result;

        public Result(ResourceLocation id, Ingredient ingredient, Item result) {
            this.id = id;
            this.ingredient = ingredient;
            this.result = result;
        }

        @Override
        public void serialize(JsonObject object) {
            object.add("ingredient", ingredient.serialize());
            JsonObject resultObject = new JsonObject();
            // noinspection ConstantConditions
            resultObject.addProperty("item", result.getRegistryName().toString());
            object.add("result", resultObject);
        }

        @Override
        public ResourceLocation getID() {
            return id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return ModRecipeTypes.TOASTING_SERIALIZER.get();
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
