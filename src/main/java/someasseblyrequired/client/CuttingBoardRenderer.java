package someasseblyrequired.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.vector.Vector3f;
import someasseblyrequired.common.block.tileentity.CuttingBoardTileEntity;

public class CuttingBoardRenderer extends TileEntityRenderer<CuttingBoardTileEntity> {

    public CuttingBoardRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(CuttingBoardTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        ItemStack ingredient = tileEntity.getIngredient();
        if (!ingredient.isEmpty()) {
            matrixStack.push();

            matrixStack.translate(0.5, 1 / 16D, 0.5);

            matrixStack.scale(0.5F, 0.5F, 0.5F);

            if (tileEntity.getWorld() != null) {
                matrixStack.rotate(Vector3f.YP.rotationDegrees(-tileEntity.getWorld().getBlockState(tileEntity.getPos()).get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle()));
            }

            matrixStack.translate(0, 1.001 / 32, 0);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(90));

            Minecraft.getInstance().getItemRenderer().renderItem(ingredient, ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);

            matrixStack.pop();
        }
    }
}
