package someassemblyrequired.common.block.redstonetoaster;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RedstoneToasterRenderer implements BlockEntityRenderer<RedstoneToasterBlockEntity> {

    @SuppressWarnings("unused")
    public RedstoneToasterRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(RedstoneToasterBlockEntity blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.scale(0.5F, 0.5F, 0.5F);

        if (blockEntity.getLevel() != null && blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()).hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90 - blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()).getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()));
        }

        matrixStack.translate(0, 2 / 16D, 0);
        if (blockEntity.isToasting()) {
            matrixStack.translate(0, -4 / 16D, 0);
        }

        int i = (int) blockEntity.getBlockPos().asLong();

        matrixStack.translate(0, 0, -3 / 16D);
        Minecraft.getInstance().getItemRenderer().renderStatic(blockEntity.getItem(0), ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer, i);
        matrixStack.translate(0, 0, 6 / 16D);
        Minecraft.getInstance().getItemRenderer().renderStatic(blockEntity.getItem(1), ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer, i);

        matrixStack.popPose();
    }
}
