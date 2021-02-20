package someassemblyrequired.common.init;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potions;
import net.minecraft.world.World;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.recipe.*;
import someassemblyrequired.common.util.Util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ModRecipeTypes {

    public static final DeferredRegister<IRecipeSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SomeAssemblyRequired.MODID);

    public static final IRecipeType<CuttingRecipe> CUTTING = IRecipeType.register(Util.prefix("cutting").toString());
    public static final IRecipeType<SingleIngredientRecipe> TOASTING = IRecipeType.register(Util.prefix("toasting").toString());
    public static final IRecipeType<SpreadType> SPREAD_TYPE = IRecipeType.register(Util.prefix("spread_type").toString());

    public static final RegistryObject<IRecipeSerializer<?>> CUTTING_SERIALIZER = REGISTRY.register("cutting", CuttingRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<?>> TOASTING_SERIALIZER = REGISTRY.register("toasting", ToastingRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<?>> SPREAD_TYPE_SERIALIZER = REGISTRY.register("spread_type", SpreadType.Serializer::new);

    @Nullable
    public static SpreadType getSpreadType(ItemStack stack, @Nonnull World world) {
        return world.getRecipeManager().getRecipe(SPREAD_TYPE, new Inventory(stack), world).orElse(null);
    }

    public static void registerBrewingRecipes() {
        BrewingRecipeRegistry.addRecipe(
                new PotionToItemStackBrewingRecipe(
                        Potions.THICK,
                        Ingredient.fromItems(Items.EGG),
                        new ItemStack(ModItems.MAYONNAISE_BOTTLE.get())
                )
        );
    }
}
