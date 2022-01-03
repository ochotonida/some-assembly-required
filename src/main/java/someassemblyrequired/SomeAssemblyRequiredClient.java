package someassemblyrequired;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.common.block.redstonetoaster.RedstoneToasterRenderer;
import someassemblyrequired.common.block.sandwich.SandwichBlockRenderer;
import someassemblyrequired.common.block.sandwichassemblytable.SandwichAssemblyTableBlockRenderer;
import someassemblyrequired.common.init.ModBlockEntityTypes;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.util.Util;

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
            BlockEntityRenderers.register(ModBlockEntityTypes.SANDWICH_ASSEMBLY_TABLE.get(), SandwichAssemblyTableBlockRenderer::new);

            ItemProperties.register(ModItems.SPREAD.get(), Util.id("on_loaf"), (stack, world, entity, i) -> stack.hasTag() && stack.getOrCreateTag().getBoolean("IsOnLoaf") ? 1 : 0);

            ForgeRegistries.ITEMS.getValues().stream().filter(item -> item.getRegistryName() != null && SomeAssemblyRequired.MODID.equals(item.getRegistryName().getNamespace())).filter(Item::isEdible).forEach(item ->
                    ItemProperties.register(item, Util.id("on_sandwich"), (stack, world, entity, i) -> stack.hasTag() && stack.getOrCreateTag().getBoolean("IsOnSandwich") ? 1 : 0)
            );

            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LETTUCE.get(), RenderType.cutout());
        });
    }

    public void onRegisterColorHandlers(ColorHandlerEvent.Item event) {
        event.getItemColors().register((itemStack, tintIndex) -> {
            CompoundTag tag = itemStack.getTag();
            if (tag != null && tintIndex == 0 && tag.getInt("Color") != 0) {
                return tag.getInt("Color");
            }
            return 0xFF00FF;
        }, ModItems.SPREAD.get());
    }
}
