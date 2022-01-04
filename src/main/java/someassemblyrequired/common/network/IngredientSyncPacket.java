package someassemblyrequired.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.common.ingredient.DataIngredient;
import someassemblyrequired.common.ingredient.DataIngredients;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class IngredientSyncPacket {

    private final Map<Item, DataIngredient> ingredients;

    public IngredientSyncPacket(Map<Item, DataIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public IngredientSyncPacket(FriendlyByteBuf buffer) {
        int size = buffer.readVarInt();
        ingredients = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            Item item = ForgeRegistries.ITEMS.getValue(buffer.readResourceLocation());
            DataIngredient ingredient = DataIngredient.fromNetwork(buffer);
            ingredients.put(item, ingredient);
        }
    }

    @SuppressWarnings("unused")
    void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(ingredients.size());
        ingredients.forEach(((item, ingredient) -> {
            // noinspection ConstantConditions
            buffer.writeResourceLocation(item.getRegistryName());
            ingredient.toNetwork(buffer);
        }));
    }

    void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DataIngredients.setIngredients(ingredients));
        context.get().setPacketHandled(true);
    }
}
