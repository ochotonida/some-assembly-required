package someasseblyrequired.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import someasseblyrequired.common.init.Items;
import someasseblyrequired.common.init.Tags;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SandwichItem extends BlockItem {

    public SandwichItem(Block block, Properties builder) {
        super(block, builder);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            int size;
            for (size = 0; size < handler.getSlots() && !handler.getStackInSlot(size).isEmpty(); size++) ;

            for (int slot = 0; slot < size; slot++) {
                // only show 8 ingredients (or 9, if there are exactly 9 ingredients)
                if (slot >= 8 && size >= 10) {
                    tooltip.add(new TranslationTextComponent("item.someassemblyrequired.sandwich.tooltip.truncate_info", size - 8).mergeStyle(TextFormatting.GRAY).mergeStyle(TextFormatting.ITALIC));
                    return;
                }
                tooltip.add(handler.getStackInSlot(slot).getDisplayName().copyRaw().mergeStyle(TextFormatting.GRAY));
            }
        });
    }

    @Override
    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        if (context.getPlayer() != null && context.getPlayer().isSneaking()) {
            return super.placeBlock(context, state);
        }
        return false;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entity) {
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            for (int slot = 0; slot < handler.getSlots() && !handler.getStackInSlot(slot).isEmpty(); slot++) {
                ItemStack ingredient = handler.getStackInSlot(slot);
                ItemStack finishStack = ingredient.getItem().onItemUseFinish(ingredient, world, entity);
                if (entity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) entity;
                    if (player.getCooldownTracker().hasCooldown(ingredient.getItem())) {
                        player.getCooldownTracker().setCooldown(this, 20);
                    }
                    if (!player.isCreative() && !finishStack.isEmpty()) {
                        player.addItemStackToInventory(finishStack);
                    }
                }
            }
        });
        return super.onItemUseFinish(stack, world, entity);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(EmptyHandler.INSTANCE);
        List<ItemStack> ingredients = new ArrayList<>();
        for (int slot = 0; slot < handler.getSlots() && !handler.getStackInSlot(slot).isEmpty(); slot++) {
            ingredients.add(handler.getStackInSlot(slot));
        }

        if (ingredients.size() >= 3) {
            ItemStack ingredient = ingredients.get(1);
            if (ingredients.size() % 2 != 0
                    && ingredients.stream().allMatch(sandwichComponent -> ingredients.indexOf(sandwichComponent) % 2 == 0 ? Tags.BREADS.contains(sandwichComponent.getItem()) : ItemStack.areItemStacksEqual(ingredient, sandwichComponent))
                    && (ingredient.getItem() == Items.TOASTED_BREAD_SLICE || !Tags.BREADS.contains(ingredient.getItem()))) {

                if (ingredient.getTag() != null && ingredient.getTag().contains("Ingredient")) {
                    ItemStack spreadItem = ItemStack.read(ingredient.getOrCreateChildTag("Ingredient"));
                    if (spreadItem.getItem() == net.minecraft.item.Items.POTION) {
                        Potion potion = PotionUtils.getPotionFromItem(spreadItem);
                        if (potion == Potions.WATER && ingredients.size() == 3) {
                            return new TranslationTextComponent("item.someassemblyrequired.soggy_sandwich");
                        } else if (potion.getEffects().size() == 1) {
                            return new TranslationTextComponent("item.someassemblyrequired." + getQuantifier(ingredients.size() / 2) + "_potion_sandwich", potion.getEffects().get(0).getPotion().getDisplayName());
                        }
                    }
                }

                return new TranslationTextComponent("item.someassemblyrequired." + getQuantifier(ingredients.size() / 2) + "_sandwich", getIngredientDisplayName(ingredient));
            }
        }

        int breadAmount = 0;
        for (ItemStack ingredient : ingredients) {
            if (Tags.BREADS.contains(ingredient.getItem())) {
                breadAmount++;
            }
        }

        if (breadAmount == ingredients.size()) {
            return new TranslationTextComponent("item.someassemblyrequired.snadwich");
        }

        if (breadAmount == 3) {
            return new TranslationTextComponent("item.someassemblyrequired.double_decker_sandwich");
        }

        return super.getDisplayName(stack);
    }

    private String getQuantifier(int number) {
        switch (number) {
            case 1:
                return "single";
            case 2:
                return "double";
            case 3:
                return "triple";
            case 4:
                return "quadruple";
            case 5:
                return "quintuple";
            case 6:
                return "sextuple";
            default:
                return "very_large";
        }
    }

    private ITextComponent getIngredientDisplayName(ItemStack ingredient) {
        if (ingredient.getItem() == Items.TOASTED_BREAD_SLICE) {
            return new TranslationTextComponent("item.someassemblyrequired.sandwich.toast");
        } else {
            return ingredient.getDisplayName();
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT compoundNBT) {
        return new ICapabilityProvider() {

            private final LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

            private IItemHandler createHandler() {
                ItemStackHandler handler = new IngredientItemHandler(stack);
                handler.deserializeNBT(stack.getOrCreateChildTag("BlockEntityTag").getCompound("Ingredients"));
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

    private static class IngredientItemHandler extends ItemStackHandler {

        private final ItemStack sandwichItem;

        private IngredientItemHandler(ItemStack sandwichItem) {
            super(0);
            this.sandwichItem = sandwichItem;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        protected void onContentsChanged(int slot) {
            sandwichItem.getOrCreateChildTag("BlockEntityTag").put("Ingredients", serializeNBT());
        }
    }
}
