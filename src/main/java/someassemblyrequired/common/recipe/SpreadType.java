package someassemblyrequired.common.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import someassemblyrequired.common.init.ModRecipeTypes;
import someassemblyrequired.common.init.ModSoundEvents;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpreadType extends SingleIngredientRecipe {

    private final Item container;
    private final SoundEvent soundEvent;

    public SpreadType(ResourceLocation id, String group, Ingredient ingredient, Item container, SoundEvent soundEvent) {
        super(ModRecipeTypes.SPREAD_TYPE, id, group, ingredient);
        this.container = container;
        this.soundEvent = soundEvent;
    }

    public Item getContainer() {
        return container;
    }

    @Nonnull
    public ItemStack getContainer(ItemStack spread) {
        if (container == Items.AIR) {
            return spread.getContainerItem();
        }
        return new ItemStack(container);
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inventory) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.SPREAD_TYPE_SERIALIZER.get();
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SpreadType> {

        @Override
        public SpreadType read(ResourceLocation id, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));

            ResourceLocation containerId = new ResourceLocation(JSONUtils.getString(json, "container", "minecraft:air"));
            if (!ForgeRegistries.ITEMS.containsKey(containerId)) {
                throw new JsonSyntaxException("Unknown item '" + id + "'");
            }
            Item container = ForgeRegistries.ITEMS.getValue(containerId);


            SoundEvent soundEvent;
            if (json.has("sound")) {
                soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(json.get("sound").getAsString()));
            } else {
                soundEvent = ModSoundEvents.ADD_SPREAD.get();
            }

            return new SpreadType(id, group, ingredient, container, soundEvent);
        }

        @Nullable
        @Override
        public SpreadType read(ResourceLocation id, PacketBuffer buffer) {
            return new SpreadType(
                    id,
                    buffer.readString(32767),
                    Ingredient.read(buffer),
                    ForgeRegistries.ITEMS.getValue(buffer.readResourceLocation()),
                    ForgeRegistries.SOUND_EVENTS.getValue(buffer.readResourceLocation())
            );
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        public void write(PacketBuffer buffer, SpreadType recipe) {
            buffer.writeString(recipe.group);
            recipe.input.write(buffer);
            buffer.writeResourceLocation(recipe.getContainer().getRegistryName());
            buffer.writeResourceLocation(recipe.getSoundEvent().getRegistryName());
        }
    }
}
