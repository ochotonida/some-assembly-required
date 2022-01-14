package someassemblyrequired.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.ingredient.CustomIngredientModels;
import someassemblyrequired.common.ingredient.IngredientProperties;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.data.ingredient.CreateIngredients;
import someassemblyrequired.data.ingredient.FarmersDelightIngredients;
import someassemblyrequired.data.ingredient.IngredientBuilder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record Ingredients(DataGenerator generator) implements DataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static final Map<Item, IngredientBuilder> ingredients = new HashMap<>();

    private void addIngredients() {
        ingredients.clear();

        for (Item item : CustomIngredientModels.itemsWithCustomModel) {
            ItemStack displayItem = new ItemStack(ModItems.SPREAD.get());
            // noinspection ConstantConditions
            displayItem.getOrCreateTag().putString("Item", item.getRegistryName().toString());
            builder(item).setDisplayItem(displayItem);
        }

        new CreateIngredients(ingredients).addIngredients();
        new FarmersDelightIngredients(ingredients).addIngredients();

        ItemStack displayItem = new ItemStack(ModItems.SPREAD.get());
        CompoundTag tag = displayItem.getOrCreateTag();
        tag.putString("Item", ModItems.GOLDEN_APPLE_SLICES.getId().toString());
        tag.putBoolean("HasEffect", true);
        builder(ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get()).setDisplayItem(displayItem);

        builder(Items.BEETROOT_SOUP).setBowled().setSpread(0x8C0023);
        builder(Items.MUSHROOM_STEW).setBowled().setSpread(0xAD7451);
        builder(Items.RABBIT_STEW).setBowled().setSpread(0xBF7234);
        builder(Items.SUSPICIOUS_STEW).setBowled().setSpread(0x3f9E80);

        builder(Items.HONEY_BOTTLE).setCustomFullName().setBottled().setSpread(0xF08A1D);
        builder(ModItems.MAYONNAISE_BOTTLE.get()).setCustomFullName().setBottled().setSpread(0xD9C98C);
        builder(ModItems.SWEET_BERRY_JAM_BOTTLE.get()).setCustomFullName().setBottled().setSpread(0xF40020);

        builder(Items.MILK_BUCKET).setCustomFullName().setBucketed().setSpread(0xEEFDFF);

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

    private IngredientBuilder builder(Item item) {
        if (ingredients.containsKey(item)) {
            return ingredients.get(item);
        }
        IngredientBuilder builder = new IngredientBuilder(item);
        ingredients.put(item, builder);
        return builder;
    }

    public void run(HashCache cache) {
        Path outputFolder = this.generator.getOutputFolder();
        addIngredients();
        ingredients.forEach((item, builder) -> {
            IngredientProperties ingredient = builder.build();
            ResourceLocation id = item.getRegistryName();
            // noinspection ConstantConditions
            String name = id.getPath();
            if (!"minecraft".equals(id.getNamespace()) && !SomeAssemblyRequired.MODID.equals(id.getNamespace())) {
                name = id.getNamespace() + "/" + name;
            }

            Path path = outputFolder.resolve("data/%s/%s/ingredients/%s.json".formatted(SomeAssemblyRequired.MODID, SomeAssemblyRequired.MODID, name));
            saveIngredient(cache, ingredient.toJson(item), path);
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
