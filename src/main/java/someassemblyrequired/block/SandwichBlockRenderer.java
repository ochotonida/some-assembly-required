package someassemblyrequired.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import someassemblyrequired.item.sandwich.SandwichItemHandler;
import someassemblyrequired.item.sandwich.SandwichItemRenderer;

public class SandwichBlockRenderer implements BlockEntityRenderer<BlockEntity> {

    @SuppressWarnings("unused")
    public SandwichBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int overlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 1 / 64D, 0.5);
        poseStack.scale(0.5F, 0.5F, 0.5F);
        if (blockEntity.getLevel() != null) {
            BlockState state = blockEntity.getLevel().getBlockState(blockEntity.getBlockPos());
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                poseStack.mulPose(Axis.YP.rotationDegrees(180 - state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()));
            }
        }

        SandwichItemHandler.get(blockEntity).ifPresent(sandwich -> SandwichItemRenderer.renderSandwich(sandwich, poseStack, buffer, packedLight, overlay, blockEntity.getBlockPos().asLong()));
        poseStack.popPose();
    }
}
