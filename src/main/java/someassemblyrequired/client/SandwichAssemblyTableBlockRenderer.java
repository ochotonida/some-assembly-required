package someassemblyrequired.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import someassemblyrequired.common.block.tileentity.ItemHandlerTileEntity;

public class SandwichAssemblyTableBlockRenderer extends SandwichBlockRenderer {

    public SandwichAssemblyTableBlockRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(ItemHandlerTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.push();
        matrixStack.translate(0, 1, 0);
        if (tileEntity.getWorld() != null) {
            combinedLight = WorldRenderer.getCombinedLight(tileEntity.getWorld(), tileEntity.getPos().up());
        }
        super.render(tileEntity, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        matrixStack.pop();
    }
}
