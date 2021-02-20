package someassemblyrequired.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import someassemblyrequired.client.ingredient.IngredientInfoManager;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTileEntityTypes;
import someassemblyrequired.common.util.Util;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SomeAssemblyRequiredClient {

    @SuppressWarnings("ConstantConditions")
    public static void addReloadListeners() {
        if (Minecraft.getInstance() == null) {
            return; // prevent datagen from crashing
        }

        IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager) resourceManager).addReloadListener(IngredientInfoManager.INSTANCE);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("ConstantConditions")
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemModelsProperties.registerGlobalProperty(
                    Util.prefix("on_sandwich"),
                    (stack, world, entity) -> stack.hasTag() && stack.getTag().getBoolean("IsOnSandwich") ? 1 : 0
            );
            ItemModelsProperties.registerGlobalProperty(
                    Util.prefix("on_loaf"),
                    (stack, world, entity) -> stack.hasTag() && stack.getTag().getBoolean("IsOnLoaf") ? 1 : 0
            );

            ModTileEntityTypes.addRenderers();

            RenderTypeLookup.setRenderLayer(ModBlocks.LETTUCE.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.TOMATOES.get(), RenderType.getCutout());
        });
    }

    @SubscribeEvent
    public static void registerColorHandlers(ColorHandlerEvent.Item event) {
        event.getItemColors().register((itemStack, tintIndex) -> itemStack.getOrCreateTag().getInt("Color"), ModItems.SPREAD.get());
        event.getItemColors().register((itemStack, tintIndex) -> itemStack.getOrCreateTag().getInt("Color"), ModItems.TRANSLUCENT_SPREAD.get());
    }
}
