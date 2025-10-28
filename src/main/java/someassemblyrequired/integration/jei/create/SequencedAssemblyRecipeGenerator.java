package someassemblyrequired.integration.jei.create;

import com.simibubi.create.compat.jei.CreateJEI;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IAdvancedRegistration;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fluids.FluidStack;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.integration.create.recipe.SandwichFluidSpoutingRecipe;
import someassemblyrequired.integration.jei.SandwichRecipeGenerator;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.item.sandwich.SandwichItemHandler;
import someassemblyrequired.recipe.SandwichSpoutingRecipe;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModRecipeTypes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SequencedAssemblyRecipeGenerator extends SandwichRecipeGenerator<SequencedAssemblyRecipe> {

    private static final RecipeType<SequencedAssemblyRecipe> SEQUENCED_ASSEMBLY = RecipeType.create(ModCompat.CREATE, "sequenced_assembly", SequencedAssemblyRecipe.class);

    public static void register(IAdvancedRegistration registration) {
        registration.addTypedRecipeManagerPlugin(SEQUENCED_ASSEMBLY, new SequencedAssemblyRecipeGenerator());
    }

    @Override
    protected int getMaxToppings() {
        return 6;
    }

    @Override
    protected SequencedAssemblyRecipe getRecipeForSandwich(ItemStack prefix, List<ItemStack> toppings, ItemStack result) {
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

        return recipe.build();
    }

    @Override
    protected List<SequencedAssemblyRecipe> getRecipesForBread(ItemStack bottomBread, ItemStack topBread) {
        List<SequencedAssemblyRecipe> recipes = super.getRecipesForBread(bottomBread, topBread);
        getSpoutingRecipes()
                .map(recipe -> recipe.assemble(FluidStack.EMPTY))
                .filter(item -> !item.is(Items.HONEY_BOTTLE)) // already included by super
                .map(filling -> SandwichItem.of(bottomBread, filling, topBread))
                .map(sandwich -> getRecipeForSandwich(SandwichItemHandler.get(sandwich).orElse(new SandwichItemHandler())))
                .forEach(recipes::add);
        ModCompat.EXAMPLE_POTIONS.stream()
                .filter(potion -> !potion.getEffects().isEmpty())
                .map(potion -> PotionUtils.setPotion(new ItemStack(Items.POTION), potion))
                .map(potion -> createRecipe(bottomBread, potion, topBread))
                .forEach(recipes::add);
        return recipes;
    }

    @Override
    protected Optional<ItemStack> getFillingFromFluid(FluidStack fluid) {
        for (Recipe<?> recipe : CreateJEI.getTypedRecipes(ModRecipeTypes.SANDWICH_SPOUTING.get())) {
            if (((SandwichSpoutingRecipe) recipe).matches(fluid)) {
                ItemStack result = ((SandwichSpoutingRecipe) recipe).assemble(fluid);
                if (!result.isEmpty()) {
                    return Optional.of(result);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    protected Optional<FluidIngredient> getFluidFromFilling(ItemStack filling) {
        if (filling.is(Items.POTION)) {
            int requiredAmount = PotionFluidHandler.getRequiredAmountForFilledBottle(null, null);
            return Optional.of(FluidIngredient.fromFluidStack(FluidHelper.copyStackWithAmount(PotionFluidHandler
                    .getFluidFromPotionItem(filling), requiredAmount)));
        }
        return getSpoutingRecipes()
                .filter(recipe -> ItemStack.isSameItem(recipe.getResultItem(RegistryAccess.EMPTY), filling))
                .map(SandwichFluidSpoutingRecipe::getIngredient)
                .findFirst();
    }

    private Stream<SandwichFluidSpoutingRecipe> getSpoutingRecipes() {
        return CreateJEI.getTypedRecipesExcluding(ModRecipeTypes.SANDWICH_SPOUTING.get(), recipe -> recipe.getSerializer() != ModRecipeTypes.SANDWICH_FLUID_SPOUTING_SERIALIZER.get())
                .stream()
                .map(recipe -> (SandwichFluidSpoutingRecipe) recipe);
    }
}
