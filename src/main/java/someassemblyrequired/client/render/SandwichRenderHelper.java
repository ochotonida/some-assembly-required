package someassemblyrequired.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.items.IItemHandler;
import someassemblyrequired.client.ingredient.IngredientInfo;
import someassemblyrequired.client.ingredient.IngredientInfoManager;
import someassemblyrequired.common.init.ModItems;

public class SandwichRenderHelper {

    private static final ItemStack SPREAD;
    private static final ItemStack TRANSLUCENT_SPREAD;

    static {
        SPREAD = new ItemStack(ModItems.SPREAD.get());
        SPREAD.getOrCreateTag();
        TRANSLUCENT_SPREAD = new ItemStack(ModItems.TRANSLUCENT_SPREAD.get());
        TRANSLUCENT_SPREAD.getOrCreateTag();
    }

    @SuppressWarnings("ConstantConditions")
    public static void renderSandwich(IItemHandler handler, float rotationDegrees, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.push();

        matrixStack.translate(0, 1 / 32D, 0);

        matrixStack.rotate(Vector3f.YP.rotationDegrees(rotationDegrees));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(90));

        for (int slot = 0; slot < handler.getSlots() && !handler.getStackInSlot(slot).isEmpty(); slot++) {
            ItemStack stack = handler.getStackInSlot(slot);
            IngredientInfo.DisplayInfo displayInfo = IngredientInfoManager.INSTANCE.getDisplayInfo(stack.getItem());
            if (displayInfo != null) {
                boolean isOnLoaf = stack.hasTag() && stack.getTag().getBoolean("IsOnLoaf");
                stack = displayInfo.isTranslucent() ? TRANSLUCENT_SPREAD : SPREAD;
                stack.getTag().putInt("Color", displayInfo.getColor());
                stack.getTag().putBoolean("IsOnLoaf", isOnLoaf);
            }
            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);
            matrixStack.translate(0, 0, -1 / 16D - 0.001);
        }

        matrixStack.pop();
    }
}
