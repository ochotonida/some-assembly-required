package someassemblyrequired.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.util.Util;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.integration.create.CreateCompat;

import java.util.List;

@JeiPlugin
@SuppressWarnings("unused")
public class JEICompat implements IModPlugin {

    private static final ResourceLocation ID = Util.id("main");

    private static final ResourceLocation SEQUENCED_ASSEMBLY = new ResourceLocation(ModCompat.CREATE, "sequenced_assembly");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.useNbtForSubtypes(ModItems.SANDWICH.get());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (ModCompat.isCreateLoaded()) {
            registration.addRecipes(CreateCompat.createSandwichAssemblingRecipes(), SEQUENCED_ASSEMBLY);
        }
        registration.addRecipes(List.of(new SandwichAssemblyCategory.Recipe()), SandwichAssemblyCategory.ID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SandwichAssemblyCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SANDWICHING_STATION.get()), SandwichAssemblyCategory.ID);
    }
}
