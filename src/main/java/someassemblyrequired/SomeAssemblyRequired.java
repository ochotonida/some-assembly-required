package someassemblyrequired;

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
import someassemblyrequired.common.config.ModConfig;
import someassemblyrequired.common.event.BlockEventHandler;
import someassemblyrequired.common.ingredient.IngredientPropertiesManager;
import someassemblyrequired.common.ingredient.Ingredients;
import someassemblyrequired.common.init.*;
import someassemblyrequired.common.network.NetworkHandler;
import someassemblyrequired.integration.ModCompat;

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
                ModBlocks.BLOCKS,
                ModBlockEntityTypes.ENTITY_TYPES,
                ModSoundEvents.SOUND_EVENTS,
                ModRecipeTypes.RECIPE_TYPES,
                ModRecipeTypes.RECIPE_SERIALIZERS,
                ModLootModifiers.LOOT_MODIFIERS,
                ModLootFunctions.LOOT_FUNCTION_TYPES,
                ModLootConditions.LOOT_CONDITION_TYPES
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
}
