package someasseblyrequired;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import someasseblyrequired.common.init.Blocks;
import someasseblyrequired.common.init.Items;
import someasseblyrequired.common.init.RecipeTypes;
import someasseblyrequired.common.init.TileEntityTypes;
import someasseblyrequired.common.item.spreadtype.SpreadTypeManager;
import someasseblyrequired.common.network.NetworkHandler;

@Mod(SomeAssemblyRequired.MODID)
public class SomeAssemblyRequired {

    public static final String MODID = "someassemblyrequired";

    public static final Logger LOGGER = LogManager.getLogger();

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> {
                NetworkHandler.register();
                RecipeTypes.registerBrewingRecipes();
            });
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(TileEntityTypes::addRenderers);
            ItemModelsProperties.registerProperty(Items.SPREAD, new ResourceLocation(SomeAssemblyRequired.MODID, "is_on_loaf"), (stack, world, entity) -> stack.hasTag() && stack.getOrCreateTag().contains("IsOnLoaf") && stack.getOrCreateTag().getBoolean("IsOnLoaf") ? 1 : 0);
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            Items.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            Blocks.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> event) {
            TileEntityTypes.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
            RecipeTypes.register(event.getRegistry());
        }
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEvents {

        @SubscribeEvent
        public static void registerColorHandlers(ColorHandlerEvent.Item event) {
            event.getItemColors().register((itemStack, tintIndex) -> tintIndex == 0 && itemStack.getOrCreateTag().getInt("Color") != 0 ? itemStack.getOrCreateTag().getInt("Color") : 0xFF00FF, Items.SPREAD);
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
