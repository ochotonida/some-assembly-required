package someassemblyrequired.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.common.ingredient.IngredientProperties;
import someassemblyrequired.common.ingredient.IngredientPropertiesManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class IngredientSyncPacket {

    private final Map<Item, IngredientProperties> ingredients;

    public IngredientSyncPacket(Map<Item, IngredientProperties> ingredients) {
        this.ingredients = ingredients;
    }

    public IngredientSyncPacket(FriendlyByteBuf buffer) {
        int size = buffer.readVarInt();
        ingredients = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            Item item = ForgeRegistries.ITEMS.getValue(buffer.readResourceLocation());
            IngredientProperties ingredient = IngredientProperties.fromNetwork(buffer);
            ingredients.put(item, ingredient);
        }
    }

    @SuppressWarnings("unused")
    void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(ingredients.size());
        ingredients.forEach(((item, ingredient) -> {
            // noinspection ConstantConditions
            buffer.writeResourceLocation(ForgeRegistries.ITEMS.getKey(item));
            ingredient.toNetwork(buffer);
        }));
    }

    void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> IngredientPropertiesManager.setIngredientProperties(ingredients));
        context.get().setPacketHandled(true);
    }
}
