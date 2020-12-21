package someasseblyrequired.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.items.CapabilityItemHandler;

public class SandwichItemRenderer extends ItemStackTileEntityRenderer {

    private static final ItemStack BARRIER = new ItemStack(Items.BARRIER);

    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            matrixStack.push();

            matrixStack.translate(0.5, 0.5 + 1 / 32D, 0.5);

            int size;
            for (size = 0; size < handler.getSlots() && !handler.getStackInSlot(size).isEmpty(); size++) ;

            if (transformType == ItemCameraTransforms.TransformType.GUI) {
                if (size > 16) {
                    matrixStack.scale(16F / size, 16F / size, 16F / size);
                }
            }

            if (transformType.isFirstPerson() || transformType == ItemCameraTransforms.TransformType.GUI) {
                matrixStack.translate(0, -size / 32D, 0);
            }

            matrixStack.rotate(Vector3f.XP.rotationDegrees(90));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(180));

            if (size == 0) {
                Minecraft.getInstance().getItemRenderer().renderItem(BARRIER, ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);
            } else {
                for (int slot = 0; slot < size; slot++) {
                    Minecraft.getInstance().getItemRenderer().renderItem(handler.getStackInSlot(slot), ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);
                    matrixStack.translate(0, 0, -1 / 16D - 0.001);
                }
            }

            matrixStack.pop();
        });
    }
}
