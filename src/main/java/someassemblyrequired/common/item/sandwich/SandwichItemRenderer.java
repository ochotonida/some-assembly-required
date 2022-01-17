package someassemblyrequired.common.item.sandwich;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import someassemblyrequired.common.ingredient.Ingredients;
import someassemblyrequired.common.init.ModTags;

import java.util.Random;

public class SandwichItemRenderer extends BlockEntityWithoutLevelRenderer {

    private static final ItemStack BARRIER = new ItemStack(Items.BARRIER);
    private static final Random random = new Random();

    public SandwichItemRenderer() {
        // noinspection ConstantConditions
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int overlay) {
        SandwichItemHandler.get(stack).ifPresent(sandwich -> {
            poseStack.pushPose();

            poseStack.translate(0.5, 0.5 + 1 / 32D, 0.5);

            int size = sandwich.size();
            if (transformType == ItemTransforms.TransformType.GUI) {
                if (size > 16) {
                    float scale = 24 / (size + 8F);
                    poseStack.scale(scale, scale, scale);
                }
            }

            if (transformType.firstPerson() || transformType == ItemTransforms.TransformType.GUI) {
                poseStack.translate(0, -size / 32D, 0);
            }

            if (size == 0) {
                poseStack.mulPose(Vector3f.XP.rotationDegrees(90));
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(180));
                Minecraft.getInstance().getItemRenderer().renderStatic(BARRIER, ItemTransforms.TransformType.FIXED, packedLight, overlay, poseStack, buffer, 0);
            } else {
                renderSandwich(sandwich, poseStack, buffer, packedLight, overlay, 0);
            }

            poseStack.popPose();
        });
    }

    public static void renderSandwich(SandwichItemHandler sandwich, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int overlay, long seed) {
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.XP.rotationDegrees(90));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(180));
        random.setSeed(seed);
        for (int slot = 0; slot < sandwich.size(); slot++) {
            poseStack.pushPose();
            ItemStack stack = sandwich.getStackInSlot(slot);
            if (!stack.is(ModTags.SANDWICH_BREAD)) {
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 20 - 10));
            }
            stack = Ingredients.getDisplayItem(stack);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.FIXED, packedLight, overlay, poseStack, buffer, (int) seed + slot);
            poseStack.popPose();
            poseStack.translate(0, 0, -1 / 16D - 0.001);
        }
        poseStack.popPose();
    }
}
