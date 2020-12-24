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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import someasseblyrequired.common.block.tileentity.ItemHandlerTileEntity;

@OnlyIn(Dist.CLIENT)
public class SandwichBlockRenderer extends TileEntityRenderer<ItemHandlerTileEntity> {

    public SandwichBlockRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(ItemHandlerTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.push();
        matrixStack.translate(0.5, 1 / 64D, 0.5);
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        if (tileEntity.getWorld() != null) {
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-tileEntity.getWorld().getBlockState(tileEntity.getPos()).get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle()));
        }
        matrixStack.rotate(Vector3f.XP.rotationDegrees(90));
        for (ItemStack ingredient : tileEntity.getItems()) {
            Minecraft.getInstance().getItemRenderer().renderItem(ingredient, ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);
            matrixStack.translate(0, 0, -1 / 16D - 0.001);
        }
        matrixStack.pop();
    }
}
