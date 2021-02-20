package someassemblyrequired.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import someassemblyrequired.client.render.SandwichRenderHelper;
import someassemblyrequired.common.block.tileentity.ItemHandlerTileEntity;

@OnlyIn(Dist.CLIENT)
public class SandwichBlockRenderer extends TileEntityRenderer<ItemHandlerTileEntity> {

    public SandwichBlockRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(ItemHandlerTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.push();

        matrixStack.translate(0.5, 0, 0.5);
        matrixStack.scale(0.5F, 0.5F, 0.5F);

        float rotationDegrees = 0;
        if (tileEntity.getWorld() != null && tileEntity.getWorld().getBlockState(tileEntity.getPos()).hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            rotationDegrees = -tileEntity.getWorld().getBlockState(tileEntity.getPos()).get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle();
        }

        SandwichRenderHelper.renderSandwich(tileEntity.getInventory(), rotationDegrees, matrixStack, buffer, combinedLight, combinedOverlay);

        matrixStack.pop();
    }
}
