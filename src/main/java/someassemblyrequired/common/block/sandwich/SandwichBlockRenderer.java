package someassemblyrequired.common.block.sandwich;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import someassemblyrequired.common.item.sandwich.SandwichItemHandler;

public class SandwichBlockRenderer implements BlockEntityRenderer<BlockEntity> {

    @SuppressWarnings("unused")
    public SandwichBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        matrixStack.translate(0.5, 1 / 64D, 0.5);
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        if (blockEntity.getLevel() != null && blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()).hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()).getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()));
        }
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
        int i = (int) blockEntity.getBlockPos().asLong();

        SandwichItemHandler.get(blockEntity)
                .ifPresent(handler -> {
                    for (ItemStack item : handler.getItems()) {
                        Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer, i);
                        matrixStack.translate(0, 0, -1 / 16D - 0.001);
                    }
                });

        matrixStack.popPose();
    }
}
