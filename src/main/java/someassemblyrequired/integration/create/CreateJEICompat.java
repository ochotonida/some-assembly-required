package someassemblyrequired.integration.create;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeRegistration;
import someassemblyrequired.integration.ModCompat;

public class CreateJEICompat {

    private static final RecipeType<SequencedAssemblyRecipe> SEQUENCED_ASSEMBLY = RecipeType.create(ModCompat.CREATE, "sequenced_assembly", SequencedAssemblyRecipe.class);

    public static void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(SEQUENCED_ASSEMBLY, CreateCompat.createSandwichAssemblingRecipes());
    }
}
