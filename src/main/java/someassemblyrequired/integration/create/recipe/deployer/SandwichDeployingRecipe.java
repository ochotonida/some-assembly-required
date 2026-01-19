package someassemblyrequired.integration.create.recipe.deployer;

import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.config.ModConfig;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModTags;

import java.util.Optional;

public class SandwichDeployingRecipe {

    private static final ResourceLocation RECIPE_ID = SomeAssemblyRequired.id("sandwich_deploying");

    public static Optional<RecipeHolder<DeployerApplicationRecipe>> createRecipe(RecipeWrapper inventory) {
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
                + SandwichContents.maybeGet(item)
                        .map(SandwichContents::getTotalHeight)
                        .orElse(Ingredients.getHeight(item));
    }

    public static RecipeHolder<DeployerApplicationRecipe> createRecipe(ItemStack sandwich, ItemStack ingredient) {
        sandwich = sandwich.copy();
        sandwich.setCount(1);
        ingredient = ingredient.copy();
        ingredient.setCount(1);

        // FIXME: inconsistent behavior:
        //  Create leaves crafting remainder in deployer,
        //  items without a crafting remainder are ejected instead
        ItemStack container = ingredient.hasCraftingRemainingItem()
                ? ItemStack.EMPTY // Crafting remainder is already handled by create
                : Ingredients.getFood(ingredient, null).usingConvertsTo().orElse(ItemStack.EMPTY);
        ItemStack result = SandwichItem.of(sandwich, ingredient);

        DeployerApplicationRecipe recipe = new ItemApplicationRecipe.Builder<>(DeployerApplicationRecipe::new, RECIPE_ID)
                .withItemOutputs(
                        new ProcessingOutput(result, 1),
                        container.isEmpty() ? ProcessingOutput.EMPTY : new ProcessingOutput(container, 1)
                ).build();

        return new RecipeHolder<>(RECIPE_ID, recipe);
    }
}
