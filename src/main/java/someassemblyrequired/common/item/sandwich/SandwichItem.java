package someassemblyrequired.common.item.sandwich;

import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.block.SandwichAssemblyTableBlock;
import someassemblyrequired.common.ingredient.Ingredients;
import someassemblyrequired.common.init.ModAdvancementTriggers;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.integration.farmersdelight.FarmersDelightCompat;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class SandwichItem extends BlockItem {

    public SandwichItem(Block block, Properties builder) {
        super(block, builder);
    }

    public static ItemStack of(ItemLike item) {
        return of(new ItemStack(item));
    }

    public static ItemStack of(ItemStack item) {
        return SandwichBuilder.builder().add(item).build();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        SandwichItemHandler.get(stack).ifPresent(handler -> {
            int size = handler.size();
            int ingredientsToShow = Math.min(size, 8);
            if (size == ingredientsToShow + 1) {
                ingredientsToShow++;
            }

            for (int i = 0; i < ingredientsToShow; i++) {
                tooltip.add(Ingredients.getFullName(handler.getStackInSlot(size - i - 1)).plainCopy().withStyle(ChatFormatting.GRAY));
            }

            if (size > ingredientsToShow) {
                tooltip.add(
                        new TranslatableComponent("tooltip.%s.truncate_info".formatted(SomeAssemblyRequired.MODID), size - ingredientsToShow)
                                .withStyle(ChatFormatting.GRAY)
                                .withStyle(ChatFormatting.ITALIC)
                );
            }
        });
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (allowdedIn(group)) {
            if (ModCompat.isFarmersDelightLoaded()) {
                FarmersDelightCompat.addSandwichSubtypes(items);
            }
        }
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        boolean isPlacingOnTable = context.getLevel().getBlockState(context.getClickedPos().below()).getBlock() instanceof SandwichAssemblyTableBlock;
        if (context.getPlayer() != null && (context.getPlayer().isShiftKeyDown() || isPlacingOnTable)) {
            return super.placeBlock(context, state);
        }
        return false;
    }

    public InteractionResult place(UseOnContext useOnContext, BlockPos pos, ItemStack sandwich) {
        BlockPlaceContext placeContext = BlockPlaceContext.at(new BlockPlaceContext(useOnContext), pos, Direction.UP);
        if (!placeContext.canPlace()) {
            return InteractionResult.FAIL;
        }
        placeContext = this.updatePlacementContext(placeContext);
        if (placeContext == null) {
            return InteractionResult.FAIL;
        }
        BlockState blockstate = this.getPlacementState(placeContext);
        if (blockstate == null) {
            return InteractionResult.FAIL;
        } else if (!placeBlock(placeContext, blockstate)) {
            return InteractionResult.FAIL;
        }
        Level level = placeContext.getLevel();
        Player player = placeContext.getPlayer();
        BlockState placedState = level.getBlockState(pos);
        if (placedState.is(blockstate.getBlock())) {
            updateCustomBlockEntityTag(pos, level, player, sandwich, placedState);
            placedState.getBlock().setPlacedBy(level, pos, placedState, player, sandwich);
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, sandwich);
            }
        }

        level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
        SoundType soundType = placedState.getSoundType(level, pos, player);
        if (player != null) {
            level.playSound(player, pos, getPlaceSound(placedState, level, pos, player), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
        }
        if (player == null || !player.getAbilities().instabuild) {
            sandwich.shrink(1);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if (entity instanceof Player player) {
            triggerAdvancements(stack, player);
        }

        SandwichItemHandler.get(stack).ifPresent(sandwich -> {
            for (ItemStack item : sandwich.items) {
                FoodProperties food = Ingredients.getFood(item);
                if (food != null) {
                    if (entity instanceof Player player) {
                        player.getFoodData().eat(food.getNutrition(), food.getSaturationModifier());
                    }
                    for (Pair<MobEffectInstance, Float> effect : food.getEffects()) {
                        if (entity.getRandom().nextFloat() < effect.getSecond()) {
                            entity.addEffect(effect.getFirst());
                        }
                    }
                }
                Ingredients.onFoodEaten(item, entity);
            }
        });

        return super.finishUsingItem(stack, world, entity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (this.isEdible()) {
            ItemStack stack = player.getItemInHand(hand);
            // noinspection ConstantConditions
            if (SandwichItemHandler.get(stack).map(SandwichItemHandler::canAlwaysEat).orElse(false) || player.canEat(getFoodProperties().canAlwaysEat())) {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(stack);
            } else {
                return InteractionResultHolder.fail(stack);
            }
        } else {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
    }

    private void triggerAdvancements(ItemStack stack, Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        SandwichItemHandler.get(stack).ifPresent(sandwich -> {
            if (sandwich.isDoubleDeckerSandwich()) {
                ModAdvancementTriggers.CONSUME_DOUBLE_DECKER_SANDWICH.trigger(serverPlayer, stack);
            }
            for (ItemStack ingredient : sandwich) {
                if (ingredient.is(Items.POTION) && PotionUtils.getPotion(ingredient) != Potions.WATER) {
                    ModAdvancementTriggers.CONSUME_POTION_SANDWICH.trigger(serverPlayer, stack);
                }
            }
        });
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            return super.getName(stack);
        }
        return SandwichNameHelper.getSandwichDisplayName(stack);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {

            private final BlockEntityWithoutLevelRenderer renderer = new SandwichItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag compoundNBT) {
        return new ICapabilityProvider() {

            private final LazyOptional<SandwichItemHandler> handler = LazyOptional.of(this::createHandler);

            private SandwichItemHandler createHandler() {
                SandwichItemHandler handler = new ItemHandler(stack);
                handler.deserializeNBT(stack.getOrCreateTagElement("BlockEntityTag").getList("Sandwich", Tag.TAG_COMPOUND));
                return handler;
            }

            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
                if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                    return handler.cast();
                }
                return LazyOptional.empty();
            }
        };
    }

    private static class ItemHandler extends SandwichItemHandler {

        private final ItemStack sandwich;

        private ItemHandler(ItemStack sandwich) {
            this.sandwich = sandwich;
        }

        @Override
        protected void onContentsChanged() {
            sandwich.getOrCreateTagElement("BlockEntityTag").put("Sandwich", serializeNBT());
        }
    }
}
