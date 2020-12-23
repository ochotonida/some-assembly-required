package someasseblyrequired.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import someasseblyrequired.common.init.RecipeTypes;

import javax.annotation.Nullable;

public class CuttingRecipe implements IRecipe<RecipeWrapper> {

    private final ResourceLocation id;
    private final String group;
    private final Ingredient input;
    private final ItemStack result;

    public CuttingRecipe(ResourceLocation id, String group, Ingredient input, ItemStack result) {
        this.id = id;
        this.group = group;
        this.input = input;
        this.result = result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(input);
        return ingredients;
    }

    @Override
    public ItemStack getCraftingResult(RecipeWrapper inventory) {
        return result.copy();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result;
    }

    @Override
    public boolean matches(RecipeWrapper inventory, World world) {
        if (inventory.isEmpty())
            return false;
        return input.test(inventory.getStackInSlot(0));
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeTypes.CUTTING_SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeTypes.CUTTING;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CuttingRecipe> {

        private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
            NonNullList<Ingredient> ingredients = NonNullList.create();
            for (JsonElement ingredientElement : ingredientArray) {
                Ingredient ingredient = Ingredient.deserialize(ingredientElement);
                if (!ingredient.hasNoMatchingItems()) {
                    ingredients.add(ingredient);
                }
            }
            return ingredients;
        }

        @Override
        public CuttingRecipe read(ResourceLocation id, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            NonNullList<Ingredient> ingredients = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));

            if (ingredients.size() != 1) {
                throw new JsonParseException(String.format("Invalid number of input ingredients for cutting recipe: %s Please define only one ingredient.", ingredients.size()));
            } else {
                return new CuttingRecipe(id, group, ingredients.get(0), result);
            }
        }

        @Nullable
        @Override
        public CuttingRecipe read(ResourceLocation id, PacketBuffer buffer) {
            String group = buffer.readString(32767);
            Ingredient input = Ingredient.read(buffer);
            ItemStack result = buffer.readItemStack();

            return new CuttingRecipe(id, group, input, result);
        }

        @Override
        public void write(PacketBuffer buffer, CuttingRecipe recipe) {
            buffer.writeString(recipe.group);
            recipe.input.write(buffer);
            buffer.writeItemStack(recipe.result);
        }
    }
}
