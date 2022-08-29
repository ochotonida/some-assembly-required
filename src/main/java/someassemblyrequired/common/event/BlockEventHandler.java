package someassemblyrequired.common.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import someassemblyrequired.common.block.sandwich.SandwichBlockEntity;
import someassemblyrequired.common.ingredient.Ingredients;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;
import someassemblyrequired.common.item.sandwich.SandwichItem;
import someassemblyrequired.common.util.Util;

public class BlockEventHandler { // TODO do more testing on this

    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(BlockEventHandler::onRightClickBlock);
    }

    private static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCanceled() || event.getUseBlock() != Event.Result.DEFAULT || event.getUseItem() != Event.Result.DEFAULT) {
            return;
        }

        BlockPos pos = event.getHitVec().getBlockPos();
        Level level = event.getLevel();
        BlockState state = level.getBlockState(pos);
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();

        if (player.isShiftKeyDown()) {
            return;
        }

        if (state.is(ModTags.SANDWICHING_STATIONS)) {
            InteractionResult result = (level.getBlockEntity(pos.above()) instanceof SandwichBlockEntity blockEntity)
                    ? blockEntity.interact(player, hand)
                    : tryPlaceSandwich(event);

            if (result != InteractionResult.PASS) {
                event.setCanceled(true);
                event.setCancellationResult(result);
            }
        }
    }

    private static InteractionResult tryPlaceSandwich(PlayerInteractEvent.RightClickBlock event) {
        ItemStack heldItem = event.getItemStack().copy();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        BlockHitResult hitResult = event.getHitVec();
        BlockPos pos = hitResult.getBlockPos();
        heldItem.setCount(1);

        if (!Ingredients.canAddToSandwich(heldItem)) {
            return InteractionResult.PASS;
        } else if (!heldItem.is(ModItems.SANDWICH.get()) && !heldItem.is(ModTags.SANDWICH_BREAD)) {
            player.displayClientMessage(Util.translate("message.bottom_bread"), true);
            return InteractionResult.SUCCESS;
        }

        ItemStack sandwich = SandwichItem.of(heldItem);
        UseOnContext useOnContext = new UseOnContext(player, hand, hitResult);
        InteractionResult placeResult = ModItems.SANDWICH.get().place(useOnContext, pos.above(), sandwich);

        if (sandwich.isEmpty()) {
            player.getItemInHand(hand).shrink(1);
        }

        return placeResult;
    }
}
