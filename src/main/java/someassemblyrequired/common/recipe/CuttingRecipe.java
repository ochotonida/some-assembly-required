package someassemblyrequired.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
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

public class CuttingRecipe extends SingleIngredientRecipe {

    private final Ingredient tool;
    private final NonNullList<ItemStack> results;

    public CuttingRecipe(ResourceLocation id, String group, Ingredient input, Ingredient tool, NonNullList<ItemStack> results) {
        super(ModRecipeTypes.CUTTING, id, group, input);
        this.tool = tool;
        this.results = results;
    }

    @Override
    public ItemStack assemble(Container inventory) {
        return results.get(0).copy();
    }

    @Override
    public ItemStack getResultItem() {
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
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.CUTTING_SERIALIZER.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<CuttingRecipe> {

        @Override
        public CuttingRecipe fromJson(ResourceLocation id, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
            Ingredient tool = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "tool"));

            NonNullList<ItemStack> results = NonNullList.create();
            if (GsonHelper.isArrayNode(json, "result")) {
                for (JsonElement resultItem : GsonHelper.getAsJsonArray(json, "result")) {
                    results.add(ShapedRecipe.itemFromJson(resultItem.getAsJsonObject()));
                }
            } else {
                results.add(ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(json, "result")));
            }

            return new CuttingRecipe(id, group, ingredient, tool, results);
        }

        @Nullable
        @Override
        public CuttingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);
            Ingredient input = Ingredient.fromNetwork(buffer);
            Ingredient tool = Ingredient.fromNetwork(buffer);

            int size = buffer.readVarInt();
            NonNullList<ItemStack> results = NonNullList.create();
            for (int i = 0; i < size; ++i) {
                results.add(buffer.readItem());
            }

            return new CuttingRecipe(id, group, input, tool, results);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CuttingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            recipe.input.toNetwork(buffer);
            recipe.tool.toNetwork(buffer);

            buffer.writeVarInt(recipe.results.size());
            for (ItemStack result : recipe.results) {
                buffer.writeItem(result);
            }
        }
    }
}
