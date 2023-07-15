package someassemblyrequired;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import someassemblyrequired.config.ModConfig;
import someassemblyrequired.event.BlockEventHandler;
import someassemblyrequired.ingredient.IngredientPropertiesManager;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.init.*;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.network.NetworkHandler;

@Mod(SomeAssemblyRequired.MODID)
public class SomeAssemblyRequired {

    public static final String MODID = "some_assembly_required";

    public static final Logger LOGGER = LogManager.getLogger();

    public SomeAssemblyRequired() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> SomeAssemblyRequiredClient::new);

        ModCompat.setup();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        register(modEventBus,
                ModItems.ITEMS,
                ModItems.CREATIVE_MODE_TABS,
                ModBlocks.BLOCKS,
                ModBlockEntityTypes.ENTITY_TYPES,
                ModSoundEvents.SOUND_EVENTS,
                ModRecipeTypes.RECIPE_TYPES,
                ModRecipeTypes.RECIPE_SERIALIZERS,
                ModLootModifiers.LOOT_MODIFIERS,
                ModLootFunctions.LOOT_FUNCTION_TYPES,
                ModLootConditions.LOOT_CONDITION_TYPES,
                ModLootPoolEntries.LOOT_POOL_ENTRY_TYPES
        );

        modEventBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.addListener(IngredientPropertiesManager::onAddReloadListener);
        MinecraftForge.EVENT_BUS.addListener(IngredientPropertiesManager::onDataPackReload);

        ModAdvancementTriggers.register();
        BlockEventHandler.register();
    }

    private static void register(IEventBus modEventBus, DeferredRegister<?>... registers) {
        for (DeferredRegister<?> register : registers) {
            register.register(modEventBus);
        }
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModConfig.registerServer();
            Ingredients.addBehaviors();
            NetworkHandler.register();
            ModItems.registerCompostables();
        });
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static MutableComponent translate(String key, Object... args) {
        return Component.translatable("%s.%s".formatted(MODID, key), args);
    }
}
