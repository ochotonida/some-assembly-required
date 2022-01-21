package someassemblyrequired.integration.create.recipe.deployer;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.processing.ProcessingOutput;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import someassemblyrequired.common.config.ModConfig;
import someassemblyrequired.common.ingredient.Ingredients;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;
import someassemblyrequired.common.item.sandwich.SandwichItemHandler;
import someassemblyrequired.common.util.Util;

import java.util.Optional;

public class SandwichDeployingRecipe extends ProcessingRecipe<RecipeWrapper> {

    private static final IRecipeTypeInfo TYPE_INFO = new TypeInfo();
    private static final ResourceLocation RECIPE_ID = Util.id("sandwich_deploying");

    public SandwichDeployingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(TYPE_INFO, params);
    }

    public static Optional<SandwichDeployingRecipe> createRecipe(RecipeWrapper inventory) {
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

        if (!Ingredients.canAddToSandwich(inventory.getItem(1))) {
            return false;
        }

        return SandwichItemHandler.get(item)
                .map(sandwich -> sandwich.size() < ModConfig.server.maximumSandwichHeight.get())
                .orElse(true);
    }

    public static SandwichDeployingRecipe createRecipe(ItemStack sandwich, ItemStack ingredient) {
        sandwich = sandwich.copy();
        sandwich.setCount(1);
        if (sandwich.is(ModTags.SANDWICH_BREAD)) {
            sandwich = new SandwichItemHandler(sandwich).getAsItem();
        }

        final ItemStack ingredient1 = ingredient.copy();
        ingredient1.setCount(1);

        ItemStack container = Ingredients.getContainer(ingredient);

        SandwichItemHandler.get(sandwich).ifPresent(handler -> handler.add(ingredient1));

        return new ProcessingRecipeBuilder<>(SandwichDeployingRecipe::new, RECIPE_ID)
                .withItemOutputs(
                        new ProcessingOutput(sandwich, 1),
                        container.isEmpty() ? ProcessingOutput.EMPTY : new ProcessingOutput(container, 1)
                )
                .build();
    }

    @Override
    public boolean matches(RecipeWrapper inventory, Level level) {
        return false;
    }

    @Override
    protected int getMaxInputCount() {
        return 2;
    }

    @Override
    protected int getMaxOutputCount() {
        return 2;
    }

    private static class TypeInfo implements IRecipeTypeInfo {

        @Override
        public ResourceLocation getId() {
            return null;
        }

        @Override
        public <T extends RecipeSerializer<?>> T getSerializer() {
            return null;
        }

        @Override
        public <T extends RecipeType<?>> T getType() {
            return AllRecipeTypes.DEPLOYING.getType();
        }
    }
}
