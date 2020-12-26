package someasseblyrequired.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public abstract class ConversionRecipe implements IRecipe<IInventory> {

    private final IRecipeType<?> type;
    private final ResourceLocation id;
    private final String group;
    private final Ingredient input;
    private final ItemStack result;

    public ConversionRecipe(IRecipeType<?> type, ResourceLocation id, String group, Ingredient input, ItemStack result) {
        this.type = type;
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
    public ItemStack getCraftingResult(IInventory inventory) {
        return result.copy();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result;
    }

    @Override
    public boolean matches(IInventory inventory, World world) {
        if (inventory.isEmpty())
            return false;
        return input.test(inventory.getStackInSlot(0));
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public IRecipeType<?> getType() {
        return type;
    }

    public static class Serializer<T extends ConversionRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ConversionRecipe> {

        private final Factory<T> factory;

        public Serializer(Factory<T> factory) {
            this.factory = factory;
        }

        @Override
        public ConversionRecipe read(ResourceLocation id, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            return factory.create(id, group, ingredient, result);
        }

        @Nullable
        @Override
        public ConversionRecipe read(ResourceLocation id, PacketBuffer buffer) {
            String group = buffer.readString(32767);
            Ingredient input = Ingredient.read(buffer);
            ItemStack result = buffer.readItemStack();

            return factory.create(id, group, input, result);
        }

        @Override
        public void write(PacketBuffer buffer, ConversionRecipe recipe) {
            buffer.writeString(recipe.group);
            recipe.input.write(buffer);
            buffer.writeItemStack(recipe.result);
        }

        public interface Factory<T extends ConversionRecipe> {
            T create(ResourceLocation id, String group, Ingredient input, ItemStack result);
        }
    }
}
