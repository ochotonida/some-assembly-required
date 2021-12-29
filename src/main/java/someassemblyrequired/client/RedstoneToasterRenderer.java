package someassemblyrequired.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import someassemblyrequired.common.block.tileentity.RedstoneToasterTileEntity;

public class RedstoneToasterRenderer extends BlockEntityRenderer<RedstoneToasterTileEntity> {

    public RedstoneToasterRenderer(BlockEntityRenderDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(RedstoneToasterTileEntity tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.scale(0.5F, 0.5F, 0.5F);

        if (tileEntity.getLevel() != null && tileEntity.getLevel().getBlockState(tileEntity.getBlockPos()).hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90 - tileEntity.getLevel().getBlockState(tileEntity.getBlockPos()).getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()));
        }

        matrixStack.translate(0, 2 / 16D, 0);
        if (tileEntity.isToasting()) {
            matrixStack.translate(0, -4 / 16D, 0);
        }

        matrixStack.translate(0, 0, -3 / 16D);
        Minecraft.getInstance().getItemRenderer().renderStatic(tileEntity.getItem(0), ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);
        matrixStack.translate(0, 0, 6 / 16D);
        Minecraft.getInstance().getItemRenderer().renderStatic(tileEntity.getItem(1), ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);

        matrixStack.popPose();
    }
}
