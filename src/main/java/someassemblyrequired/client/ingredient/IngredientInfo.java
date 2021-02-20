package someassemblyrequired.client.ingredient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.Item;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class IngredientInfo {

    private final Item item;
    private final ITextComponent displayName;
    private final DisplayInfo displayInfo;

    private IngredientInfo(Item item, @Nullable ITextComponent displayName, @Nullable DisplayInfo displayInfo) {
        this.item = item;
        this.displayName = displayName;
        this.displayInfo = displayInfo;
    }

    protected Item getItem() {
        return item;
    }

    public ITextComponent getDisplayName() {
        return displayName;
    }

    public DisplayInfo getDisplayInfo() {
        return displayInfo;
    }

    public static IngredientInfo deserialize(JsonObject object) {
        ResourceLocation itemLocation = new ResourceLocation(JSONUtils.getString(object, "item"));
        if (!ForgeRegistries.ITEMS.containsKey(itemLocation)) {
            throw new JsonParseException(String.format("Unknown item %s", itemLocation.toString()));
        }
        Item item = ForgeRegistries.ITEMS.getValue(itemLocation);

        ITextComponent displayName = object.has("name") ? ITextComponent.Serializer.getComponentFromJson(object.get("name")) : null;
        DisplayInfo displayInfo = object.has("display") ? DisplayInfo.deserialize(object.getAsJsonObject("display")) : null;

        return new IngredientInfo(item, displayName, displayInfo);
    }

    public static class DisplayInfo {

        private final int color;
        private final boolean translucent;

        public DisplayInfo(int color, boolean translucent) {
            this.color = color;
            this.translucent = translucent;
        }

        public int getColor() {
            return color;
        }

        public boolean isTranslucent() {
            return translucent;
        }

        @Nullable
        private static DisplayInfo deserialize(JsonObject object) {
            String type = JSONUtils.getString(object, "type");
            if (type.equals("default")) {
                return null;
            }
            if (!type.equals("spread") && !type.equals("translucent_spread")) {
                throw new JsonParseException("Invalid display type: " + type);
            }

            int color = JSONUtils.getInt(object, "color");
            boolean isTranslucent = type.equals("translucent_spread");

            return new DisplayInfo(color, isTranslucent);
        }
    }
}
