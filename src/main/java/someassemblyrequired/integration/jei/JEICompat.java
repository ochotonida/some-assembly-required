package someassemblyrequired.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModRecipeTypes;
import someassemblyrequired.common.util.Util;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
@SuppressWarnings("unused")
public class JEICompat implements IModPlugin {

    private static final ResourceLocation ID = Util.id("main");

    private static List<Recipe<?>> findRecipes(RecipeType<?> recipeType) {
        // noinspection ConstantConditions
        return Minecraft.getInstance().level.getRecipeManager().getRecipes().stream().filter(r -> r.getType() == recipeType).collect(Collectors.toList());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.useNbtForSubtypes(ModItems.SANDWICH.get());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ToastingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (Block toaster : ModBlocks.getToasters()) {
            registration.addRecipeCatalyst(new ItemStack(toaster), ToastingRecipeCategory.ID);
            registration.addRecipeCatalyst(new ItemStack(toaster), VanillaRecipeCategoryUid.SMOKING);
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(findRecipes(ModRecipeTypes.TOASTING), ToastingRecipeCategory.ID);

        for (Block assemblyTable : ModBlocks.getSandwichAssemblyTables()) {
            registration.addIngredientInfo(new ItemStack(assemblyTable), VanillaTypes.ITEM, new TranslatableComponent("description.%s.sandwich_assembly_table".formatted(SomeAssemblyRequired.MODID)));
        }
        for (ItemLike item : ModBlocks.getToasters()) {
            // noinspection ConstantConditions
            registration.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM, new TranslatableComponent(String.format("description.%s.%s", SomeAssemblyRequired.MODID, item.asItem().getRegistryName().getPath())));
        }
    }
}
