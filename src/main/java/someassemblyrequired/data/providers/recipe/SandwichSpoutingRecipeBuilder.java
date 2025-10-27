package someassemblyrequired.data.providers.recipe;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.integration.create.recipe.SandwichFluidSpoutingRecipe;
import someassemblyrequired.integration.create.recipe.SandwichPotionSpoutingRecipe;

public class SandwichSpoutingRecipeBuilder {

    public static void addFillingRecipes(RecipeOutput output) {
        create(output, "water_bottle", PotionContents.createItemStack(Items.POTION, Potions.WATER), SizedFluidIngredient.of(Fluids.WATER, 250));
        create(output, Items.HONEY_BOTTLE, AllFluids.HONEY.get().getSource());
        create(output, AllItems.BUILDERS_TEA.get(), AllFluids.TEA.get().getSource());
        create(output, Items.MILK_BUCKET, NeoForgeMod.MILK.get());
        create(output, AllFluids.CHOCOLATE.get().getBucket(), AllFluids.CHOCOLATE.get().getSource());
        output.accept(SomeAssemblyRequired.id("sandwich_spouting/potion"), new SandwichPotionSpoutingRecipe(), null, new ModLoadedCondition(ModCompat.CREATE));
    }

    public static void create(RecipeOutput output, Item result, Fluid fluid) {
        create(output, new ItemStack(result), fluid, 250);
    }

    public static void create(RecipeOutput output, Item result, Fluid fluid, int amountRequired) {
        create(output, new ItemStack(result), fluid, amountRequired);
    }

    public static void create(RecipeOutput output, ItemStack result, Fluid fluid, int amountRequired) {
        create(output, BuiltInRegistries.ITEM.getKey(result.getItem()).getPath(), result, SizedFluidIngredient.of(fluid, amountRequired));
    }

    public static void create(RecipeOutput output, String name, ItemStack result, SizedFluidIngredient ingredient) {
        output.accept(id(name), new SandwichFluidSpoutingRecipe(ingredient, result), null, new ModLoadedCondition(ModCompat.CREATE));
    }

    private static ResourceLocation id(String id) {
        return SomeAssemblyRequired.id("sandwich_spouting/%s".formatted(id));
    }
}
