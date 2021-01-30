package someassemblyrequired;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import someassemblyrequired.common.init.*;
import someassemblyrequired.common.item.spreadtype.SpreadTypeManager;
import someassemblyrequired.common.network.NetworkHandler;
import someassemblyrequired.common.util.Util;

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
        ModAdvancementTriggers.register();
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> {
                NetworkHandler.register();
                ModRecipeTypes.registerBrewingRecipes();
                ModItems.registerCompostables();
            });
        }
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                ModTileEntityTypes.addRenderers();
                ItemModelsProperties.registerProperty(ModItems.SPREAD.get(), Util.prefix("on_loaf"), (stack, world, entity) -> stack.hasTag() && stack.getOrCreateTag().getBoolean("IsOnLoaf") ? 1 : 0);

                ForgeRegistries.ITEMS.getValues().stream().filter(item -> item.getRegistryName() != null && SomeAssemblyRequired.MODID.equals(item.getRegistryName().getNamespace())).filter(Item::isFood).forEach(item ->
                        ItemModelsProperties.registerProperty(item, Util.prefix("on_sandwich"), (stack, world, entity) -> stack.hasTag() && stack.getOrCreateTag().getBoolean("IsOnSandwich") ? 1 : 0)
                );

                RenderTypeLookup.setRenderLayer(ModBlocks.LETTUCE.get(), RenderType.getCutout());
                RenderTypeLookup.setRenderLayer(ModBlocks.TOMATOES.get(), RenderType.getCutout());
            });
        }

        @SubscribeEvent
        public static void registerColorHandlers(ColorHandlerEvent.Item event) {
            event.getItemColors().register((itemStack, tintIndex) -> {
                CompoundNBT tag = itemStack.getTag();
                if (tag != null && tintIndex == 0 && tag.getInt("Color") != 0) {
                    return tag.getInt("Color");
                }
                return 0xFF00FF;
            }, ModItems.SPREAD.get());
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
