package someasseblyrequired.common.init;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import someasseblyrequired.SomeAssemblyRequired;
import someasseblyrequired.common.recipe.CuttingRecipe;

public class RecipeTypes {

    public static final IRecipeType<CuttingRecipe> CUTTING = IRecipeType.register(new ResourceLocation(SomeAssemblyRequired.MODID, "cutting").toString());
    public static final CuttingRecipe.Serializer CUTTING_SERIALIZER = (CuttingRecipe.Serializer) new CuttingRecipe.Serializer().setRegistryName(new ResourceLocation(SomeAssemblyRequired.MODID, "cutting"));

    public static void register(IForgeRegistry<IRecipeSerializer<?>> registry) {
        registry.registerAll(
                CUTTING_SERIALIZER
        );
    }

    public static void registerBrewingRecipes() {
        BrewingRecipeRegistry.addRecipe(Ingredient.fromItems(net.minecraft.item.Items.GLASS_BOTTLE), Ingredient.fromItems(net.minecraft.item.Items.EGG), new ItemStack(Items.MAYONNAISE_BOTTLE));
    }
}
