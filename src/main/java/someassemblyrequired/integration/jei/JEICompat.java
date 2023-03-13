package someassemblyrequired.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.init.ModBlocks;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.integration.create.CreateJEICompat;

import java.util.List;

@JeiPlugin
@SuppressWarnings("unused")
public class JEICompat implements IModPlugin {

    private static final ResourceLocation ID = SomeAssemblyRequired.id("main");

    public static final RecipeType<SandwichingStationCategory.Recipe> SANDWICHING_STATION = RecipeType.create(SomeAssemblyRequired.MODID, "sandwiching_station", SandwichingStationCategory.Recipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (ModCompat.isCreateLoaded()) {
            CreateJEICompat.registerRecipes(registration);
        }
        registration.addRecipes(SANDWICHING_STATION, List.of(new SandwichingStationCategory.Recipe()));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SandwichingStationCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SANDWICHING_STATION.get()), SANDWICHING_STATION);
    }
}
