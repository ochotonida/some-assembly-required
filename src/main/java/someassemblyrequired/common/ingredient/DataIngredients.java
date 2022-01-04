package someassemblyrequired.common.ingredient;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.forgespi.Environment;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.network.IngredientSyncPacket;
import someassemblyrequired.common.network.NetworkHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class DataIngredients extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static Map<Item, DataIngredient> ingredients = Map.of();

    private DataIngredients() {
        super(GSON, SomeAssemblyRequired.MODID + "/ingredients");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<Item, DataIngredient> map = new HashMap<>();

        resources.forEach((resourceLocation, element) -> {
            try {
                if (element.isJsonObject() && !CraftingHelper.processConditions(element.getAsJsonObject(), "conditions")) {
                    SomeAssemblyRequired.LOGGER.debug("Skipping loading spread type {} as it's conditions were not met", resourceLocation);
                    return;
                }

                JsonObject object = GsonHelper.convertToJsonObject(element, "spread type");
                DataIngredient ingredient = DataIngredient.fromJson(object);
                ResourceLocation itemId = new ResourceLocation(GsonHelper.getAsString(object, "item"));
                if (!ForgeRegistries.ITEMS.containsKey(itemId)) {
                    throw new JsonParseException("Unknown item: " + itemId);
                }
                Item item = ForgeRegistries.ITEMS.getValue(itemId);

                if (map.containsKey(item)) {
                    SomeAssemblyRequired.LOGGER.error("Multiple ingredients found for item " + itemId);
                }
                map.put(item, ingredient);
            } catch (IllegalArgumentException | JsonParseException exception) {
                SomeAssemblyRequired.LOGGER.error("Parsing error loading sandwich ingredient {}: {}", resourceLocation, exception.getMessage());
            }
        });

        ingredients = ImmutableMap.copyOf(map);
        SomeAssemblyRequired.LOGGER.info("Loaded {} sandwich ingredients", map.size());
    }

    @Nullable
    protected static SandwichIngredient get(ItemStack item) {
        return ingredients.get(item.getItem());
    }

    public static void setIngredients(Map<Item, DataIngredient> brewTypes) {
        DataIngredients.ingredients = brewTypes;
    }

    public static void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new DataIngredients());
    }

    public static void onDataPackReload(OnDatapackSyncEvent event) {
        if (Environment.get().getDist().isClient()) {
            return;
        }
        if (event.getPlayer() != null) {
            sync(event.getPlayer());
        } else {
            event.getPlayerList().getPlayers().forEach(DataIngredients::sync);
        }
    }

    private static void sync(ServerPlayer player) {
        NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new IngredientSyncPacket(ingredients));
    }
}
