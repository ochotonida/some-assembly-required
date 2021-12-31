package someassemblyrequired.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import someassemblyrequired.common.block.ItemHandlerBlockEntity;

public class SandwichBlockRenderer implements BlockEntityRenderer<ItemHandlerBlockEntity> {

    @SuppressWarnings("unused")
    public SandwichBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ItemHandlerBlockEntity tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        matrixStack.translate(0.5, 1 / 64D, 0.5);
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        if (tileEntity.getLevel() != null && tileEntity.getLevel().getBlockState(tileEntity.getBlockPos()).hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-tileEntity.getLevel().getBlockState(tileEntity.getBlockPos()).getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()));
        }
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
        int i = (int) tileEntity.getBlockPos().asLong();
        for (ItemStack ingredient : tileEntity.getItems()) {
            Minecraft.getInstance().getItemRenderer().renderStatic(ingredient, ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer, i);
            matrixStack.translate(0, 0, -1 / 16D - 0.001);
        }
        matrixStack.popPose();
    }
}
