package someassemblyrequired.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.client.ingredient.IngredientInfo;
import someassemblyrequired.common.init.ModItems;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IngredientInfos implements IDataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    private final Map<Item, ITextComponent> displayNames = new HashMap<>();
    private final Map<Item, IngredientInfo.DisplayInfo> displayInfos = new HashMap<>();

    private final DataGenerator generator;

    protected IngredientInfos(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String getName() {
        return "Sandwich Ingredients";
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        addNames();
        addDisplayInfos();

        Path path = generator.getOutputFolder();

        Set<Item> items = new HashSet<>();
        items.addAll(displayNames.keySet());
        items.addAll(displayInfos.keySet());

        for (Item item : items) {
            JsonObject json = new JsonObject();
            // noinspection ConstantConditions
            json.addProperty("item", item.getRegistryName().toString());
            serializeName(item, json);
            serializeDisplayInfo(item, json);
            IDataProvider.save(GSON, cache, json, path.resolve("assets/" + SomeAssemblyRequired.MODID + "/sandwich_ingredients/" + item.getRegistryName().getPath() + ".json"));
        }
    }

    protected void serializeName(Item item, JsonObject json) {
        if (displayNames.containsKey(item)) {
            json.add("name", ITextComponent.Serializer.toJsonTree(displayNames.get(item)));
        }
    }

    protected void serializeDisplayInfo(Item item, JsonObject json) {
        if (displayInfos.containsKey(item)) {
            JsonObject display = new JsonObject();
            display.addProperty("type", displayInfos.get(item).isTranslucent() ? "translucent_spread" : "spread");
            display.addProperty("color", displayInfos.get(item).getColor());
            json.add("display", display);
        }
    }

    private void addTranslatedName(Item item) {
        ResourceLocation location = item.getRegistryName();
        // noinspection ConstantConditions
        displayNames.put(item, new TranslationTextComponent("ingredient.someassemblyrequired." + location.getPath()));
    }

    private void addDisplay(Item item, int color, boolean translucent) {
        displayInfos.put(item, new IngredientInfo.DisplayInfo(color, translucent));
    }

    protected void addNames() {
        Item[] items = new Item[]{
                Items.HONEY_BOTTLE,
                Items.MILK_BUCKET,
                ModItems.TOASTED_BREAD_SLICE.get(),
                ModItems.APPLE_SLICES.get(),
                ModItems.GOLDEN_APPLE_SLICES.get(),
                ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(),
                ModItems.CHOPPED_CARROT.get(),
                ModItems.CHOPPED_GOLDEN_CARROT.get(),
                ModItems.CHOPPED_BEETROOT.get(),
                ModItems.PORK_CUTS.get(),
                ModItems.BACON_STRIPS.get(),
                ModItems.SLICED_TOASTED_CRIMSON_FUNGUS.get(),
                ModItems.SLICED_TOASTED_WARPED_FUNGUS.get(),
                ModItems.TOMATO_SLICES.get(),
                ModItems.LETTUCE_LEAF.get(),
                ModItems.MAYONNAISE_BOTTLE.get(),
                ModItems.KETCHUP_BOTTLE.get(),
                ModItems.SWEET_BERRY_JAM_BOTTLE.get()
        };
        for (Item item : items) {
            addTranslatedName(item);
        }
    }

    protected void addDisplayInfos() {
        addDisplay(Items.BEETROOT_SOUP, 0x8C0023, false);
        addDisplay(Items.HONEY_BOTTLE, 0xF08A1D, true);
        addDisplay(Items.MILK_BUCKET, 0xEEFDFF, false);
        addDisplay(Items.MUSHROOM_STEM, 0xAD7451, false);
        addDisplay(Items.RABBIT_STEW, 0xBF7234, false);
        addDisplay(Items.SUSPICIOUS_STEW, 0xC3C45E, false);

        addDisplay(ModItems.KETCHUP_BOTTLE.get(), 0x910900, false);
        addDisplay(ModItems.MAYONNAISE_BOTTLE.get(), 0xD9C98C, false);
        addDisplay(ModItems.SWEET_BERRY_JAM_BOTTLE.get(), 0xF40020, true);
    }
}
