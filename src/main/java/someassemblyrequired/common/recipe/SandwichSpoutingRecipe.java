package someassemblyrequired.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import someassemblyrequired.common.init.ModRecipeTypes;

import javax.annotation.Nullable;

public abstract class SandwichSpoutingRecipe implements Recipe<Container> {

    private final ResourceLocation id;

    public SandwichSpoutingRecipe(ResourceLocation id) {
        this.id = id;
    }

    public abstract int getAmountRequired(FluidStack fluid);

    public abstract boolean matches(FluidStack fluid);

    public abstract ItemStack assemble(FluidStack fluid);

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.SANDWICH_SPOUTING;
    }

    @Override
    public final boolean isSpecial() {
        return true;
    }

    @Override
    public final boolean matches(Container container, Level level) {
        return false;
    }

    @Override
    public final ItemStack assemble(Container container) {
        return null;
    }

    @Override
    public final boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return null;
    }

    public static class EmptySerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<SandwichSpoutingRecipe> {

        @Override
        public SandwichSpoutingRecipe fromJson(ResourceLocation id, JsonObject object) {
            return null;
        }

        @Nullable
        @Override
        public SandwichSpoutingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SandwichSpoutingRecipe recipe) {
            throw new UnsupportedOperationException();
        }
    }
}
