package someasseblyrequired.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import someasseblyrequired.common.init.RecipeTypes;

import javax.annotation.Nullable;

public class ToastingRecipe extends SingleIngredientRecipe {

    private final ItemStack result;

    public ToastingRecipe(ResourceLocation id, String group, Ingredient input, ItemStack result) {
        super(RecipeTypes.TOASTING, id, group, input);
        this.result = result;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inventory) {
        return result.copy();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result;
    }

    @Override
    public IRecipeSerializer<ToastingRecipe> getSerializer() {
        return RecipeTypes.TOASTING_SERIALIZER;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ToastingRecipe> {

        @Override
        public ToastingRecipe read(ResourceLocation id, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            return new ToastingRecipe(id, group, ingredient, result);
        }

        @Nullable
        @Override
        public ToastingRecipe read(ResourceLocation id, PacketBuffer buffer) {
            String group = buffer.readString(32767);
            Ingredient input = Ingredient.read(buffer);
            ItemStack result = buffer.readItemStack();

            return new ToastingRecipe(id, group, input, result);
        }

        @Override
        public void write(PacketBuffer buffer, ToastingRecipe recipe) {
            buffer.writeString(recipe.group);
            recipe.input.write(buffer);
            buffer.writeItemStack(recipe.result);
        }
    }
}
