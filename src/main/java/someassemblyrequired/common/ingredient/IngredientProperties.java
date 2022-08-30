package someassemblyrequired.common.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModSoundEvents;
import someassemblyrequired.common.util.JsonHelper;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class IngredientProperties {

    @Nullable
    private final FoodProperties foodProperties;
    @Nullable
    private final Component displayName;
    @Nullable
    private final Component fullName;
    private final ItemStack displayItem;
    private final ItemStack container;
    @Nullable
    private final SoundEvent soundEvent;
    private final int height;
    private final boolean renderAsItem;

    public IngredientProperties(
            FoodProperties foodProperties,
            Component displayName,
            Component fullName,
            ItemStack displayItem,
            ItemStack container,
            SoundEvent soundEvent,
            int height,
            boolean renderAsItem
    ) {
        this.foodProperties = foodProperties;
        this.displayName = displayName;
        this.fullName = fullName;
        this.displayItem = displayItem;
        this.container = container;
        this.soundEvent = soundEvent;
        this.height = height;
        this.renderAsItem = renderAsItem;
    }

    public IngredientProperties() {
        this(null, null, null, ItemStack.EMPTY, ItemStack.EMPTY, null, 1, true);
    }

    @Nullable
    public FoodProperties getFood(ItemStack item, @Nullable LivingEntity entity) {
        if (foodProperties == null) {
            return item.getItem().getFoodProperties(item, entity);
        }
        return foodProperties;
    }

    public Component getDisplayName(ItemStack item) {
        if (displayName == null) {
            return getFullName(item);
        }
        return displayName;
    }

    public Component getFullName(ItemStack item) {
        if (fullName == null) {
            return item.getHoverName();
        }
        return fullName;
    }

    public ItemStack getDisplayItem(ItemStack item) {
        if (displayItem.isEmpty()) {
            return item;
        }
        return displayItem;
    }

    public ItemStack getContainer(ItemStack item) {
        if (container.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return container;
    }

    public boolean shouldRenderAsItem(ItemStack item) {
        return renderAsItem;
    }

    public int getHeight(ItemStack item) {
        return height;
    }

    public void playSound(Level level, Player player, BlockPos pos, float pitch) {
        SoundEvent soundEvent = this.soundEvent;
        if (soundEvent == null) {
            soundEvent = ModSoundEvents.ADD_ITEM.get();
        }
        level.playSound(player, pos, soundEvent, SoundSource.BLOCKS, 1, pitch);
    }

    public JsonObject toJson(Item item) {
        JsonObject result = new JsonObject();

        // noinspection ConstantConditions
        String modid = ForgeRegistries.ITEMS.getKey(item).getNamespace();
        if (!modid.equals("minecraft") && !modid.equals(SomeAssemblyRequired.MODID)) {
            JsonHelper.addModLoadedCondition(result, modid);
        }

        // noinspection ConstantConditions
        result.addProperty("item", ForgeRegistries.ITEMS.getKey(item).toString());
        if (foodProperties != null) {
            result.add("food", writeFoodProperties(foodProperties));
        }
        if (displayName != null) {
            result.add("displayName", Component.Serializer.toJsonTree(displayName));
        }
        if (fullName != null) {
            result.add("fullName", Component.Serializer.toJsonTree(fullName));
        }
        if (!displayItem.isEmpty()) {
            result.add("displayItem", JsonHelper.writeItemStack(displayItem));
        }
        if (!container.isEmpty()) {
            result.add("container", JsonHelper.writeItemStack(container));
        }
        if (soundEvent != null) {
            // noinspection ConstantConditions
            result.addProperty("soundEvent", ForgeRegistries.SOUND_EVENTS.getKey(soundEvent).toString());
        }
        if (height != 1) {
            result.addProperty("height", height);
        }
        if (!renderAsItem) {
            result.addProperty("renderAsItem", false);
        }

        return result;
    }

    public static IngredientProperties fromJson(JsonObject object) {
        FoodProperties foodProperties = readFoodProperties(object, "food");
        Component displayName = null;
        if (object.has("displayName")) {
            displayName = Component.Serializer.fromJson(object.get("displayName"));
        }
        Component fullName = null;
        if (object.has("fullName")) {
            fullName = Component.Serializer.fromJson(object.get("fullName"));
        }
        ItemStack displayItem = readOptionalItemStack(object, "displayItem");
        ItemStack container = readOptionalItemStack(object, "container");
        SoundEvent soundEvent = null;
        if (object.has("soundEvent")) {
            ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(object, "soundEvent"));
            if (!ForgeRegistries.SOUND_EVENTS.containsKey(id)) {
                throw new JsonParseException("No such sound event: " + id);
            }
            soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(id);
        }
        int height = 1;
        if (object.has("height")) {
            height = GsonHelper.convertToInt(object.get("height"), "height");
            if (height < 1) {
                throw new JsonParseException("Ingredient height cannot be smaller than 1");
            }
        }
        boolean renderAsItem = true;
        if (object.has("renderAsItem")) {
            renderAsItem = GsonHelper.convertToBoolean(object.get("renderAsItem"), "renderAsItem");
        }

        return new IngredientProperties(foodProperties, displayName, fullName, displayItem, container, soundEvent, height, renderAsItem);
    }

    private static JsonElement writeFoodProperties(FoodProperties properties) {
        JsonObject result = new JsonObject();
        result.addProperty("nutrition", properties.getNutrition());
        result.addProperty("saturationModifier", properties.getSaturationModifier());
        return result;
    }

    @Nullable
    private static FoodProperties readFoodProperties(JsonObject object, String memberName) {
        if (!object.has(memberName)) {
            return null;
        }
        JsonObject foodObject = GsonHelper.getAsJsonObject(object, memberName);
        FoodProperties.Builder builder = new FoodProperties.Builder();
        return builder
                .nutrition(GsonHelper.getAsInt(foodObject, "nutrition"))
                .saturationMod(GsonHelper.getAsInt(foodObject, "saturationModifier"))
                .build();
    }

    private static ItemStack readOptionalItemStack(JsonObject object, String memberName) {
        if (!object.has(memberName)) {
            return ItemStack.EMPTY;
        }
        return CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(object, memberName), true);
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeBoolean(foodProperties != null);
        if (foodProperties != null) {
            writeFoodProperties(buffer, foodProperties);
        }
        buffer.writeBoolean(displayName != null);
        if (displayName != null) {
            buffer.writeComponent(displayName);
        }
        buffer.writeBoolean(fullName != null);
        if (fullName != null) {
            buffer.writeComponent(fullName);
        }
        buffer.writeItem(displayItem);
        buffer.writeItem(container);
        buffer.writeBoolean(soundEvent != null);
        if (soundEvent != null) {
            // noinspection ConstantConditions
            buffer.writeResourceLocation(ForgeRegistries.SOUND_EVENTS.getKey(soundEvent));
        }
        buffer.writeInt(height);
        buffer.writeBoolean(renderAsItem);
    }

    public static IngredientProperties fromNetwork(FriendlyByteBuf buffer) {
        FoodProperties foodProperties = null;
        if (buffer.readBoolean()) {
            foodProperties = readFoodProperties(buffer);
        }
        Component displayName = null;
        if (buffer.readBoolean()) {
            displayName = buffer.readComponent();
        }
        Component fullName = null;
        if (buffer.readBoolean()) {
            fullName = buffer.readComponent();
        }
        ItemStack displayItem = buffer.readItem();
        ItemStack container = buffer.readItem();
        SoundEvent soundEvent = null;
        if (buffer.readBoolean()) {
            soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(buffer.readResourceLocation());
        }
        int height = buffer.readInt();
        boolean renderAsItem = buffer.readBoolean();
        return new IngredientProperties(foodProperties, displayName, fullName, displayItem, container, soundEvent, height, renderAsItem);
    }

    private static void writeFoodProperties(FriendlyByteBuf buffer, FoodProperties foodProperties) {
        buffer.writeInt(foodProperties.getNutrition());
        buffer.writeFloat(foodProperties.getSaturationModifier());
    }

    private static FoodProperties readFoodProperties(FriendlyByteBuf buffer) {
        return new FoodProperties.Builder()
                .nutrition(buffer.readInt())
                .saturationMod(buffer.readFloat())
                .build();
    }
}
