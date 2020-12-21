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
import someasseblyrequired.common.init.SpreadTypes;
import someasseblyrequired.common.init.Tags;
import someasseblyrequired.common.item.spreadtype.SpreadType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SandwichItem extends BlockItem {

    public SandwichItem(Block block, Properties builder) {
        super(block, builder);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT compoundNBT) {
        return new ICapabilityProvider() {

            private final LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

            private IItemHandler createHandler() {
                ItemStackHandler handler = new ItemStackHandler() {

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
                        stack.getOrCreateChildTag("BlockEntityTag").put("Ingredients", serializeNBT());
                    }
                };
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

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(EmptyHandler.INSTANCE);
        List<ItemStack> sandwich = new ArrayList<>();
        for (int slot = 0; slot < handler.getSlots() && !handler.getStackInSlot(slot).isEmpty(); slot++) {
            sandwich.add(handler.getStackInSlot(slot));
        }

        if (sandwich.size() >= 3) {
            ItemStack ingredient = sandwich.get(1);
            if (sandwich.size() % 2 != 0
                    && sandwich.stream().allMatch(sandwichComponent -> sandwich.indexOf(sandwichComponent) % 2 == 0 ? Tags.BREADS.contains(sandwichComponent.getItem()) : ingredient.equals(sandwichComponent, false))
                    && (ingredient.getItem() == Items.TOASTED_BREAD_SLICE || !Tags.BREADS.contains(ingredient.getItem()))) {
                String quantifier;
                switch (sandwich.size() / 2) {
                    case 1:
                        quantifier = "single";
                        break;
                    case 2:
                        quantifier = "double";
                        break;
                    case 3:
                        quantifier = "triple";
                        break;
                    case 4:
                        quantifier = "quadruple";
                        break;
                    case 5:
                        quantifier = "quintuple";
                        break;
                    case 6:
                        quantifier = "sextuple";
                        break;
                    default:
                        quantifier = "septuple";
                }
                ITextComponent displayName;
                if (ingredient.getItem() == Items.TOASTED_BREAD_SLICE) {
                    displayName = new TranslationTextComponent("item.someassemblyrequired.sandwich.toast");
                } else {
                    SpreadType spreadType = SpreadTypes.findSpreadType(ingredient.getItem());
                    if (spreadType != null) {
                        displayName = spreadType.getDisplayName(ingredient);
                    } else {
                        displayName = ingredient.getDisplayName();
                    }
                }
                return new TranslationTextComponent("item.someassemblyrequired." + quantifier + "_sandwich", displayName);
            }
        }

        int breadAmount = 0;
        for (ItemStack ingredient : sandwich) {
            if (Tags.BREADS.contains(ingredient.getItem())) {
                breadAmount++;
            }
        }

        if (breadAmount == sandwich.size()) {
            return new TranslationTextComponent("item.someassemblyrequired.snadwich");
        }

        if (breadAmount == 3) {
            return new TranslationTextComponent("item.someassemblyrequired.double_decker_sandwich");
        }

        return super.getDisplayName(stack);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(EmptyHandler.INSTANCE);
        for (int slot = 0; slot < handler.getSlots() && !handler.getStackInSlot(slot).isEmpty(); slot++) {
            if (!Tags.BREADS.contains(handler.getStackInSlot(slot).getItem())) {
                return super.getTranslationKey(stack);
            }
        }
        return "item.someassemblyrequired.snadwich";
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            for (int slot = 0; slot < handler.getSlots() && !handler.getStackInSlot(slot).isEmpty(); slot++) {
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
}
