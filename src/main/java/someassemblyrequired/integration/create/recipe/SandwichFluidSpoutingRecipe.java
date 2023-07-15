package someassemblyrequired.integration.create.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import someassemblyrequired.recipe.SandwichSpoutingRecipe;
import someassemblyrequired.registry.ModRecipeTypes;

import javax.annotation.Nullable;

public class SandwichFluidSpoutingRecipe extends SandwichSpoutingRecipe {

    private final FluidIngredient ingredient;
    private final ItemStack result;

    public SandwichFluidSpoutingRecipe(ResourceLocation id, FluidIngredient ingredient, ItemStack result) {
        super(id);
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public int getAmountRequired(FluidStack fluid) {
        return ingredient.getRequiredAmount();
    }

    @Override
    public boolean matches(FluidStack fluid) {
        return ingredient.test(fluid);
    }

    @Override
    public ItemStack assemble(FluidStack fluid) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.SANDWICH_FLUID_SPOUTING_SERIALIZER.get();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return result;
    }

    public FluidIngredient getIngredient() {
        return ingredient;
    }

    public static class Serializer implements RecipeSerializer<SandwichFluidSpoutingRecipe> {

        @Override
        public SandwichFluidSpoutingRecipe fromJson(ResourceLocation id, JsonObject object) {
            if (!object.has("fluid")) {
                throw new JsonParseException("Missing 'fluid', expected to find fluid ingredient");
            }
            FluidIngredient ingredient = FluidIngredient.deserialize(object.get("fluid"));
            JsonObject resultObject = GsonHelper.getAsJsonObject(object, "result");
            ItemStack result = CraftingHelper.getItemStack(resultObject, true, true);
            if (result.isEmpty()) {
                throw new JsonParseException("Resulting item cannot be empty");
            }
            if (result.getCount() != 1) {
                throw new JsonParseException("Resulting item must have a count of 1");
            }
            return new SandwichFluidSpoutingRecipe(id, ingredient, result);
        }

        @Nullable
        @Override
        public SandwichFluidSpoutingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new SandwichFluidSpoutingRecipe(id, FluidIngredient.read(buffer), buffer.readItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SandwichFluidSpoutingRecipe recipe) {
            recipe.ingredient.write(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
