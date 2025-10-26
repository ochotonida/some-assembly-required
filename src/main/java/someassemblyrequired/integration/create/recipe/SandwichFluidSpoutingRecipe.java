package someassemblyrequired.integration.create.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import someassemblyrequired.recipe.SandwichSpoutingRecipe;
import someassemblyrequired.registry.ModRecipeTypes;

public class SandwichFluidSpoutingRecipe extends SandwichSpoutingRecipe {

    private final SizedFluidIngredient ingredient;
    private final ItemStack result;

    public SandwichFluidSpoutingRecipe(SizedFluidIngredient ingredient, ItemStack result) {
        this.ingredient = ingredient;
        this.result = result;
    }

    public SizedFluidIngredient ingredient() {
        return ingredient;
    }

    public ItemStack result() {
        return result;
    }

    @Override
    public int getAmountRequired(FluidStack fluid) {
        return ingredient.amount();
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
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.SANDWICH_FLUID_SPOUTING_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<SandwichFluidSpoutingRecipe> {

        private static final MapCodec<SandwichFluidSpoutingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                SizedFluidIngredient.FLAT_CODEC.fieldOf("fluid").forGetter(SandwichFluidSpoutingRecipe::ingredient),
                ItemStack.SINGLE_ITEM_CODEC.fieldOf("result").forGetter(SandwichFluidSpoutingRecipe::result)
        ).apply(instance, SandwichFluidSpoutingRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, SandwichFluidSpoutingRecipe> STREAM_CODEC = StreamCodec.composite(
                SizedFluidIngredient.STREAM_CODEC,
                SandwichFluidSpoutingRecipe::ingredient,
                ItemStack.STREAM_CODEC,
                SandwichFluidSpoutingRecipe::result,
                SandwichFluidSpoutingRecipe::new
        );

        @Override
        public MapCodec<SandwichFluidSpoutingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SandwichFluidSpoutingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
