package someassemblyrequired.integration.jei.create;

import com.simibubi.create.Create;
import com.simibubi.create.compat.jei.CreateJEI;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IAdvancedRegistration;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.integration.create.recipe.SandwichFluidSpoutingRecipe;
import someassemblyrequired.integration.jei.SandwichRecipeGenerator;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.recipe.SandwichSpoutingRecipe;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModRecipeTypes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SequencedAssemblyRecipeGenerator extends SandwichRecipeGenerator<RecipeHolder<Recipe<?>>> {

    private static final RecipeType<RecipeHolder<Recipe<?>>> SEQUENCED_ASSEMBLY = RecipeType.createRecipeHolderType(Create.asResource("sequenced_assembly"));

    public static void register(IAdvancedRegistration registration) {
        registration.addTypedRecipeManagerPlugin(SEQUENCED_ASSEMBLY, new SequencedAssemblyRecipeGenerator());
    }

    @Override
    protected int getMaxToppings() {
        return 6;
    }

    @Override
    protected RecipeHolder<Recipe<?>> getRecipeForSandwich(ItemStack prefix, List<ItemStack> toppings, ItemStack result) {
        SequencedAssemblyRecipeBuilder recipe = new SequencedAssemblyRecipeBuilder(
                SomeAssemblyRequired.id("dynamic/sequenced_assembly"))
                .transitionTo(ModItems.SANDWICH.get())
                .loops(1)
                .addOutput(result, 1)
                .require(Ingredient.of(prefix));

        for (ItemStack input : toppings) {
            Optional<FluidIngredient> fluidIngredient = getFluidFromFilling(input);

            if (fluidIngredient.isPresent()) {
                recipe.addStep(FillingRecipe::new, builder -> builder.require(fluidIngredient.get()));
            } else {
                recipe.addStep(DeployerApplicationRecipe::new, builder -> builder.require(Ingredient.of(input)));
            }
        }

        var r = recipe.build();
        return new RecipeHolder<>(r.id(), r.value());
    }

    @Override
    protected List<RecipeHolder<Recipe<?>>> getRecipesForBread(ItemStack bottomBread, ItemStack topBread) {
        List<RecipeHolder<Recipe<?>>> recipes = super.getRecipesForBread(bottomBread, topBread);
        getSpoutingRecipes()
                .map(recipe -> recipe.assemble(FluidStack.EMPTY))
                .filter(item -> !item.is(Items.HONEY_BOTTLE)) // already included by super
                .map(filling -> SandwichItem.of(bottomBread, filling, topBread))
                .map(sandwich -> getRecipeForSandwich(SandwichContents.get(sandwich)))
                .forEach(recipes::add);
        ModCompat.EXAMPLE_POTIONS.stream()
                .filter(potion -> !potion.value().getEffects().isEmpty())
                .map(potion -> PotionContents.createItemStack(Items.POTION, potion))
                .map(potion -> createRecipe(bottomBread, potion, topBread))
                .forEach(recipes::add);
        return recipes;
    }

    @Override
    protected ItemStack getFillingFromFluid(FluidStack fluid) {
        for (RecipeHolder<?> recipe : CreateJEI.getTypedRecipes(ModRecipeTypes.SANDWICH_SPOUTING.get())) {
            if (((SandwichSpoutingRecipe) recipe.value()).matches(fluid)) {
                return ((SandwichSpoutingRecipe) recipe.value()).assemble(fluid);
            }
        }
        throw new IllegalArgumentException("Unsupported fluid: [%s]".formatted(fluid.toString()));
    }

    @Override
    protected Optional<FluidIngredient> getFluidFromFilling(ItemStack filling) {
        if (filling.is(Items.POTION)) {
            int requiredAmount = PotionFluidHandler.getRequiredAmountForFilledBottle(null, null);
            return Optional.of(FluidIngredient.fromFluidStack(FluidHelper.copyStackWithAmount(PotionFluidHandler
                    .getFluidFromPotionItem(filling), requiredAmount)));
        }
        return getSpoutingRecipes()
                .filter(recipe -> ItemStack.isSameItemSameComponents(recipe.getResultItem(RegistryAccess.EMPTY), filling))
                .map(SandwichFluidSpoutingRecipe::ingredient)
                .findFirst();
    }

    private Stream<SandwichFluidSpoutingRecipe> getSpoutingRecipes() {
        return CreateJEI.getTypedRecipesExcluding(ModRecipeTypes.SANDWICH_SPOUTING.get(), recipe -> recipe.value().getSerializer() != ModRecipeTypes.SANDWICH_FLUID_SPOUTING_SERIALIZER.get())
                .stream()
                .map(RecipeHolder::value)
                .map(recipe -> (SandwichFluidSpoutingRecipe) recipe);
    }
}
