package someassemblyrequired.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import someassemblyrequired.common.block.tileentity.CuttingBoardTileEntity;
import someassemblyrequired.common.init.ModItems;

public class CuttingBoardRenderer extends BlockEntityRenderer<CuttingBoardTileEntity> {

    public CuttingBoardRenderer(BlockEntityRenderDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(CuttingBoardTileEntity tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemStack ingredient = tileEntity.getIngredient();
        if (!ingredient.isEmpty()) {
            matrixStack.pushPose();

            matrixStack.translate(0.5, 1 / 16D, 0.5);

            if (tileEntity.getLevel() != null && tileEntity.getLevel().getBlockState(tileEntity.getBlockPos()).hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(-tileEntity.getLevel().getBlockState(tileEntity.getBlockPos()).getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()));
            }

            boolean isBlock = Minecraft.getInstance().getItemRenderer().getModel(ingredient, tileEntity.getLevel(), null).isGui3d();
            boolean isSandwich = ingredient.getItem() == ModItems.SANDWICH.get();
            boolean isKnife = tileEntity.hasKnife();
            ItemTransforms.TransformType transformType = ItemTransforms.TransformType.FIXED;

            if (isSandwich || !isBlock) {
                if (isSandwich) {
                    transformType = ItemTransforms.TransformType.GROUND;
                } else {
                    matrixStack.scale(0.5F, 0.5F, 0.5F);
                    if (isKnife) {
                        matrixStack.translate(0, 4.999 / 16, 0);
                        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
                    } else {
                        matrixStack.translate(0, 0.501 / 16, 0);
                        matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
                    }
                }
            } else {
                matrixStack.translate(0, 3.001 / 16, 0);
                matrixStack.scale(0.75F, 0.75F, 0.75F);
            }

            Minecraft.getInstance().getItemRenderer().renderStatic(ingredient, transformType, combinedLight, combinedOverlay, matrixStack, buffer);
            matrixStack.popPose();
        }
    }
}
