package someassemblyrequired.common.block.sandwichassemblytable;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import someassemblyrequired.common.block.sandwich.SandwichBlockRenderer;

public class SandwichAssemblyTableBlockRenderer extends SandwichBlockRenderer {

    @SuppressWarnings("unused")
    public SandwichAssemblyTableBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        matrixStack.translate(0, 1, 0);
        if (blockEntity.getLevel() != null) {
            combinedLight = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos().above());
        }
        super.render(blockEntity, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        matrixStack.popPose();
    }
}
