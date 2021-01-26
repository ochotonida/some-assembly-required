package someassemblyrequired.common.network;

import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import someassemblyrequired.common.util.Util;

public class NetworkHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            Util.prefix("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        INSTANCE.registerMessage(0, SpreadTypeSyncPacket.class, SpreadTypeSyncPacket::encode, SpreadTypeSyncPacket::new, SpreadTypeSyncPacket::handle);
    }
}
