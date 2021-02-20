package someassemblyrequired.client.ingredient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.crafting.CraftingHelper;
import someassemblyrequired.SomeAssemblyRequired;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IngredientInfoManager extends JsonReloadListener {

    public static final IngredientInfoManager INSTANCE = new IngredientInfoManager();

    private Map<Item, IngredientInfo> data = Collections.emptyMap();

    public IngredientInfoManager() {
        super(new Gson(), "sandwich_ingredients");
    }

    @Nullable
    public ITextComponent getDisplayName(Item item) {
        return data.containsKey(item) ? data.get(item).getDisplayName() : null;
    }

    @Nullable
    public IngredientInfo.DisplayInfo getDisplayInfo(Item item) {
        return data.containsKey(item) ? data.get(item).getDisplayInfo() : null;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, IResourceManager resourceManager, IProfiler profiler) {
        Map<ResourceLocation, IngredientInfo> data = new HashMap<>();

        object.forEach((resourceLocation, element) -> {
            try {
                if (element.isJsonObject() && !CraftingHelper.processConditions(element.getAsJsonObject(), "conditions")) {
                    SomeAssemblyRequired.LOGGER.debug("Skipping loading sandwich ingredient info {} as it's conditions were not met", resourceLocation);
                    return;
                }
                JsonObject jsonobject = JSONUtils.getJsonObject(element, "sandwich ingredient");
                data.put(resourceLocation, IngredientInfo.deserialize(jsonobject));
            } catch (IllegalArgumentException | JsonParseException | ResourceLocationException exception) {
                SomeAssemblyRequired.LOGGER.error("Parsing error loading sandwich ingredient info {}: {}", resourceLocation, exception.getMessage());
            }
        });
        SomeAssemblyRequired.LOGGER.info("Loaded {} sandwich ingredient info's", data.size());
        this.data = data.values().stream().collect(Collectors.toMap(IngredientInfo::getItem, Function.identity()));
    }
}
