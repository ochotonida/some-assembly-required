package someassemblyrequired.data.providers;

import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomModelData;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import net.neoforged.neoforge.common.extensions.IHolderExtension;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.data.providers.ingredient.*;
import someassemblyrequired.ingredient.IngredientProperties;
import someassemblyrequired.registry.ModItems;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

// TODO use neoforge provider or replace with data map
public record Ingredients(PackOutput packOutput) implements DataProvider {

    private static final Map<Holder<Item>, IngredientBuilder> INGREDIENTS = new HashMap<>();

    public static final List<Holder<Item>> MODEL_OVERRIDES = new ArrayList<>();

    static {
        MODEL_OVERRIDES.addAll(List.of(
                ModItems.BURGER_BUN,
                ModItems.BURGER_BUN_BOTTOM,
                ModItems.BURGER_BUN_TOP,
                ModItems.APPLE_SLICES,
                ModItems.GOLDEN_APPLE_SLICES,
                ModItems.CHOPPED_CARROT,
                ModItems.CHOPPED_GOLDEN_CARROT,
                ModItems.CHOPPED_BEETROOT,
                ModItems.TOMATO_SLICES,
                ModItems.SLICED_ONION
        ));
        for (Item override : FarmersDelightIngredients.MODEL_OVERRIDES) {
            MODEL_OVERRIDES.add(BuiltInRegistries.ITEM.wrapAsHolder(override));
        }
        MODEL_OVERRIDES.addAll(MinersDelightIngredients.MODEL_OVERRIDES);
        MODEL_OVERRIDES.addAll(CreateFoodIngredients.MODEL_OVERRIDES);
        MODEL_OVERRIDES.add(BuiltInRegistries.ITEM.wrapAsHolder(Items.POTATO));
    }

    private void addIngredients() {
        INGREDIENTS.clear();

        for (int i = 0; i < MODEL_OVERRIDES.size(); i++) {
            Holder<Item> item = MODEL_OVERRIDES.get(i);
            ItemStack displayItem = new ItemStack(ModItems.SPREAD.get());
            displayItem.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(i + 1));
            builder(item).setDisplayItem(displayItem);
        }

        CreateIngredients.addIngredients(this);
        FarmersDelightIngredients.addIngredients(this);
        MinersDelightIngredients.addIngredients(this);
        CreateFoodIngredients.addIngredients(this);

        ItemStack displayItem = INGREDIENTS.get(ModItems.GOLDEN_APPLE_SLICES).getDisplayItem().copy();
        displayItem.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        builder(ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get()).setDisplayItem(displayItem);

        builder(Items.BEETROOT_SOUP).setSpread(0x8C0023).setMoistSound();
        builder(Items.MUSHROOM_STEW).setSpread(0xAD7451).setMoistSound();
        builder(Items.RABBIT_STEW).setSpread(0xBF7234).setWetSound();
        builder(Items.SUSPICIOUS_STEW).setSpread(0x3f9E80).setMoistSound();

        builder(Items.HONEY_BOTTLE).setCustomFullName().setSpread(0xf0a90e, 0.8).setMoistSound();

        builder(Items.MILK_BUCKET).setCustomFullName().setSpread(0xEEFDFF).setMoistSound()
                .setFoodProperties(new FoodProperties.Builder().usingConvertsTo(Items.BUCKET).build());

        builder(Items.POTATO).setHeight(5).customModel();
        builder(ModItems.BURGER_BUN.get()).setHeight(6).customModel();
        String burgerBunHalf = "%s.ingredient.%s".formatted(SomeAssemblyRequired.MOD_ID, "burger_bun_half");
        builder(ModItems.BURGER_BUN_BOTTOM.get()).setHeight(2).customModel().setFullName(burgerBunHalf);
        builder(ModItems.BURGER_BUN_TOP.get()).setHeight(4).customModel().setFullName(burgerBunHalf);

        builder(Items.GOLD_BLOCK).setHeight(8).customModel().hidden();

        Arrays.asList(
                ModItems.TOASTED_BREAD_SLICE.get(),
                ModItems.APPLE_SLICES.get(),
                ModItems.GOLDEN_APPLE_SLICES.get(),
                ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(),
                ModItems.CHOPPED_CARROT.get(),
                ModItems.CHOPPED_GOLDEN_CARROT.get(),
                ModItems.CHOPPED_BEETROOT.get(),
                ModItems.TOMATO_SLICES.get(),
                ModItems.SLICED_ONION.get()
        ).forEach(item -> builder(item).setCustomDisplayName());
    }

    public IngredientBuilder builder(Item item) {
        return builder(BuiltInRegistries.ITEM.wrapAsHolder(item));
    }

    public IngredientBuilder builder(Holder<Item> item) {
        if (INGREDIENTS.containsKey(item)) {
            return INGREDIENTS.get(item);
        }
        IngredientBuilder builder = new IngredientBuilder(item);
        INGREDIENTS.put(item, builder);
        return builder;
    }

    public CompletableFuture<?> run(CachedOutput cache) {
        Path outputFolder = packOutput().getOutputFolder();
        List<CompletableFuture<?>> futures = new ArrayList<>();

        addIngredients();
        INGREDIENTS.forEach((item, builder) -> {
            IngredientProperties ingredient = builder.build();
            // noinspection ConstantConditions
            ResourceLocation id = item.getKey().location();
            String name = id.getPath();
            if (!"minecraft".equals(id.getNamespace()) && !SomeAssemblyRequired.MOD_ID.equals(id.getNamespace())) {
                name = id.getNamespace() + "/" + name;
            }

            Path path = outputFolder.resolve("data/%s/%s/ingredients/%s.json".formatted(SomeAssemblyRequired.MOD_ID, SomeAssemblyRequired.MOD_ID, name));

            String modId = id.getNamespace();

            List<ICondition> conditions = modId.equals("minecraft") || modId.equals(SomeAssemblyRequired.MOD_ID) ? List.of()
                    : List.of(new ModLoadedCondition(modId));

            futures.add(DataProvider.saveStable(cache, ConditionalOps.createConditionalCodecWithConditions(IngredientProperties.CODEC)
                    .encodeStart(JsonOps.INSTANCE, Optional.of(new WithConditions<>(conditions, ingredient))).getOrThrow(), path));
        });

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    public List<ResourceKey<Item>> collectSpecialFillings() {
        addIngredients();
        return INGREDIENTS.keySet().stream()
                .filter(key -> {
                    IngredientProperties ingredient = INGREDIENTS.get(key).build();
                    return !ingredient.hidden() && !ingredient.displayItem().isEmpty();
                })
                .map(IHolderExtension::getKey)
                .peek(Objects::requireNonNull)
                .sorted(Comparator.comparing(key -> key.location().toString()))
                .toList();
    }

    public static Holder.Reference<Item> reference(String modId, String path) {
        return Holder.Reference.createStandAlone(BuiltInRegistries.ITEM.holderOwner(), ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, path)));
    }

    @Override
    public String getName() {
        return "Ingredients";
    }
}
