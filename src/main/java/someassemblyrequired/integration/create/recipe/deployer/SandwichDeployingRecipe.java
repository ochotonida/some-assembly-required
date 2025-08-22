package someassemblyrequired.integration.create.recipe.deployer;

import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.config.ModConfig;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.item.sandwich.SandwichItemHandler;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModTags;

import java.util.Optional;

public class SandwichDeployingRecipe {

    private static final ResourceLocation RECIPE_ID = SomeAssemblyRequired.id("sandwich_deploying");

    public static Optional<DeployerApplicationRecipe> createRecipe(RecipeWrapper inventory) {
        if (!matches(inventory)) {
            return Optional.empty();
        }
        return Optional.of(createRecipe(inventory.getItem(0), inventory.getItem(1)));
    }

    public static boolean matches(RecipeWrapper inventory) {
        ItemStack item = inventory.getItem(0);

        if (item.isEmpty() || !item.is(ModTags.SANDWICH_BREAD) && !item.is(ModItems.SANDWICH.get())) {
            return false;
        }

        ItemStack ingredient = inventory.getItem(1);
        if (!Ingredients.canAddToSandwich(ingredient) || ingredient.is(ModItems.SANDWICH.get())) {
            return false;
        }

        return ModConfig.server.maximumSandwichHeight.get() >= Ingredients.getHeight(ingredient)
                + SandwichItemHandler.get(item)
                        .map(SandwichItemHandler::getTotalHeight)
                        .orElse(Ingredients.getHeight(item));
    }

    public static DeployerApplicationRecipe createRecipe(ItemStack sandwich, ItemStack ingredient) {
        sandwich = sandwich.copy();
        sandwich.setCount(1);
        ingredient = ingredient.copy();
        ingredient.setCount(1);

        ItemStack container = Ingredients.getContainer(ingredient);
        ItemStack result = SandwichItem.of(sandwich, ingredient);

        return new ProcessingRecipeBuilder<>(DeployerApplicationRecipe::new, RECIPE_ID)
                .withItemOutputs(
                        new ProcessingOutput(result, 1),
                        container.isEmpty() ? ProcessingOutput.EMPTY : new ProcessingOutput(container, 1)
                ).build();
    }
}
