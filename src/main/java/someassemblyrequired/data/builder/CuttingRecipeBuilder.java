package someassemblyrequired.data.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.util.Util;
import someassemblyrequired.integration.ModCompat;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class CuttingRecipeBuilder {

    private static final Set<CuttingRecipeBuilder> recipes = new HashSet<>();

    private final List<ChanceResult> results = new ArrayList<>(4);
    private final Ingredient ingredient;
    private final Ingredient tool;
    private SoundEvent soundEvent;

    private CuttingRecipeBuilder(Ingredient ingredient, Ingredient tool, ItemLike mainResult, int count, float chance) {
        this.results.add(new ChanceResult(new ItemStack(mainResult.asItem(), count), chance));
        this.ingredient = ingredient;
        this.tool = tool;
    }

    public static void addCuttingRecipes(Consumer<FinishedRecipe> consumer) {
        recipes.clear();

        cuttingRecipe(Items.APPLE, ModItems.APPLE_SLICES.get(), 2);
        cuttingRecipe(Items.BREAD, ModItems.BREAD_SLICE.get(), 4);
        cuttingRecipe(Items.BEETROOT, ModItems.CHOPPED_BEETROOT.get(), 2);
        cuttingRecipe(Items.CARROT, ModItems.CHOPPED_CARROT.get(), 2);
        cuttingRecipe(Items.GOLDEN_CARROT, ModItems.CHOPPED_GOLDEN_CARROT.get(), 2);
        cuttingRecipe(Items.ENCHANTED_GOLDEN_APPLE, ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(), 2);
        cuttingRecipe(Items.GOLDEN_APPLE, ModItems.GOLDEN_APPLE_SLICES.get(), 2);
        cuttingRecipe(ModItems.LETTUCE_HEAD.get(), ModItems.LETTUCE_LEAF.get(), 3);
        cuttingRecipe(ModItems.TOASTED_CRIMSON_FUNGUS.get(), ModItems.SLICED_TOASTED_CRIMSON_FUNGUS.get(), 2);
        cuttingRecipe(ModItems.TOASTED_WARPED_FUNGUS.get(), ModItems.SLICED_TOASTED_WARPED_FUNGUS.get(), 2);
        cuttingRecipe(ForgeTags.CROPS_TOMATO, ModItems.TOMATO_SLICES.get(), 2);

        for (CuttingRecipeBuilder recipe : new HashSet<>(recipes)) {
            recipe.build(consumer);
        }
    }

    public static CuttingRecipeBuilder cuttingRecipe(ItemLike ingredient, ItemLike mainResult, int count) {
        return cuttingRecipe(Ingredient.of(ingredient), mainResult, count);
    }

    public static CuttingRecipeBuilder cuttingRecipe(Tag<Item> ingredient, ItemLike mainResult, int count) {
        return cuttingRecipe(Ingredient.of(ingredient), mainResult, count);
    }

    public static CuttingRecipeBuilder cuttingRecipe(Ingredient ingredient, ItemLike mainResult, int count) {
        CuttingRecipeBuilder builder = new CuttingRecipeBuilder(ingredient, Ingredient.of(ForgeTags.TOOLS_KNIVES), mainResult, count, 1.0F);
        recipes.add(builder);
        return builder;
    }

    public CuttingRecipeBuilder addResult(ItemLike result) {
        return addResult(result, 1);
    }

    public CuttingRecipeBuilder addResult(ItemLike result, int count) {
        results.add(new ChanceResult(new ItemStack(result.asItem(), count), 1.0F));
        return this;
    }

    public CuttingRecipeBuilder addResultWithChance(ItemLike result, float chance) {
        return this.addResultWithChance(result, chance, 1);
    }

    public CuttingRecipeBuilder addResultWithChance(ItemLike result, float chance, int count) {
        this.results.add(new ChanceResult(new ItemStack(result.asItem(), count), chance));
        return this;
    }

    public CuttingRecipeBuilder addSound(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
        return this;
    }

    public void build(Consumer<FinishedRecipe> consumer) {
        ResourceLocation location = results.get(0).getStack().getItem().getRegistryName();
        // noinspection ConstantConditions
        build(consumer, Util.id("cutting/" + location.getPath()));
    }

    public void build(Consumer<FinishedRecipe> consumer, String save) {
        build(consumer, new ResourceLocation(save));
    }

    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        recipes.remove(this);
        consumer.accept(new Result(id, ingredient, tool, results, soundEvent));
    }

    public static final class Result extends CuttingBoardRecipeBuilder.Result {

        public Result(ResourceLocation id, Ingredient ingredient, Ingredient tool, List<ChanceResult> results, SoundEvent soundEvent) {
            // noinspection ConstantConditions
            super(id, ingredient, tool, results, soundEvent == null ? "" : soundEvent.getRegistryName().toString());
        }

        public void serializeRecipeData(JsonObject object) {
            JsonArray conditions = new JsonArray();
            conditions.add(CraftingHelper.serialize(new ModLoadedCondition(ModCompat.FARMERS_DELIGHT)));
            object.add("conditions", conditions);
            super.serializeRecipeData(object);
        }
    }
}
