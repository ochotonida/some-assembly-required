package someassemblyrequired.data.providers.recipe.create;

import com.simibubi.create.api.data.recipe.CuttingRecipeGen;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModTags;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class CuttingRecipeGenerator extends CuttingRecipeGen {

    public CuttingRecipeGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, SomeAssemblyRequired.MOD_ID);

        cut(Items.APPLE, ModItems.APPLE_SLICES.get(), 2);
        cut(Items.BREAD, ModItems.BREAD_SLICE.get(), 4);
        cut(Items.BEETROOT, ModItems.CHOPPED_BEETROOT.get(), 2);
        cut(Items.CARROT, ModItems.CHOPPED_CARROT.get(), 2);
        cut(Items.GOLDEN_CARROT, ModItems.CHOPPED_GOLDEN_CARROT.get(), 2);
        cut(Items.ENCHANTED_GOLDEN_APPLE, ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(), 2);
        cut(Items.GOLDEN_APPLE, ModItems.GOLDEN_APPLE_SLICES.get(), 2);
        cut(CommonTags.Items.CROPS_TOMATO, ModItems.TOMATO_SLICES.get(), 2);
        cut(CommonTags.Items.CROPS_ONION, ModItems.SLICED_ONION.get(), 2);
        cut(ModItems.BURGER_BUN.get(), ModItems.BURGER_BUN_BOTTOM.get(), 1, ModItems.BURGER_BUN_TOP.get());

        farmersDelightCutting();
    }

    private void farmersDelightCutting() {
        cuttingAnimalItems();
        cuttingVegetables();
        cuttingFoods();
    }

    private void cuttingAnimalItems() {
        cut(Items.BEEF, vectorwing.farmersdelight.common.registry.ModItems.MINCED_BEEF.get(), 2);
        cut(Items.PORKCHOP, vectorwing.farmersdelight.common.registry.ModItems.BACON.get(), 2);
        cut(Items.CHICKEN, vectorwing.farmersdelight.common.registry.ModItems.CHICKEN_CUTS.get(), 2, Items.BONE_MEAL);
        cut(Items.COOKED_CHICKEN, vectorwing.farmersdelight.common.registry.ModItems.COOKED_CHICKEN_CUTS.get(), 2, Items.BONE_MEAL);
        cut(Items.COD, vectorwing.farmersdelight.common.registry.ModItems.COD_SLICE.get(), 2, Items.BONE_MEAL);
        cut(Items.COOKED_COD, vectorwing.farmersdelight.common.registry.ModItems.COOKED_COD_SLICE.get(), 2, Items.BONE_MEAL);
        cut(Items.SALMON, vectorwing.farmersdelight.common.registry.ModItems.SALMON_SLICE.get(), 2, Items.BONE_MEAL);
        cut(Items.COOKED_SALMON, vectorwing.farmersdelight.common.registry.ModItems.COOKED_SALMON_SLICE.get(), 2, Items.BONE_MEAL);
        cut(vectorwing.farmersdelight.common.registry.ModItems.HAM.get(), Items.PORKCHOP, 2, Items.BONE);
        cut(vectorwing.farmersdelight.common.registry.ModItems.SMOKED_HAM.get(), Items.COOKED_PORKCHOP, 2, Items.BONE);
        cut(Items.MUTTON, vectorwing.farmersdelight.common.registry.ModItems.MUTTON_CHOPS.get(), 2);
        cut(Items.COOKED_MUTTON, vectorwing.farmersdelight.common.registry.ModItems.COOKED_MUTTON_CHOPS.get(), 2);
    }

    private static void cuttingVegetables() {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.MELON), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), Items.MELON_SLICE, 9);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PUMPKIN), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), vectorwing.farmersdelight.common.registry.ModItems.PUMPKIN_SLICE.get(), 4);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(vectorwing.farmersdelight.common.registry.ModItems.BROWN_MUSHROOM_COLONY.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), Items.BROWN_MUSHROOM, 5);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(vectorwing.farmersdelight.common.registry.ModItems.RED_MUSHROOM_COLONY.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIFE), Items.RED_MUSHROOM, 5);
    }

    private void cuttingFoods() {
        create(SomeAssemblyRequired.id(ModCompat.CREATE + "/" + "dough"), builder -> {
            builder.output(vectorwing.farmersdelight.common.registry.ModItems.RAW_PASTA.get());

            return builder.whenModMissing(ModCompat.SLICE_AND_DICE)
                    .duration(30)
                    .withItemIngredients(Ingredient.fromValues(Stream.of(new Ingredient.TagValue(ModTags.DOUGH), new Ingredient.TagValue(ModTags.DOUGHS))));
        });
        cut(vectorwing.farmersdelight.common.registry.ModItems.KELP_ROLL.get(), vectorwing.farmersdelight.common.registry.ModItems.KELP_ROLL_SLICE.get(), 3);
        cut(Items.CAKE, vectorwing.farmersdelight.common.registry.ModItems.CAKE_SLICE.get(), 7);
        cut(vectorwing.farmersdelight.common.registry.ModItems.APPLE_PIE.get(), vectorwing.farmersdelight.common.registry.ModItems.APPLE_PIE_SLICE.get(), 4);
        cut(vectorwing.farmersdelight.common.registry.ModItems.SWEET_BERRY_CHEESECAKE.get(), vectorwing.farmersdelight.common.registry.ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get(), 4);
        cut(vectorwing.farmersdelight.common.registry.ModItems.CHOCOLATE_PIE.get(), vectorwing.farmersdelight.common.registry.ModItems.CHOCOLATE_PIE_SLICE.get(), 4);
    }

    private void cut(ItemLike input, ItemLike result, int count, ItemLike... extraResults) {
        // noinspection ConstantConditions
        ResourceLocation id = SomeAssemblyRequired.id(ModCompat.CREATE + "/" + BuiltInRegistries.ITEM.getKey(input.asItem()).getPath());
        create(id, builder -> {
            builder.output(result, count);
            if (BuiltInRegistries.ITEM.getKey(input.asItem()).getNamespace().equals(ModCompat.FARMERSDELIGHT)
                    || BuiltInRegistries.ITEM.getKey(result.asItem()).getNamespace().equals(ModCompat.FARMERSDELIGHT)) {
                builder.whenModLoaded(ModCompat.FARMERSDELIGHT);
            }

            for (ItemLike item : extraResults) {
                builder.output(item);
            }

            return builder.whenModMissing(ModCompat.SLICE_AND_DICE)
                    .duration(30)
                    .withItemIngredients(Ingredient.of(input));
        });
    }

    private void cut(ItemLike input, ItemLike result, int count) {
        cut(Ingredient.of(input), result, count);
    }

    private void cut(TagKey<Item> input, ItemLike result, int count) {
        cut(Ingredient.of(input), result, count);
    }

    private void cut(Ingredient input, ItemLike result, int count) {
        create(SomeAssemblyRequired.id(ModCompat.CREATE + "/" + BuiltInRegistries.ITEM.getKey(result.asItem()).getPath()), builder -> {
            if (Stream.concat(Arrays.stream(input.getItems()).map(ItemStack::getItem), Stream.of(result))
                    .map(ItemLike::asItem)
                    .map(BuiltInRegistries.ITEM::getKey)
                    .map(ResourceLocation::getNamespace)
                    .anyMatch(ModCompat.FARMERSDELIGHT::equals)
            ) {
                builder.whenModLoaded(ModCompat.FARMERSDELIGHT);
            }
            return builder.whenModMissing(ModCompat.SLICE_AND_DICE).duration(30).output(result, count).withItemIngredients(input);
        });
    }

    @Override
    protected GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name, UnaryOperator<StandardProcessingRecipe.Builder<CuttingRecipe>> transform) {
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(getBuilder(name.get()))
                        .whenModLoaded(ModCompat.CREATE)
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }
}