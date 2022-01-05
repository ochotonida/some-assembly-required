package someassemblyrequired.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.ingredient.DataIngredient;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.data.recipe.IngredientBuilder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public record SandwichIngredients(DataGenerator generator) implements DataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static final Set<IngredientBuilder> ingredients = new HashSet<>();

    private void addIngredients() {
        ingredients.clear();

        builder(Items.BEETROOT_SOUP).setBowled().setSpread(0x8C0023);
        builder(Items.MUSHROOM_STEW).setBowled().setSpread(0xAD7451);
        builder(Items.RABBIT_STEW).setBowled().setSpread(0xBF7234);
        // TODO suspicious stew

        builder(Items.HONEY_BOTTLE).setCustomFullName().setBottled().setSpread(0xF08A1D);
        builder(ModItems.KETCHUP_BOTTLE.get()).setCustomFullName().setBottled().setSpread(0x910900);
        builder(ModItems.MAYONNAISE_BOTTLE.get()).setCustomFullName().setBottled().setSpread(0xD9C98C);
        builder(ModItems.SWEET_BERRY_JAM_BOTTLE.get()).setCustomFullName().setBottled().setSpread(0xF40020);

        builder(Items.MILK_BUCKET).setCustomFullName().setBucketed().setSpread(0xEEFDFF);

        builder(ModItems.TOASTED_BREAD_SLICE.get()).setCustomDisplayName();

        Arrays.asList(
                ModItems.APPLE_SLICES.get(),
                ModItems.GOLDEN_APPLE_SLICES.get(),
                ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(),
                ModItems.CHOPPED_CARROT.get(),
                ModItems.CHOPPED_GOLDEN_CARROT.get(),
                ModItems.CHOPPED_BEETROOT.get(),
                ModItems.SLICED_TOASTED_CRIMSON_FUNGUS.get(),
                ModItems.SLICED_TOASTED_WARPED_FUNGUS.get(),
                ModItems.TOMATO_SLICES.get(),
                ModItems.LETTUCE_LEAF.get()
        ).forEach(item -> builder(item).setCustomDisplayName().setCustomModelData());
    }

    private IngredientBuilder builder(Item item) {
        IngredientBuilder builder = new IngredientBuilder(item);
        ingredients.add(builder);
        return builder;
    }

    public void run(HashCache cache) {
        Path outputFolder = this.generator.getOutputFolder();
        Set<Item> set = new HashSet<>();
        addIngredients();
        ingredients.forEach(builder -> {
            Item item = builder.getItem();
            if (!set.add(item)) {
                throw new IllegalStateException("Duplicate ingredient for item " + item.getRegistryName());
            } else {
                DataIngredient ingredient = builder.build();
                ResourceLocation id = item.getRegistryName();
                // noinspection ConstantConditions
                String name = id.getPath();
                if (!"minecraft".equals(id.getNamespace()) && !SomeAssemblyRequired.MODID.equals(id.getNamespace())) {
                    name = id.getNamespace() + "/" + name;
                }

                Path path = outputFolder.resolve("data/%s/%s/ingredients/%s.json".formatted(SomeAssemblyRequired.MODID, SomeAssemblyRequired.MODID, name));
                saveIngredient(cache, ingredient.toJson(item), path);
            }
        });
    }

    private static void saveIngredient(HashCache cache, JsonObject object, Path path) {
        try {
            String json = GSON.toJson(object);
            String hash = SHA1.hashUnencodedChars(json).toString();
            if (!Objects.equals(cache.getHash(path), hash) || !Files.exists(path)) {
                Files.createDirectories(path.getParent());
                try (BufferedWriter bufferedwriter = Files.newBufferedWriter(path)) {
                    bufferedwriter.write(json);
                }
            }
            cache.putNew(path, hash);
        } catch (IOException exception) {
            SomeAssemblyRequired.LOGGER.error("Couldn't save ingredient {}", path, exception);
        }
    }

    @Override
    public String getName() {
        return "Ingredients";
    }
}
