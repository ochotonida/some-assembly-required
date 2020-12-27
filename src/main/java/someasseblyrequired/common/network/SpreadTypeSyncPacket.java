package someasseblyrequired.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import someasseblyrequired.common.item.spreadtype.SimpleSpreadType;
import someasseblyrequired.common.item.spreadtype.SpreadTypeManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SpreadTypeSyncPacket {

    private final Map<ResourceLocation, SimpleSpreadType> spreadTypes;

    public SpreadTypeSyncPacket(Map<ResourceLocation, SimpleSpreadType> spreadTypes) {
        this.spreadTypes = spreadTypes;
    }

    public SpreadTypeSyncPacket(PacketBuffer buffer) {
        int size = buffer.readVarInt();
        spreadTypes = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            ResourceLocation resourceLocation = buffer.readResourceLocation();
            SimpleSpreadType simpleSpreadType = SimpleSpreadType.read(buffer);
            spreadTypes.put(resourceLocation, simpleSpreadType);
        }
    }

    @SuppressWarnings("unused")
    void encode(PacketBuffer buffer) {
        buffer.writeVarInt(spreadTypes.size());
        spreadTypes.forEach(((resourceLocation, simpleSpreadType) -> {
            buffer.writeResourceLocation(resourceLocation);
            simpleSpreadType.write(buffer);
        }));
    }

    void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> SpreadTypeManager.INSTANCE.setSpreadTypes(spreadTypes));
        context.get().setPacketHandled(true);
    }
}
