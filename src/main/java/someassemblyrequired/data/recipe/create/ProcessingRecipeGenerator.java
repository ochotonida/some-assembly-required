package someassemblyrequired.data.recipe.create;

import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import someassemblyrequired.common.util.Util;
import someassemblyrequired.integration.ModCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class ProcessingRecipeGenerator extends ProcessingRecipeGen {

    private static final List<ProcessingRecipeGenerator> GENERATORS = new ArrayList<>();

    public static void registerAll(DataGenerator gen) {
        GENERATORS.add(new CuttingRecipeGenerator(gen));

        gen.addProvider(new DataProvider() {
            public String getName() {
                return "Processing Recipes";
            }

            public void run(HashCache cache) {
                GENERATORS.forEach((generator) -> {
                    try {
                        generator.run(cache);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                });
            }
        });
    }

    public ProcessingRecipeGenerator(DataGenerator generator) {
        super(generator);
    }

    protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(String namespace, Supplier<ItemLike> singleIngredient, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        ProcessingRecipeSerializer<T> serializer = getSerializer();
        GeneratedRecipe generatedRecipe = c -> {
            ItemLike iItemProvider = singleIngredient.get();
            // noinspection ConstantConditions
            transform.apply(
                    new ProcessingRecipeBuilder<>(
                            serializer.getFactory(),
                            new ResourceLocation(namespace, iItemProvider.asItem().getRegistryName().getPath())
                    ).whenModLoaded(ModCompat.CREATE).withItemIngredients(Ingredient.of(iItemProvider))
            ).build(c);
        };
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    protected <T extends ProcessingRecipe<?>> GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        ProcessingRecipeSerializer<T> serializer = getSerializer();
        GeneratedRecipe generatedRecipe = c -> transform
                .andThen(b -> b.whenModLoaded(ModCompat.CREATE))
                .apply(new ProcessingRecipeBuilder<>(serializer.getFactory(), name.get()))
                .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(ResourceLocation name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return createWithDeferredId(() -> name, transform);
    }

    protected abstract IRecipeTypeInfo getRecipeType();

    protected <T extends ProcessingRecipe<?>> ProcessingRecipeSerializer<T> getSerializer() {
        return getRecipeType().getSerializer();
    }

    protected Supplier<ResourceLocation> idWithSuffix(Supplier<ItemLike> item, String suffix) {
        return () -> {
            ResourceLocation registryName = item.get().asItem().getRegistryName();
            // noinspection ConstantConditions
            return Util.id(registryName.getPath() + suffix);
        };
    }

    @Override
    public String getName() {
        return "Processing Recipes: " + getRecipeType().getId().getPath();
    }
}
