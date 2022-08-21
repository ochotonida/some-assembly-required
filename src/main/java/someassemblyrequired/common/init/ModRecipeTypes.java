package someassemblyrequired.common.init;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.recipe.SandwichSpoutingRecipe;
import someassemblyrequired.common.util.Util;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.integration.create.recipe.SandwichFluidSpoutingRecipe;
import someassemblyrequired.integration.create.recipe.SandwichPotionSpoutingRecipe;

public class ModRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, SomeAssemblyRequired.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SomeAssemblyRequired.MODID);

    public static final RegistryObject<RecipeType<SandwichSpoutingRecipe>> SANDWICH_SPOUTING = RECIPE_TYPES.register("sandwich_spouting", () -> RecipeType.simple(Util.id("sandwich_spouting")));

    public static final RegistryObject<RecipeSerializer<?>> SANDWICH_FLUID_SPOUTING_SERIALIZER = RECIPE_SERIALIZERS.register("sandwich_spouting", ModCompat.isCreateLoaded() ? SandwichFluidSpoutingRecipe.Serializer::new : SandwichSpoutingRecipe.EmptySerializer::new);
    public static final RegistryObject<RecipeSerializer<?>> SANDWICH_POTION_SPOUTING_SERIALIZER = RECIPE_SERIALIZERS.register("sandwich_potion_spouting", ModCompat.isCreateLoaded() ? SandwichPotionSpoutingRecipe.Serializer::new : SandwichSpoutingRecipe.EmptySerializer::new);
}
