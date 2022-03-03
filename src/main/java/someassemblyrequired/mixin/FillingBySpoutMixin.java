package someassemblyrequired.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import someassemblyrequired.common.config.ModConfig;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModRecipeTypes;
import someassemblyrequired.common.init.ModTags;
import someassemblyrequired.common.item.sandwich.SandwichItem;
import someassemblyrequired.common.item.sandwich.SandwichItemHandler;
import someassemblyrequired.common.recipe.SandwichSpoutingRecipe;

@Pseudo
@Mixin(com.simibubi.create.content.contraptions.fluids.actors.FillingBySpout.class)
public class FillingBySpoutMixin {

    @Inject(method = "canItemBeFilled", remap = false, cancellable = true, at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lcom/simibubi/create/content/contraptions/fluids/actors/GenericItemFilling;canItemBeFilled(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Z", remap = false))
    private static void canSandwichBeFilled(Level level, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.is(ModTags.SANDWICH_BREAD)) {
            cir.setReturnValue(true);
        } else if (stack.is(ModItems.SANDWICH.get())) {
            cir.setReturnValue(
                    SandwichItemHandler.get(stack)
                            .map(s -> s.size() < ModConfig.server.maximumSandwichHeight.get())
                            .orElse(false)
            );
        }
    }

    @Inject(method = "getRequiredAmountForItem", remap = false, cancellable = true, at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lcom/simibubi/create/content/contraptions/fluids/actors/GenericItemFilling;getRequiredAmountForItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraftforge/fluids/FluidStack;)I", remap = false))
    private static void getRequiredAmountForItem(Level level, ItemStack stack, FluidStack availableFluid, CallbackInfoReturnable<Integer> cir) {
        if (!stack.is(ModItems.SANDWICH.get()) && !stack.is(ModTags.SANDWICH_BREAD)) {
            return;
        }
        SandwichSpoutingRecipe recipe = getMatchingRecipe(availableFluid, level);
        if (recipe != null) {
            cir.setReturnValue(recipe.getAmountRequired(availableFluid));
        }
    }

    @Inject(method = "fillItem", remap = false, cancellable = true, at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lcom/simibubi/create/content/contraptions/fluids/actors/GenericItemFilling;fillItem(Lnet/minecraft/world/level/Level;ILnet/minecraft/world/item/ItemStack;Lnet/minecraftforge/fluids/FluidStack;)Lnet/minecraft/world/item/ItemStack;", remap = false))
    private static void fillSandwich(Level level, int requiredAmount, ItemStack stack, FluidStack availableFluid, CallbackInfoReturnable<ItemStack> cir) {
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
        for (SandwichSpoutingRecipe recipe : level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.SANDWICH_SPOUTING)) {
            if (recipe.matches(fluid)) {
                return recipe;
            }
        }
        return null;
    }
}
