package someassemblyrequired.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import someassemblyrequired.common.block.ItemHandlerBlockEntity;

public class SandwichAssemblyTableBlockRenderer extends SandwichBlockRenderer {

    @SuppressWarnings("unused")
    public SandwichAssemblyTableBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ItemHandlerBlockEntity tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        matrixStack.translate(0, 1, 0);
        if (tileEntity.getLevel() != null) {
            combinedLight = LevelRenderer.getLightColor(tileEntity.getLevel(), tileEntity.getBlockPos().above());
        }
        super.render(tileEntity, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        matrixStack.popPose();
    }
}
