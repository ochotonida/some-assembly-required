package someassemblyrequired.data.providers;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.data.providers.ingredient.CreateIngredients;
import someassemblyrequired.data.providers.ingredient.FarmersDelightIngredients;
import someassemblyrequired.data.providers.ingredient.IngredientBuilder;
import someassemblyrequired.ingredient.IngredientProperties;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.registry.ModItems;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public record Ingredients(PackOutput packOutput) implements DataProvider {

    private static final Map<Item, IngredientBuilder> INGREDIENTS = new HashMap<>();

    public static final List<Item> MODEL_OVERRIDES = new ArrayList<>();

    static {
        MODEL_OVERRIDES.addAll(List.of(
                ModItems.BURGER_BUN.get(),
                ModItems.BURGER_BUN_BOTTOM.get(),
                ModItems.BURGER_BUN_TOP.get(),
                ModItems.APPLE_SLICES.get(),
                ModItems.GOLDEN_APPLE_SLICES.get(),
                ModItems.CHOPPED_CARROT.get(),
                ModItems.CHOPPED_GOLDEN_CARROT.get(),
                ModItems.CHOPPED_BEETROOT.get(),
                ModItems.TOMATO_SLICES.get(),
                ModItems.SLICED_ONION.get()
        ));
        MODEL_OVERRIDES.addAll(FarmersDelightIngredients.MODEL_OVERRIDES);
        MODEL_OVERRIDES.add(
                Items.POTATO
        );
    }

    private void addIngredients() {
        INGREDIENTS.clear();

        for (int i = 0; i < MODEL_OVERRIDES.size(); i++) {
            Item item = MODEL_OVERRIDES.get(i);
            ItemStack displayItem = new ItemStack(ModItems.SPREAD.get());
            displayItem.getOrCreateTag().putInt("CustomModelData", i + 1);
            builder(item).setDisplayItem(displayItem);
        }

        if (ModCompat.isCreateLoaded()) {
            CreateIngredients.addIngredients(this);
        }
        if (ModCompat.isFarmersDelightLoaded()) {
            FarmersDelightIngredients.addIngredients(this);
        }

        ItemStack displayItem = INGREDIENTS.get(ModItems.GOLDEN_APPLE_SLICES.get()).getDisplayItem().copy();
        displayItem.getOrCreateTag().putBoolean("HasEffect", true);
        builder(ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get()).setDisplayItem(displayItem);

        builder(Items.BEETROOT_SOUP).setBowled().setSpread(0x8C0023).setMoistSound();
        builder(Items.MUSHROOM_STEW).setBowled().setSpread(0xAD7451).setMoistSound();
        builder(Items.RABBIT_STEW).setBowled().setSpread(0xBF7234).setWetSound();
        builder(Items.SUSPICIOUS_STEW).setBowled().setSpread(0x3f9E80).setMoistSound();

        builder(Items.HONEY_BOTTLE).setCustomFullName().setBottled().setSpread(0xf0a90e).setMoistSound();

        builder(Items.MILK_BUCKET).setCustomFullName().setBucketed().setSpread(0xEEFDFF).setMoistSound();

        builder(Items.POTATO).setHeight(5).setRenderAsItem(false);
        builder(ModItems.BURGER_BUN.get()).setHeight(6).setRenderAsItem(false);
        String burgerBunHalf = "%s.ingredient.%s".formatted(SomeAssemblyRequired.MOD_ID, "burger_bun_half");
        builder(ModItems.BURGER_BUN_BOTTOM.get()).setHeight(2).setRenderAsItem(false).setFullName(burgerBunHalf);
        builder(ModItems.BURGER_BUN_TOP.get()).setHeight(4).setRenderAsItem(false).setFullName(burgerBunHalf);

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
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
            // noinspection ConstantConditions
            String name = id.getPath();
            if (!"minecraft".equals(id.getNamespace()) && !SomeAssemblyRequired.MOD_ID.equals(id.getNamespace())) {
                name = id.getNamespace() + "/" + name;
            }

            Path path = outputFolder.resolve("data/%s/%s/ingredients/%s.json".formatted(SomeAssemblyRequired.MOD_ID, SomeAssemblyRequired.MOD_ID, name));

            futures.add(DataProvider.saveStable(cache, ingredient.toJson(item), path));
        });

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Ingredients";
    }
}
