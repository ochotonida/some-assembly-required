package someassemblyrequired.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.vector.Vector3f;
import someassemblyrequired.common.block.tileentity.RedstoneToasterTileEntity;

public class RedstoneToasterRenderer extends TileEntityRenderer<RedstoneToasterTileEntity> {

    public RedstoneToasterRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(RedstoneToasterTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.push();
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.scale(0.5F, 0.5F, 0.5F);

        if (tileEntity.getWorld() != null && tileEntity.getWorld().getBlockState(tileEntity.getPos()).hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            matrixStack.rotate(Vector3f.YP.rotationDegrees(90 - tileEntity.getWorld().getBlockState(tileEntity.getPos()).get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle()));
        }

        matrixStack.translate(0, 2 / 16D, 0);
        if (tileEntity.isToasting()) {
            matrixStack.translate(0, -4 / 16D, 0);
        }

        matrixStack.translate(0, 0, -3 / 16D);
        Minecraft.getInstance().getItemRenderer().renderItem(tileEntity.getItem(0), ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);
        matrixStack.translate(0, 0, 6 / 16D);
        Minecraft.getInstance().getItemRenderer().renderItem(tileEntity.getItem(1), ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);

        matrixStack.pop();
    }
}
