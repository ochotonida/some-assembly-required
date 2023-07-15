package someassemblyrequired.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import someassemblyrequired.config.ModConfig;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.item.sandwich.SandwichItemHandler;
import someassemblyrequired.recipe.SandwichSpoutingRecipe;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModRecipeTypes;
import someassemblyrequired.registry.ModTags;

@Pseudo
@Mixin(com.simibubi.create.content.fluids.spout.FillingBySpout.class)
public class FillingBySpoutMixin {

    @Inject(method = "canItemBeFilled", remap = false, cancellable = true, at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lcom/simibubi/create/content/fluids/transfer/GenericItemFilling;canItemBeFilled(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Z", remap = false))
    private static void canSandwichBeFilled(Level level, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.is(ModTags.SANDWICH_BREAD) || stack.is(ModItems.SANDWICH.get())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getRequiredAmountForItem", remap = false, cancellable = true, at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lcom/simibubi/create/content/fluids/transfer/GenericItemFilling;getRequiredAmountForItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraftforge/fluids/FluidStack;)I", remap = false))
    private static void getRequiredAmountForItem(Level level, ItemStack stack, FluidStack availableFluid, CallbackInfoReturnable<Integer> cir) {
        if (!stack.is(ModItems.SANDWICH.get()) && !stack.is(ModTags.SANDWICH_BREAD)) {
            return;
        }
        SandwichSpoutingRecipe recipe = getMatchingRecipe(availableFluid, level);

        int resultHeight = SandwichItemHandler.get(stack)
                .filter(h -> recipe != null)
                .map(s -> s.getTotalHeight() + 1)
                .orElse(1);
        if (recipe != null && resultHeight <= ModConfig.server.maximumSandwichHeight.get()) {
            cir.setReturnValue(recipe.getAmountRequired(availableFluid));
        }
    }

    @Inject(method = "fillItem", remap = false, cancellable = true, at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lcom/simibubi/create/content/fluids/transfer/GenericItemFilling;fillItem(Lnet/minecraft/world/level/Level;ILnet/minecraft/world/item/ItemStack;Lnet/minecraftforge/fluids/FluidStack;)Lnet/minecraft/world/item/ItemStack;", remap = false))
    private static void fillSandwich(Level level, int requiredAmount, ItemStack stack, FluidStack availableFluid, CallbackInfoReturnable<ItemStack> cir) {
        if (!stack.is(ModItems.SANDWICH.get()) && !stack.is(ModTags.SANDWICH_BREAD)) {
            return;
        }
        SandwichSpoutingRecipe recipe = getMatchingRecipe(availableFluid, level);
        if (recipe == null) {
            return;
        }

        ItemStack existing = stack.split(1);
        ItemStack sandwich = SandwichItem.of(existing, recipe.assemble(availableFluid.copy()));
        availableFluid.shrink(requiredAmount);
        cir.setReturnValue(sandwich);
    }

    private static SandwichSpoutingRecipe getMatchingRecipe(FluidStack fluid, Level level) {
        for (SandwichSpoutingRecipe recipe : level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.SANDWICH_SPOUTING.get())) {
            if (recipe.matches(fluid)) {
                return recipe;
            }
        }
        return null;
    }
}
