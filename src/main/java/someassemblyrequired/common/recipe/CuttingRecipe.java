package someassemblyrequired.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import someassemblyrequired.common.init.RecipeTypes;

import javax.annotation.Nullable;

public class CuttingRecipe extends SingleIngredientRecipe {

    private final Ingredient tool;
    private final NonNullList<ItemStack> results;

    public CuttingRecipe(ResourceLocation id, String group, Ingredient input, Ingredient tool, NonNullList<ItemStack> results) {
        super(RecipeTypes.CUTTING, id, group, input);
        this.tool = tool;
        this.results = results;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inventory) {
        return results.get(0).copy();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return results.get(0);
    }

    public NonNullList<ItemStack> getRecipeOutputs() {
        NonNullList<ItemStack> results = NonNullList.create();
        for (ItemStack stack : this.results) {
            results.add(stack.copy());
        }
        return results;
    }

    public Ingredient getTool() {
        return tool;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeTypes.CUTTING_SERIALIZER.get();
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CuttingRecipe> {

        @Override
        public CuttingRecipe read(ResourceLocation id, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
            Ingredient tool = Ingredient.deserialize(JSONUtils.getJsonObject(json, "tool"));

            NonNullList<ItemStack> results = NonNullList.create();
            if (JSONUtils.isJsonArray(json, "result")) {
                for (JsonElement resultItem : JSONUtils.getJsonArray(json, "result")) {
                    results.add(ShapedRecipe.deserializeItem(resultItem.getAsJsonObject()));
                }
            } else {
                results.add(ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result")));
            }

            return new CuttingRecipe(id, group, ingredient, tool, results);
        }

        @Nullable
        @Override
        public CuttingRecipe read(ResourceLocation id, PacketBuffer buffer) {
            String group = buffer.readString(32767);
            Ingredient input = Ingredient.read(buffer);
            Ingredient tool = Ingredient.read(buffer);

            int size = buffer.readVarInt();
            NonNullList<ItemStack> results = NonNullList.create();
            for (int i = 0; i < size; ++i) {
                results.add(buffer.readItemStack());
            }

            return new CuttingRecipe(id, group, input, tool, results);
        }

        @Override
        public void write(PacketBuffer buffer, CuttingRecipe recipe) {
            buffer.writeString(recipe.group);
            recipe.input.write(buffer);
            recipe.tool.write(buffer);

            buffer.writeVarInt(recipe.results.size());
            for (ItemStack result : recipe.results) {
                buffer.writeItemStack(result);
            }
        }
    }
}
