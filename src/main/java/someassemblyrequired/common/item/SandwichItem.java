package someassemblyrequired.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import someassemblyrequired.common.init.ModAdvancementTriggers;
import someassemblyrequired.common.init.ModRecipeTypes;
import someassemblyrequired.common.recipe.SpreadType;
import someassemblyrequired.common.util.SandwichBuilder;
import someassemblyrequired.common.util.SandwichIngredientHelper;
import someassemblyrequired.common.util.SandwichNameHelper;

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

            for (int slot = size - 1; slot >= 0; slot--) {
                // only show 8 ingredients (or 9, if there are exactly 9 ingredients)
                if (slot < size - 8 && size >= 10) {
                    tooltip.add(new TranslationTextComponent("tooltip.someassemblyrequired.truncate_info", size - 8).mergeStyle(TextFormatting.GRAY).mergeStyle(TextFormatting.ITALIC));
                    return;
                }
                tooltip.add(handler.getStackInSlot(slot).getDisplayName().copyRaw().mergeStyle(TextFormatting.GRAY));
            }
        });
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group)) {
            items.add(SandwichBuilder.blt());
        }
    }

    @Override
    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        if (context.getPlayer() != null && context.getPlayer().isSneaking()) {
            return super.placeBlock(context, state);
        }
        return false;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack sandwich, World world, LivingEntity entity) {
        List<ItemStack> ingredients = SandwichIngredientHelper.getIngredients(sandwich);

        if (entity instanceof ServerPlayerEntity) {
            if (SandwichIngredientHelper.isDoubleDeckerSandwich(ingredients)) {
                ModAdvancementTriggers.CONSUME_DOUBLE_DECKER_SANDWICH.trigger((ServerPlayerEntity) entity, sandwich);
            } else if (SandwichIngredientHelper.isBLT(SandwichIngredientHelper.getUniqueIngredientsExcludingBread(ingredients))) {
                ModAdvancementTriggers.CONSUME_BLT_SANDWICH.trigger((ServerPlayerEntity) entity, sandwich);
            }
        }

        List<ItemStack> results = new ArrayList<>();
        for (ItemStack ingredient : ingredients) {
            ItemStack result = consumeStack(ingredient, world, entity);
            if (!result.isEmpty()) {
                results.add(result);
            }
        }
        ItemStack result = super.onItemUseFinish(sandwich, world, entity);

        if (!(entity instanceof PlayerEntity)) {
            return result;
        }

        if (result.isEmpty() && results.size() > 0) {
            result = results.remove(0);
        }

        for (ItemStack stack : results) {
            ((PlayerEntity) entity).addItemStackToInventory(stack);
        }

        return result;
    }

    private ItemStack consumeStack(ItemStack ingredient, World world, LivingEntity entity) {
        if (ingredient.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack ingredientCopy = ingredient.copy();
        ItemStack result = ForgeEventFactory.onItemUseFinish(entity, ingredient.copy(), ingredient.getUseDuration(), ingredient.onItemUseFinish(world, entity));

        if (!result.isEmpty()) {
            SpreadType spreadType = ModRecipeTypes.getSpreadType(ingredientCopy, world);
            if (spreadType == null || result.getItem() != spreadType.getContainer(ingredientCopy).getItem()) {
                if (!(entity instanceof PlayerEntity) || !((PlayerEntity) entity).isCreative()) {
                    return result;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (stack.hasDisplayName()) {
            return super.getDisplayName(stack);
        }
        return SandwichNameHelper.getSandwichDisplayName(stack);
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
