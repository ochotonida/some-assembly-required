package someassemblyrequired;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import someassemblyrequired.common.init.*;
import someassemblyrequired.common.item.spreadtype.SpreadTypeManager;
import someassemblyrequired.common.network.NetworkHandler;

@Mod(SomeAssemblyRequired.MODID)
public class SomeAssemblyRequired {

    public static final String MODID = "some_assembly_required";

    public static final Logger LOGGER = LogManager.getLogger();

    public SomeAssemblyRequired() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> SomeAssemblyRequiredClient::new);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.REGISTRY.register(modEventBus);
        ModBlocks.REGISTRY.register(modEventBus);
        ModRecipeTypes.REGISTRY.register(modEventBus);
        ModBlockEntityTypes.REGISTRY.register(modEventBus);
        ModSoundEvents.REGISTRY.register(modEventBus);

        modEventBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.addListener(this::onAddReloadListeners);

        ModAdvancementTriggers.register();
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NetworkHandler.register();
            ModRecipeTypes.registerBrewingRecipes();
            ModItems.registerCompostables();
        });
    }

    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(SpreadTypeManager.INSTANCE);
    }
}
