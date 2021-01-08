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
import someasseblyrequired.common.init.Items;

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

            if (tileEntity.getWorld() != null && tileEntity.getWorld().getBlockState(tileEntity.getPos()).hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                matrixStack.rotate(Vector3f.YP.rotationDegrees(-tileEntity.getWorld().getBlockState(tileEntity.getPos()).get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle()));
            }

            boolean isBlock = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(ingredient, tileEntity.getWorld(), null).isGui3d();
            boolean isSandwich = ingredient.getItem() == Items.SANDWICH.get();
            boolean isKnife = tileEntity.hasKnife();
            ItemCameraTransforms.TransformType transformType = ItemCameraTransforms.TransformType.FIXED;

            if (isSandwich || !isBlock) {
                if (isSandwich) {
                    transformType = ItemCameraTransforms.TransformType.GROUND;
                } else {
                    matrixStack.scale(0.5F, 0.5F, 0.5F);
                    if (isKnife) {
                        matrixStack.translate(0, 4.999 / 16, 0);
                        matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
                    } else {
                        matrixStack.translate(0, 0.501 / 16, 0);
                        matrixStack.rotate(Vector3f.XP.rotationDegrees(90));
                    }
                }
            } else {
                matrixStack.translate(0, 3.001 / 16, 0);
                matrixStack.scale(0.75F, 0.75F, 0.75F);
            }

            Minecraft.getInstance().getItemRenderer().renderItem(ingredient, transformType, combinedLight, combinedOverlay, matrixStack, buffer);
            matrixStack.pop();
        }
    }
}
