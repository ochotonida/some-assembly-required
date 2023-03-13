package someassemblyrequired.integration.create.recipe;

import com.google.gson.JsonObject;
import com.simibubi.create.AllFluids;
import com.simibubi.create.content.contraptions.fluids.potion.PotionFluid;
import com.simibubi.create.content.contraptions.fluids.potion.PotionFluidHandler;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import someassemblyrequired.init.ModRecipeTypes;
import someassemblyrequired.recipe.SandwichSpoutingRecipe;

import javax.annotation.Nullable;

public class SandwichPotionSpoutingRecipe extends SandwichSpoutingRecipe {

    private static final ItemStack BOTTLE = new ItemStack(Items.GLASS_BOTTLE);

    public SandwichPotionSpoutingRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public int getAmountRequired(FluidStack fluid) {
        return PotionFluidHandler.getRequiredAmountForFilledBottle(BOTTLE, fluid);
    }

    @Override
    public boolean matches(FluidStack fluid) {
        if (!fluid.getFluid().isSame(AllFluids.POTION.get()) || fluid.getTag() == null) 
            return false;
        
        return NBTHelper.readEnum(fluid.getOrCreateTag(), "Bottle", PotionFluid.BottleType.class) == PotionFluid.BottleType.REGULAR;
    }

    @Override
    public ItemStack assemble(FluidStack fluid) {
        return PotionFluidHandler.fillBottle(BOTTLE.copy(), fluid.copy());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.SANDWICH_POTION_SPOUTING_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<SandwichPotionSpoutingRecipe> {

        @Override
        public SandwichPotionSpoutingRecipe fromJson(ResourceLocation id, JsonObject object) {
            return new SandwichPotionSpoutingRecipe(id);
        }

        @Nullable
        @Override
        public SandwichPotionSpoutingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new SandwichPotionSpoutingRecipe(id);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SandwichPotionSpoutingRecipe recipe) {

        }
    }
}
