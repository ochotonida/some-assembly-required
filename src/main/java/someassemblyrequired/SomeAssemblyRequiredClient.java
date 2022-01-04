package someassemblyrequired;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import someassemblyrequired.common.block.redstonetoaster.RedstoneToasterRenderer;
import someassemblyrequired.common.block.sandwich.SandwichBlockRenderer;
import someassemblyrequired.common.init.ModBlockEntityTypes;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;

public class SomeAssemblyRequiredClient {

    public SomeAssemblyRequiredClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onRegisterColorHandlers);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(ModBlockEntityTypes.REDSTONE_TOASTER.get(), RedstoneToasterRenderer::new);
            BlockEntityRenderers.register(ModBlockEntityTypes.STICKY_REDSTONE_TOASTER.get(), RedstoneToasterRenderer::new);
            BlockEntityRenderers.register(ModBlockEntityTypes.SANDWICH.get(), SandwichBlockRenderer::new);
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LETTUCE.get(), RenderType.cutout());
        });
    }

    public void onRegisterColorHandlers(ColorHandlerEvent.Item event) {
        event.getItemColors().register((itemStack, tintIndex) -> {
            CompoundTag tag = itemStack.getTag();
            if (tag != null && tintIndex == 0 && tag.contains("Color", Tag.TAG_INT)) {
                return tag.getInt("Color");
            }
            return 0xFF00FF;
        }, ModItems.SPREAD.get());
    }
}
