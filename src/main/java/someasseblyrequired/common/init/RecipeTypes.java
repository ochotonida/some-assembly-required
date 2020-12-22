package someasseblyrequired.common.init;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import someasseblyrequired.SomeAssemblyRequired;
import someasseblyrequired.common.recipe.CuttingBoardRecipe;

public class RecipeTypes {

    public static final IRecipeType<CuttingBoardRecipe> CUTTING = IRecipeType.register(new ResourceLocation(SomeAssemblyRequired.MODID, "cutting").toString());
    public static final CuttingBoardRecipe.Serializer CUTTING_SERIALIZER = (CuttingBoardRecipe.Serializer) new CuttingBoardRecipe.Serializer().setRegistryName(new ResourceLocation(SomeAssemblyRequired.MODID, "cutting"));

    public static void register(IForgeRegistry<IRecipeSerializer<?>> registry) {
        registry.registerAll(
                CUTTING_SERIALIZER
        );
    }
}
