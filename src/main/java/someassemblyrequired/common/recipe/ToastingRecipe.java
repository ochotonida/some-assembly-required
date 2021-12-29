package someassemblyrequired.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;
import someassemblyrequired.common.init.ModRecipeTypes;

import javax.annotation.Nullable;

public class ToastingRecipe extends SingleIngredientRecipe {

    private final ItemStack result;

    public ToastingRecipe(ResourceLocation id, String group, Ingredient input, ItemStack result) {
        super(ModRecipeTypes.TOASTING, id, group, input);
        this.result = result;
    }

    @Override
    public ItemStack assemble(Container inventory) {
        return result.copy();
    }

    @Override
    public ItemStack getResultItem() {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.TOASTING_SERIALIZER.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ToastingRecipe> {

        @Override
        public ToastingRecipe fromJson(ResourceLocation id, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
            ItemStack result = ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new ToastingRecipe(id, group, ingredient, result);
        }

        @Nullable
        @Override
        public ToastingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);
            Ingredient input = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();

            return new ToastingRecipe(id, group, input, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ToastingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            recipe.input.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
