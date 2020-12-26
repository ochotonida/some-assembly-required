package someasseblyrequired.common.init;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import someasseblyrequired.SomeAssemblyRequired;
import someasseblyrequired.common.recipe.ConversionRecipe;
import someasseblyrequired.common.recipe.CuttingRecipe;
import someasseblyrequired.common.recipe.ToastingRecipe;

public class RecipeTypes {

    public static final IRecipeType<CuttingRecipe> CUTTING = IRecipeType.register(new ResourceLocation(SomeAssemblyRequired.MODID, "cutting").toString());
    public static final ConversionRecipe.Serializer<CuttingRecipe> CUTTING_SERIALIZER = new ConversionRecipe.Serializer<>(CuttingRecipe::new);
    public static final IRecipeType<ConversionRecipe> TOASTING = IRecipeType.register(new ResourceLocation(SomeAssemblyRequired.MODID, "toasting").toString());
    public static final ConversionRecipe.Serializer<ToastingRecipe> TOASTING_SERIALIZER = new ConversionRecipe.Serializer<>(ToastingRecipe::new);

    public static void register(IForgeRegistry<IRecipeSerializer<?>> registry) {
        registry.registerAll(
                CUTTING_SERIALIZER.setRegistryName(SomeAssemblyRequired.MODID, "cutting"),
                TOASTING_SERIALIZER.setRegistryName(SomeAssemblyRequired.MODID, "toasting")
        );
    }

    public static void registerBrewingRecipes() {
        BrewingRecipeRegistry.addRecipe(Ingredient.fromItems(net.minecraft.item.Items.GLASS_BOTTLE), Ingredient.fromItems(net.minecraft.item.Items.EGG), new ItemStack(Items.MAYONNAISE_BOTTLE));
    }
}
