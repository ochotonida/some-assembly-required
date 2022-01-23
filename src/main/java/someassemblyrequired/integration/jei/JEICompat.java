package someassemblyrequired.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.util.Util;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.integration.create.CreateCompat;

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
            registration.addRecipes(CreateCompat.createSandwichAssemblingRecipes(registration.getIngredientManager()), SEQUENCED_ASSEMBLY);
        }
    }
}
