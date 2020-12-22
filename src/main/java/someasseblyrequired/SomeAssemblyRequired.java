package someasseblyrequired;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import someasseblyrequired.common.init.*;
import someasseblyrequired.common.item.spreadtype.SpreadType;

@Mod(SomeAssemblyRequired.MODID)
public class SomeAssemblyRequired {

    public static final String MODID = "someassemblyrequired";

    public static final ItemGroup CREATIVE_TAB = new ItemGroup(MODID) {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(Items.BREAD_SLICE);
        }
    };

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            TileEntityTypes.addRenderers();
        }

        @SubscribeEvent
        public static void addRegistry(RegistryEvent.NewRegistry event) {
            SpreadTypes.createRegistry();
        }

        @SubscribeEvent
        public static void registerSpreads(RegistryEvent.Register<SpreadType> event) {
            SpreadTypes.register(event.getRegistry());
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
}
