package someassemblyrequired.common.block.redstonetoaster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import someassemblyrequired.common.block.ItemHandlerBlockEntity;
import someassemblyrequired.common.init.ModBlockEntityTypes;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModRecipeTypes;
import someassemblyrequired.common.init.ModTags;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class RedstoneToasterBlockEntity extends ItemHandlerBlockEntity {

    public static final BlockEntityTicker<RedstoneToasterBlockEntity> TICKER = (level, pos, state, blockEntity) -> blockEntity.tick();

    private static final int TOASTING_TIME = 240;
    private static final int SMOKE_PARTICLES_TIME = 80;

    private int toastingProgress;
    private int smokeParticlesProgress;
    private boolean isEjectionToaster;
    @Nullable
    private UUID playerToasting;

    public RedstoneToasterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.REDSTONE_TOASTER.get(), pos, state, 2);
    }

    public void setAutoEject(boolean isEjectionToaster) {
        this.isEjectionToaster = isEjectionToaster;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        // for items, only allow inserting/extracting from the bottom
        // return super.getCapability for any other capability
        if (side == null || side == Direction.DOWN || capability != CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return super.getCapability(capability, side);
        }
        return LazyOptional.empty();
    }

    private boolean hasMetalInside() {
        return getItems().stream().anyMatch(stack -> ModTags.TOASTER_METALS.contains(stack.getItem()));
    }

    public void explode() {
        if (level != null && !level.isClientSide()) {
            Player player = playerToasting == null ? null : level.getPlayerByUUID(playerToasting);
            NonNullList<ItemStack> items = removeItems();
            level.removeBlock(worldPosition, true);
            level.explode(player, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, 2.2F, true, Explosion.BlockInteraction.DESTROY);
            for (ItemStack stack : items) {
                ItemEntity entity = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, stack);
                entity.setDefaultPickUpDelay();
                entity.setDeltaMovement(entity.getDeltaMovement().scale(2.5));
                level.addFreshEntity(entity);
            }
        }
    }

    public ItemStack removeItem() {
        int slot = !getInventory().getStackInSlot(0).isEmpty() ? 0 : 1;
        return getInventory().extractItem(slot, 1, false);
    }

    /**
     * Insert 1 unit of the specified stack in the toaster
     *
     * @return whether the ingredient successfully got added
     */
    public boolean addItem(ItemStack stack) {
        int slot;
        if (getInventory().getStackInSlot(0).isEmpty()) {
            if (!getInventory().getStackInSlot(1).isEmpty()) {
                // fully empty the toaster first
                return false;
            }
            slot = 0;
        } else {
            slot = 1;
        }

        if (stack.getCount() != getInventory().insertItem(slot, stack.copy(), false).getCount()) {
            stack.shrink(1);
            return true;
        }
        return false;
    }

    public ItemStack getItem(int slot) {
        return getInventory().getStackInSlot(slot);
    }

    private boolean hasToastingResult(ItemStack ingredient) {
        return !getToastingResult(ingredient).isEmpty();
    }

    private ItemStack getToastingResult(ItemStack ingredient) {
        if (ingredient.isEmpty() || ingredient.getItem() == ModItems.CHARRED_MORSEL.get() || level == null) {
            return ItemStack.EMPTY;
        }

        Container ingredientWrapper = new SimpleContainer(ingredient);

        Optional<? extends Recipe<Container>> toastingRecipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.TOASTING, ingredientWrapper, level);

        Recipe<Container> recipe = null;
        if (toastingRecipe.isPresent()) {
            recipe = toastingRecipe.get();
        } else {
            Optional<? extends Recipe<Container>> smokingRecipe = level.getRecipeManager().getRecipeFor(RecipeType.SMOKING, ingredientWrapper, level);
            if (smokingRecipe.isPresent()) {
                recipe = smokingRecipe.get();
            }
        }

        if (recipe == null) {
            if (ingredient.getItem().isEdible()) {
                if (ModTags.SMALL_FOODS.contains(ingredient.getItem())) {
                    return new ItemStack(ModItems.CHARRED_MORSEL.get());
                } else {
                    return new ItemStack(ModItems.CHARRED_FOOD.get());
                }
            } else {
                return ItemStack.EMPTY;
            }
        } else {
            return recipe.assemble(ingredientWrapper);
        }
    }

    private void toastItems() {
        if (level == null) {
            return;
        }

        for (int slot = 0; slot < getInventory().getSlots(); slot++) {
            ItemStack result = getToastingResult(getInventory().getStackInSlot(slot));
            if (!result.isEmpty()) {
                getInventory().setStackInSlot(slot, result);
            }
        }
    }

    public boolean startToasting(Player entity) {
        playerToasting = entity.getUUID();
        if (level != null && level.getBlockState(worldPosition).getValue(BlockStateProperties.WATERLOGGED)) {
            explode();
        }
        return startToasting();
    }

    public boolean startToasting() {
        if (level == null || isToasting()) {
            return false;
        }

        level.playSound(null, worldPosition, SoundEvents.WOODEN_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.5F, 0.8F);

        if (hasMetalInside()) {
            explode();
            return true;
        }

        if (level.getBlockState(worldPosition).hasProperty(RedstoneToasterBlock.TOASTING)) {
            level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(RedstoneToasterBlock.TOASTING, true));
        }

        toastingProgress = TOASTING_TIME;
        if (smokeParticlesProgress > 0) {
            smokeParticlesProgress = TOASTING_TIME + SMOKE_PARTICLES_TIME;
        }

        return true;
    }

    private void stopToasting() {
        toastItems();

        playerToasting = null;

        if (level == null) {
            return;
        }

        if (level.getBlockState(worldPosition).hasProperty(RedstoneToasterBlock.TOASTING)) {
            level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(RedstoneToasterBlock.TOASTING, false));
        }

        if (isEjectionToaster) {
            level.playSound(null, worldPosition, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.25F + 0.8F);

            for (ItemStack ingredient : removeItems()) {
                ItemEntity item = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.75, worldPosition.getZ() + 0.5, ingredient);
                item.setDefaultPickUpDelay();
                item.setDeltaMovement(item.getDeltaMovement().multiply(0.5, 2, 0.5));
                level.addFreshEntity(item);
            }
        }

        level.playSound(null, worldPosition, SoundEvents.NOTE_BLOCK_BELL, SoundSource.BLOCKS, 0.8F, 4);
    }

    public boolean isToasting() {
        return toastingProgress > 0;
    }

    @Override
    protected boolean canModifyItems() {
        return !isToasting();
    }

    @Override
    public void load(CompoundTag compoundNBT) {
        super.load(compoundNBT);
        toastingProgress = compoundNBT.getInt("ToastingProgress");
        smokeParticlesProgress = compoundNBT.getInt("SmokeParticlesProgress");
        isEjectionToaster = compoundNBT.getBoolean("IsEjectionToaster");
        if (compoundNBT.hasUUID("PlayerToasting")) {
            playerToasting = compoundNBT.getUUID("PlayerToasting");
        }
    }

    @Override
    public void saveAdditional(CompoundTag compoundNBT) {
        compoundNBT.putInt("ToastingProgress", toastingProgress);
        compoundNBT.putInt("SmokeParticlesProgress", smokeParticlesProgress);
        compoundNBT.putBoolean("IsEjectionToaster", isEjectionToaster);
        if (playerToasting != null) {
            compoundNBT.putUUID("PlayerToasting", playerToasting);
        }
        super.saveAdditional(compoundNBT);
    }

    public void tick() {
        if (level == null) {
            return;
        }

        if (isToasting()) {
            if (--toastingProgress <= 0) {
                stopToasting();
            } else {
                if (toastingProgress % 4 == 0) {
                    level.playSound(null, worldPosition, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.05F, toastingProgress % 8 == 0 ? 2 : 1.9F);
                }
            }
        }

        if (toastingProgress == TOASTING_TIME / 3) {
            smokeParticlesProgress = TOASTING_TIME / 3 + SMOKE_PARTICLES_TIME;
        }

        if (smokeParticlesProgress > 0) {
            if (smokeParticlesProgress-- % 3 == 0) {
                level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + 0.5, worldPosition.getY() + 0.8, worldPosition.getZ() + 0.5, 0, 0.03, 0);
            }
        }
    }

    @Override
    protected BlockEntityItemHandler createItemHandler(int size) {
        return new BlockEntityItemHandler(size) {

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return super.isItemValid(slot, stack) && stack.getItem() != ModItems.SANDWICH.get() && (stack.getItem().isEdible() || !(stack.getItem() instanceof BlockItem) || hasToastingResult(stack));
            }
        };
    }
}
