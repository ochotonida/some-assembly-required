package someassemblyrequired.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import someassemblyrequired.common.block.sandwich.SandwichBlockEntity;
import someassemblyrequired.common.ingredient.Ingredients;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;
import someassemblyrequired.common.item.sandwich.SandwichItem;
import someassemblyrequired.common.util.Util;

public class SandwichAssemblyTableBlock extends Block {

    public SandwichAssemblyTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos.above()) instanceof SandwichBlockEntity blockEntity) {
            return blockEntity.interact(player, hand);
        } else {
            return tryPlaceSandwich(player, hand, pos, hitResult);
        }
    }

    private static InteractionResult tryPlaceSandwich(Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitResult) {
        ItemStack heldItem = player.getItemInHand(hand).copy();
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
