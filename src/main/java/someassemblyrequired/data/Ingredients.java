package someassemblyrequired.data;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.ingredient.IngredientProperties;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.data.ingredient.CreateIngredients;
import someassemblyrequired.data.ingredient.FarmersDelightIngredients;
import someassemblyrequired.data.ingredient.IngredientBuilder;
import someassemblyrequired.integration.ModCompat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public record Ingredients(DataGenerator generator) implements DataProvider {

    private static final Map<Item, IngredientBuilder> INGREDIENTS = new HashMap<>();

    public static final List<Item> MODEL_OVERRIDES = new ArrayList<>();

    static {
        MODEL_OVERRIDES.addAll(List.of(
                ModItems.APPLE_SLICES.get(),
                ModItems.GOLDEN_APPLE_SLICES.get(),
                ModItems.CHOPPED_CARROT.get(),
                ModItems.CHOPPED_GOLDEN_CARROT.get(),
                ModItems.CHOPPED_BEETROOT.get(),
                ModItems.TOMATO_SLICES.get()
        ));
        MODEL_OVERRIDES.addAll(FarmersDelightIngredients.itemsWithCustomModel);
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

        builder(Items.BEETROOT_SOUP).setBowled().setSpread(0x8C0023).setSpreadSound();
        builder(Items.MUSHROOM_STEW).setBowled().setSpread(0xAD7451).setSpreadSound();
        builder(Items.RABBIT_STEW).setBowled().setSpread(0xBF7234).setSpreadSound();
        builder(Items.SUSPICIOUS_STEW).setBowled().setSpread(0x3f9E80).setSpreadSound();

        builder(Items.HONEY_BOTTLE).setCustomFullName().setBottled().setSpread(0xf0a90e).setSpreadSound();

        builder(Items.MILK_BUCKET).setCustomFullName().setBucketed().setSpread(0xEEFDFF);

        builder(Items.POTATO).setHeight(5);

        Arrays.asList(
                ModItems.TOASTED_BREAD_SLICE.get(),
                ModItems.APPLE_SLICES.get(),
                ModItems.GOLDEN_APPLE_SLICES.get(),
                ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(),
                ModItems.CHOPPED_CARROT.get(),
                ModItems.CHOPPED_GOLDEN_CARROT.get(),
                ModItems.CHOPPED_BEETROOT.get(),
                ModItems.TOMATO_SLICES.get()
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

    public void run(CachedOutput cache) {
        Path outputFolder = this.generator.getOutputFolder();
        addIngredients();
        INGREDIENTS.forEach((item, builder) -> {
            IngredientProperties ingredient = builder.build();
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
            // noinspection ConstantConditions
            String name = id.getPath();
            if (!"minecraft".equals(id.getNamespace()) && !SomeAssemblyRequired.MODID.equals(id.getNamespace())) {
                name = id.getNamespace() + "/" + name;
            }

            Path path = outputFolder.resolve("data/%s/%s/ingredients/%s.json".formatted(SomeAssemblyRequired.MODID, SomeAssemblyRequired.MODID, name));
            saveIngredient(cache, ingredient.toJson(item), path);
        });
    }

    private static void saveIngredient(CachedOutput cache, JsonObject object, Path path) {
        // TODO
        try {
            DataProvider.saveStable(cache, object, path);
        } catch (IOException exception) {
            SomeAssemblyRequired.LOGGER.error("Couldn't save ingredient {}", path, exception);
        }
    }

    @Override
    public String getName() {
        return "Ingredients";
    }
}
