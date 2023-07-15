package someassemblyrequired.data.providers.recipe.farmersdelight;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.util.JsonHelper;
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

    private static final Set<CuttingRecipeBuilder> RECIPES = new HashSet<>();

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
        RECIPES.clear();

        cut(Items.APPLE, ModItems.APPLE_SLICES.get(), 2);
        cut(Items.BREAD, ModItems.BREAD_SLICE.get(), 4);
        cut(Items.BEETROOT, ModItems.CHOPPED_BEETROOT.get(), 2);
        cut(Items.CARROT, ModItems.CHOPPED_CARROT.get(), 2);
        cut(Items.GOLDEN_CARROT, ModItems.CHOPPED_GOLDEN_CARROT.get(), 2);
        cut(Items.ENCHANTED_GOLDEN_APPLE, ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(), 2);
        cut(Items.GOLDEN_APPLE, ModItems.GOLDEN_APPLE_SLICES.get(), 2);
        cut(ForgeTags.CROPS_TOMATO, ModItems.TOMATO_SLICES.get(), 2);
        cut(ForgeTags.CROPS_ONION, ModItems.SLICED_ONION.get(), 2);

        cut(ModItems.BURGER_BUN.get(), ModItems.BURGER_BUN_BOTTOM.get(), 1).addResult(ModItems.BURGER_BUN_TOP.get());

        for (CuttingRecipeBuilder recipe : new HashSet<>(RECIPES)) {
            recipe.build(consumer);
        }
    }

    public static CuttingRecipeBuilder cut(ItemLike ingredient, ItemLike mainResult, int count) {
        return cut(Ingredient.of(ingredient), mainResult, count);
    }

    public static CuttingRecipeBuilder cut(TagKey<Item> ingredient, ItemLike mainResult, int count) {
        return cut(Ingredient.of(ingredient), mainResult, count);
    }

    public static CuttingRecipeBuilder cut(Ingredient ingredient, ItemLike mainResult, int count) {
        CuttingRecipeBuilder builder = new CuttingRecipeBuilder(ingredient, Ingredient.of(ForgeTags.TOOLS_KNIVES), mainResult, count, 1.0F);
        RECIPES.add(builder);
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
        Item item = results.size() == 1 ?
                results.get(0).getStack().getItem() : ingredient.getItems()[0].getItem();
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        // noinspection ConstantConditions
        build(consumer, SomeAssemblyRequired.id("cutting/%s/".formatted(ModCompat.FARMERSDELIGHT) + id.getPath()));
    }

    public void build(Consumer<FinishedRecipe> consumer, String save) {
        build(consumer, new ResourceLocation(save));
    }

    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        RECIPES.remove(this);
        // noinspection ConstantConditions
        String sound = soundEvent == null ? "" : ForgeRegistries.SOUND_EVENTS.getKey(soundEvent).toString();
        consumer.accept(new Result(id, ingredient, tool, results, sound));
    }

    public static class Result extends CuttingBoardRecipeBuilder.Result {

        public Result(ResourceLocation id, Ingredient ingredient, Ingredient tool, List<ChanceResult> results, String soundEventID) {
            super(id, ingredient, tool, results, soundEventID);
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonHelper.addModLoadedCondition(json, ModCompat.FARMERSDELIGHT);
            super.serializeRecipeData(json);
        }
    }
}
