package someassemblyrequired.data.recipe.create;

import com.simibubi.create.AllRecipeTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.util.Util;
import someassemblyrequired.integration.ModCompat;
import vectorwing.farmersdelight.common.tag.ForgeTags;

public class CuttingRecipeGenerator extends ProcessingRecipeGenerator {

    public CuttingRecipeGenerator(DataGenerator generator) {
        super(generator);

        cut(Items.APPLE, ModItems.APPLE_SLICES.get(), 2);
        cut(Items.BREAD, ModItems.BREAD_SLICE.get(), 4);
        cut(Items.BEETROOT, ModItems.CHOPPED_BEETROOT.get(), 2);
        cut(Items.CARROT, ModItems.CHOPPED_CARROT.get(), 2);
        cut(Items.GOLDEN_CARROT, ModItems.CHOPPED_GOLDEN_CARROT.get(), 2);
        cut(Items.ENCHANTED_GOLDEN_APPLE, ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(), 2);
        cut(Items.GOLDEN_APPLE, ModItems.GOLDEN_APPLE_SLICES.get(), 2);
        cut(ModItems.LETTUCE_HEAD.get(), ModItems.LETTUCE_LEAF.get(), 3);
        cut(ModItems.TOASTED_CRIMSON_FUNGUS.get(), ModItems.SLICED_TOASTED_CRIMSON_FUNGUS.get(), 2);
        cut(ModItems.TOASTED_WARPED_FUNGUS.get(), ModItems.SLICED_TOASTED_WARPED_FUNGUS.get(), 2);
        cut(ForgeTags.CROPS_TOMATO, ModItems.TOMATO_SLICES.get(), 2);
    }

    private void cut(ItemLike input, ItemLike result, int count) {
        cut(Ingredient.of(input), result, count);
    }

    private void cut(Tag<Item> input, ItemLike result, int count) {
        cut(Ingredient.of(input), result, count);
    }

    private void cut(Ingredient input, ItemLike result, int count) {
        // noinspection ConstantConditions
        create(Util.id(ModCompat.CREATE + "/" + result.asItem().getRegistryName().getPath()), builder -> builder.duration(30).output(result, count).withItemIngredients(input));
    }

    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.CUTTING;
    }
}