package someassemblyrequired.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class SandwichItemRenderer extends ItemStackTileEntityRenderer {

    private static final IItemHandler BARRIER = new ItemStackHandler(NonNullList.from(ItemStack.EMPTY, new ItemStack(Items.BARRIER)));

    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            matrixStack.push();

            matrixStack.translate(0.5, 0.5, 0.5);

            int size;
            for (size = 0; size < handler.getSlots() && !handler.getStackInSlot(size).isEmpty(); size++) ;

            if (transformType.isFirstPerson() || transformType == ItemCameraTransforms.TransformType.GUI || transformType == ItemCameraTransforms.TransformType.FIXED) {
                matrixStack.translate(0, -size / 32D, 0);
            }

            SandwichRenderHelper.renderSandwich(size == 0 ? BARRIER : handler, 180, matrixStack, buffer, combinedLight, combinedOverlay);

            matrixStack.pop();
        });
    }
}
