package someassemblyrequired.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.CapabilityItemHandler;

public class SandwichItemRenderer extends BlockEntityWithoutLevelRenderer {

    private static final ItemStack BARRIER = new ItemStack(Items.BARRIER);

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            matrixStack.pushPose();

            matrixStack.translate(0.5, 0.5 + 1 / 32D, 0.5);

            int size;
            for (size = 0; size < handler.getSlots() && !handler.getStackInSlot(size).isEmpty(); size++) ;

            if (transformType == ItemTransforms.TransformType.GUI) {
                if (size > 16) {
                    matrixStack.scale(16F / size, 16F / size, 16F / size);
                }
            }

            if (transformType.firstPerson() || transformType == ItemTransforms.TransformType.GUI || transformType == ItemTransforms.TransformType.FIXED) {
                matrixStack.translate(0, -size / 32D, 0);
            }

            matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));

            if (size == 0) {
                Minecraft.getInstance().getItemRenderer().renderStatic(BARRIER, ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);
            } else {
                for (int slot = 0; slot < size; slot++) {
                    Minecraft.getInstance().getItemRenderer().renderStatic(handler.getStackInSlot(slot), ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);
                    matrixStack.translate(0, 0, -1 / 16D - 0.001);
                }
            }

            matrixStack.popPose();
        });
    }
}
