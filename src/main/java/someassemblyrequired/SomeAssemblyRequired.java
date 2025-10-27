package someassemblyrequired;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import someassemblyrequired.config.ModConfig;
import someassemblyrequired.data.SomeAssemblyRequiredData;
import someassemblyrequired.event.BlockEventHandler;
import someassemblyrequired.ingredient.IngredientProperties;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.registry.*;

import java.util.function.Consumer;

@Mod(SomeAssemblyRequired.MOD_ID)
public class SomeAssemblyRequired {

    public static final String MOD_ID = "someassemblyrequired";

    public static final Logger LOGGER = LogManager.getLogger();

    public SomeAssemblyRequired(IEventBus modEventBus, Dist dist, ModContainer container) {
        if (dist == Dist.CLIENT) {
            new SomeAssemblyRequiredClient(modEventBus, container);
        }
        container.registerConfig(net.neoforged.fml.config.ModConfig.Type.SERVER, ModConfig.serverSpec);

        ModCompat.setup(modEventBus);

        register(modEventBus,
                ModItems.ITEMS,
                ModBlocks.BLOCKS,
                ModStatistics.CUSTOM_STATS,
                ModItems.CREATIVE_MODE_TABS,
                ModSoundEvents.SOUND_EVENTS,
                ModRecipeTypes.RECIPE_TYPES,
                ModLootModifiers.LOOT_MODIFIERS,
                ModBlockEntityTypes.ENTITY_TYPES,
                ModRecipeTypes.RECIPE_SERIALIZERS,
                ModLootFunctions.LOOT_FUNCTION_TYPES,
                ModDataComponents.DATA_COMPONENT_TYPES,
                ModLootConditions.LOOT_CONDITION_TYPES,
                ModLootPoolEntries.LOOT_POOL_ENTRY_TYPES,
                ModAdvancementTriggers.CRITERIA_TRIGGERS,
                ModItemSubPredicateTypes.ITEM_SUB_PREDICATE_TYPES
        );

        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(SomeAssemblyRequiredData::gatherData);
        modEventBus.addListener(this::registerDataPackRegistries);
        modEventBus.addListener(ModPayloadTypes::register);

        NeoForge.EVENT_BUS.addListener(ModIngredients::onDataPackSync);

        BlockEventHandler.register();
    }

    private void registerDataPackRegistries(DataPackRegistryEvent.NewRegistry event) {
        Consumer<RegistryBuilder<IngredientProperties>> callback = builder -> builder
                .onBake(registry -> ModIngredients.refresh(registry.asLookup()));
        event.dataPackRegistry(ModIngredients.INGREDIENTS, IngredientProperties.CODEC, IngredientProperties.CODEC, callback);
    }

    private static void register(IEventBus modEventBus, DeferredRegister<?>... registers) {
        for (DeferredRegister<?> register : registers) {
            register.register(modEventBus);
        }
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(Ingredients::addBehaviors);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registryKey, String path) {
        return ResourceKey.create(registryKey, id(path));
    }

    public static MutableComponent translate(String key, Object... args) {
        return Component.translatable("%s.%s".formatted(MOD_ID, key), args);
    }
}
