package someassemblyrequired.data.providers.recipe.create;

import com.simibubi.create.api.data.recipe.BaseRecipeProvider;
import com.simibubi.create.api.data.recipe.CuttingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class CuttingRecipeGenerator extends CuttingRecipeGen {

    public CuttingRecipeGenerator(PackOutput packOutput) {
        super(packOutput, SomeAssemblyRequired.MOD_ID);

        cut(Items.APPLE, ModItems.APPLE_SLICES.get(), 2);
        cut(Items.BREAD, ModItems.BREAD_SLICE.get(), 4);
        cut(Items.BEETROOT, ModItems.CHOPPED_BEETROOT.get(), 2);
        cut(Items.CARROT, ModItems.CHOPPED_CARROT.get(), 2);
        cut(Items.GOLDEN_CARROT, ModItems.CHOPPED_GOLDEN_CARROT.get(), 2);
        cut(Items.ENCHANTED_GOLDEN_APPLE, ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(), 2);
        cut(Items.GOLDEN_APPLE, ModItems.GOLDEN_APPLE_SLICES.get(), 2);
        cut(ForgeTags.CROPS_TOMATO, ModItems.TOMATO_SLICES.get(), 2);
        cut(ForgeTags.CROPS_ONION, ModItems.SLICED_ONION.get(), 2);
        cut(ModItems.BURGER_BUN.get(), ModItems.BURGER_BUN_BOTTOM.get(), ModItems.BURGER_BUN_TOP.get());
    }

    private void cut(ItemLike input, ItemLike... results) {
        // noinspection ConstantConditions
        ResourceLocation id = SomeAssemblyRequired.id(ModCompat.CREATE + "/" + ForgeRegistries.ITEMS.getKey(input.asItem()).getPath());
        create(id, builder -> {
                    for (ItemLike item : results) {
                        builder.output(item);
                    }
                    return builder.whenModMissing(ModCompat.SLICE_AND_DICE)
                            .duration(30)
                            .withItemIngredients(Ingredient.of(input));
                }
        );
    }

    private void cut(ItemLike input, ItemLike result, int count) {
        cut(Ingredient.of(input), result, count);
    }

    private void cut(TagKey<Item> input, ItemLike result, int count) {
        cut(Ingredient.of(input), result, count);
    }

    private void cut(Ingredient input, ItemLike result, int count) {
        // noinspection ConstantConditions
        create(SomeAssemblyRequired.id(ModCompat.CREATE + "/" + ForgeRegistries.ITEMS.getKey(result.asItem()).getPath()), builder -> builder.whenModMissing(ModCompat.SLICE_AND_DICE).duration(30).output(result, count).withItemIngredients(input));
    }

    @Override
    protected <T extends ProcessingRecipe<?>> BaseRecipeProvider.GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        ProcessingRecipeSerializer<T> serializer = this.getSerializer();
        BaseRecipeProvider.GeneratedRecipe generatedRecipe = (c) -> {
            transform.apply(new ProcessingRecipeBuilder<>(serializer.getFactory(), name.get()))
                    .whenModLoaded(ModCompat.CREATE)
                    .build(c);
        };
        all.add(generatedRecipe);
        return generatedRecipe;
    }
}