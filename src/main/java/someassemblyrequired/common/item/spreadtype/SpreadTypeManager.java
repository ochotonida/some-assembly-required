package someassemblyrequired.common.item.spreadtype;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.PacketDistributor;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.network.NetworkHandler;
import someassemblyrequired.common.network.SpreadTypeSyncPacket;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SpreadTypeManager extends JsonReloadListener {

    public static final SpreadTypeManager INSTANCE = new SpreadTypeManager();

    private Map<ResourceLocation, SimpleSpreadType> spreadTypes = Collections.emptyMap();
    private Map<Item, SpreadType> spreadTypeLookup = Collections.emptyMap();

    private SpreadTypeManager() {
        super(new Gson(), SomeAssemblyRequired.MODID + "/spread_types");
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, IResourceManager resourceManager, IProfiler profiler) {
        Map<ResourceLocation, SimpleSpreadType> spreadTypes = Maps.newHashMap();

        object.forEach((resourceLocation, element) -> {
            try {
                if (element.isJsonObject() && !net.minecraftforge.common.crafting.CraftingHelper.processConditions(element.getAsJsonObject(), "conditions")) {
                    SomeAssemblyRequired.LOGGER.debug("Skipping loading spread type {} as it's conditions were not met", resourceLocation);
                    return;
                }

                JsonObject jsonobject = JSONUtils.getJsonObject(element, "spread type");
                SimpleSpreadType spreadType = SimpleSpreadType.deserialize(jsonobject);
                if (spreadTypes.values().stream().anyMatch(s -> s.getIngredient() == spreadType.getIngredient())) {
                    SomeAssemblyRequired.LOGGER.warn("Multiple spread types found for item {}", spreadType.getIngredient());
                }
                spreadTypes.put(resourceLocation, spreadType);
            } catch (IllegalArgumentException | JsonParseException exception) {
                SomeAssemblyRequired.LOGGER.error("Parsing error loading custom spread type {}: {}", resourceLocation, exception.getMessage());
            }
        });
        SomeAssemblyRequired.LOGGER.info("Loaded {} spread types", spreadTypes.size());

        boolean isServer = true;
        try {
            LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        } catch (Exception exception) {
            isServer = false;
        }
        if (isServer) {
            sendSyncingPackets();
        }

        setSpreadTypes(spreadTypes);
    }

    @Nullable
    public SpreadType getSpreadType(Item item) {
        return spreadTypeLookup.get(item);
    }

    public boolean hasSpreadType(Item item) {
        return spreadTypeLookup.containsKey(item);
    }

    public void setSpreadTypes(Map<ResourceLocation, SimpleSpreadType> spreadTypes) {
        this.spreadTypes = spreadTypes;
        refreshSpreadTypeLookup();
    }

    private void refreshSpreadTypeLookup() {
        spreadTypeLookup = new HashMap<>();
        spreadTypeLookup.put(Items.POTION, new PotionSpreadType());
        for (SpreadType spreadType : spreadTypes.values()) {
            spreadTypeLookup.put(spreadType.getIngredient(), spreadType);
        }
    }

    private void sendSyncingPackets() {
        NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new SpreadTypeSyncPacket(spreadTypes));
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new SpreadTypeSyncPacket(spreadTypes));
        }
    }
}
