package someassemblyrequired.common.init;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.recipe.PotionToItemStackBrewingRecipe;
import someassemblyrequired.common.recipe.SingleIngredientRecipe;
import someassemblyrequired.common.recipe.ToastingRecipe;
import someassemblyrequired.common.util.Util;

public class ModRecipeTypes {

    public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SomeAssemblyRequired.MODID);

    public static final RecipeType<SingleIngredientRecipe> TOASTING = RecipeType.register(Util.id("toasting").toString());

    public static final RegistryObject<RecipeSerializer<?>> TOASTING_SERIALIZER = REGISTRY.register("toasting", ToastingRecipe.Serializer::new);

    public static void registerBrewingRecipes() {
        BrewingRecipeRegistry.addRecipe(
                new PotionToItemStackBrewingRecipe(
                        Potions.THICK,
                        Ingredient.of(Items.EGG),
                        new ItemStack(ModItems.MAYONNAISE_BOTTLE.get())
                )
        );
    }
}
