package someassemblyrequired;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import someassemblyrequired.client.SomeAssemblyRequiredClient;
import someassemblyrequired.common.init.*;

@Mod(SomeAssemblyRequired.MODID)
public class SomeAssemblyRequired {

    public static final String MODID = "someassemblyrequired";

    public static final Logger LOGGER = LogManager.getLogger();

    public SomeAssemblyRequired() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.REGISTRY.register(modEventBus);
        ModBlocks.REGISTRY.register(modEventBus);
        ModRecipeTypes.REGISTRY.register(modEventBus);
        ModTileEntityTypes.REGISTRY.register(modEventBus);
        ModSoundEvents.REGISTRY.register(modEventBus);
        ModAdvancementTriggers.register();

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> SomeAssemblyRequiredClient::addReloadListeners);
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> {
                ModRecipeTypes.registerBrewingRecipes();
                ModItems.registerCompostables();
            });
        }
    }
}
