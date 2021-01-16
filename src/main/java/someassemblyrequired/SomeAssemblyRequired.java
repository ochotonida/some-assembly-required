package someassemblyrequired;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import someassemblyrequired.common.init.*;
import someassemblyrequired.common.item.spreadtype.SpreadTypeManager;
import someassemblyrequired.common.network.NetworkHandler;

@Mod(SomeAssemblyRequired.MODID)
public class SomeAssemblyRequired {

    public static final String MODID = "someassemblyrequired";

    public static final Logger LOGGER = LogManager.getLogger();

    public SomeAssemblyRequired() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Items.REGISTRY.register(modEventBus);
        Blocks.REGISTRY.register(modEventBus);
        RecipeTypes.REGISTRY.register(modEventBus);
        TileEntityTypes.REGISTRY.register(modEventBus);
        AdvancementTriggers.register();
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> {
                NetworkHandler.register();
                RecipeTypes.registerBrewingRecipes();
                Items.registerCompostables();
            });
        }
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(TileEntityTypes::addRenderers);
            ItemModelsProperties.registerProperty(Items.SPREAD.get(), new ResourceLocation(SomeAssemblyRequired.MODID, "on_loaf"), (stack, world, entity) -> stack.hasTag() && stack.getOrCreateTag().getBoolean("IsOnLoaf") ? 1 : 0);
            for (Item item : new Item[]{
                    Items.APPLE_SLICES.get(),
                    Items.GOLDEN_APPLE_SLICES.get(),
                    Items.ENCHANTED_GOLDEN_APPLE_SLICES.get(),
                    Items.CHOPPED_CARROT.get(),
                    Items.CHOPPED_GOLDEN_CARROT.get(),
                    Items.CHOPPED_BEETROOT.get(),
                    Items.PORK_CUTS.get(),
                    Items.BACON_STRIPS.get(),
                    Items.SLICED_TOASTED_CRIMSON_FUNGUS.get(),
                    Items.SLICED_TOASTED_WARPED_FUNGUS.get(),
                    Items.TOMATO_SLICES.get(),
                    Items.LETTUCE_LEAF.get()
            }) {
                ItemModelsProperties.registerProperty(item, new ResourceLocation(SomeAssemblyRequired.MODID, "on_sandwich"), (stack, world, entity) -> stack.hasTag() && stack.getOrCreateTag().getBoolean("IsOnSandwich") ? 1 : 0);
            }

            RenderTypeLookup.setRenderLayer(Blocks.LETTUCE.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(Blocks.TOMATOES.get(), RenderType.getCutout());
        }

        @SubscribeEvent
        public static void registerColorHandlers(ColorHandlerEvent.Item event) {
            event.getItemColors().register((itemStack, tintIndex) -> tintIndex == 0 && itemStack.getOrCreateTag().getInt("Color") != 0 ? itemStack.getOrCreateTag().getInt("Color") : 0xFF00FF, Items.SPREAD.get());
        }
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber
    public static class Events {

        @SubscribeEvent
        public static void addReloadListeners(AddReloadListenerEvent event) {
            event.addListener(SpreadTypeManager.INSTANCE);
        }
    }
}
