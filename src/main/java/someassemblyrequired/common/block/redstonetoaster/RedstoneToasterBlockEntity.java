package someassemblyrequired.common.block.redstonetoaster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import someassemblyrequired.common.init.ModBlockEntityTypes;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModRecipeTypes;
import someassemblyrequired.common.init.ModTags;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class RedstoneToasterBlockEntity extends BlockEntity {

    public static final BlockEntityTicker<RedstoneToasterBlockEntity> TICKER = (level, pos, state, blockEntity) -> blockEntity.tick();

    private static final int TOASTING_TIME = 240;
    private static final int SMOKE_PARTICLES_TIME = 80;

    private final RedstoneToasterItemHandler inventory;
    private final LazyOptional<RedstoneToasterItemHandler> itemHandler;

    private final boolean isSticky;

    private int toastingProgress;
    private int smokeParticlesProgress;
    @Nullable
    private UUID playerToasting;

    private RedstoneToasterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, boolean isSticky) {
        super(type, pos, state);
        inventory = new RedstoneToasterItemHandler(this);
        itemHandler = LazyOptional.of(() -> inventory);
        this.isSticky = isSticky;
    }

    public static RedstoneToasterBlockEntity create(BlockPos pos, BlockState state, boolean isSticky) {
        BlockEntityType<?> type = isSticky ? ModBlockEntityTypes.STICKY_REDSTONE_TOASTER.get() : ModBlockEntityTypes.REDSTONE_TOASTER.get();
        return new RedstoneToasterBlockEntity(type, pos, state, isSticky);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
        }
        return super.getCapability(capability, side);
    }

    public int getAmountOfItems() {
        int result = 0;
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                result++;
            }
        }
        return result;
    }

    private boolean hasMetalInside() {
        for (ItemStack stack : inventory) {
            if (stack.is(ModTags.TOASTER_METALS)) {
                return true;
            }
        }
        return false;
    }

    public void dropItems() {
        if (getLevel() == null) {
            return;
        }
        BlockPos pos = getBlockPos();
        for (ItemStack stack : inventory) {
            ItemEntity item = new ItemEntity(getLevel(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
            getLevel().addFreshEntity(item);
        }
        inventory.clear();
    }

    public void explode() {
        if (getLevel() == null || getLevel().isClientSide()) {
            return;
        }
        BlockPos pos = getBlockPos();
        Player player = playerToasting == null ? null : getLevel().getPlayerByUUID(playerToasting);
        for (ItemStack stack : inventory) {
            ItemEntity entity = new ItemEntity(getLevel(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
            entity.setDefaultPickUpDelay();
            entity.setDeltaMovement(entity.getDeltaMovement().scale(2.5));
            getLevel().addFreshEntity(entity);
        }
        inventory.clear();
        getLevel().removeBlock(pos, true);
        getLevel().explode(player, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2.2F, true, Explosion.BlockInteraction.DESTROY);
    }

    public ItemStack removeItem() {
        int slot = !inventory.getStackInSlot(0).isEmpty() ? 0 : 1;
        return inventory.extractItem(slot, 1, false);
    }

    public boolean addItem(ItemStack stack) {
        int slot = 1;
        if (inventory.getStackInSlot(0).isEmpty()) {
            if (!inventory.getStackInSlot(1).isEmpty()) {
                return false;
            }
            slot = 0;
        }

        if (stack.getCount() != inventory.insertItem(slot, stack.copy(), false).getCount()) {
            stack.shrink(1);
            return true;
        }
        return false;
    }

    public ItemStack getItem(int slot) {
        return inventory.getStackInSlot(slot);
    }

    public static boolean hasToastingResult(Level level, ItemStack ingredient) {
        return !getToastingResult(level, ingredient).isEmpty();
    }

    private static ItemStack getToastingResult(Level level, ItemStack ingredient) {
        if (ingredient.isEmpty() || level == null) {
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
            if (ingredient.getItem().isEdible() && ingredient.getItem() != ModItems.CHARRED_FOOD.get()) {
                return new ItemStack(ModItems.CHARRED_FOOD.get());
            }
            return ItemStack.EMPTY;
        }
        return recipe.assemble(ingredientWrapper);
    }

    private void toastItems() {
        if (getLevel() == null) {
            return;
        }

        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            ItemStack result = getToastingResult(getLevel(), inventory.getStackInSlot(slot));
            if (!result.isEmpty()) {
                inventory.setStackInSlot(slot, result);
            }
        }
    }

    public boolean startToasting(Player entity) {
        playerToasting = entity.getUUID();
        return startToasting();
    }

    public boolean startToasting() {
        if (level == null || isToasting()) {
            return false;
        }

        BlockPos pos = getBlockPos();
        level.playSound(null, pos, SoundEvents.WOODEN_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.5F, 0.8F);

        if (hasMetalInside()) {
            explode();
            return true;
        }

        if (level != null && level.getBlockState(getBlockPos()).getValue(BlockStateProperties.WATERLOGGED)) {
            explode();
        }

        if (level.getBlockState(pos).hasProperty(RedstoneToasterBlock.TOASTING)) {
            level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(RedstoneToasterBlock.TOASTING, true));
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

        BlockPos pos = getBlockPos();
        if (level.getBlockState(pos).hasProperty(RedstoneToasterBlock.TOASTING)) {
            level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(RedstoneToasterBlock.TOASTING, false));
        }

        if (!isSticky) {
            level.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.25F + 0.8F);

            for (ItemStack ingredient : inventory) {
                ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.75, pos.getZ() + 0.5, ingredient);
                item.setDefaultPickUpDelay();
                item.setDeltaMovement(item.getDeltaMovement().multiply(0.5, 2, 0.5));
                level.addFreshEntity(item);
            }
            inventory.clear();
        }

        level.playSound(null, pos, SoundEvents.NOTE_BLOCK_BELL, SoundSource.BLOCKS, 0.8F, 4);
    }

    public boolean isToasting() {
        return toastingProgress > 0;
    }

    public void onContentsChanged() {
        if (getLevel() instanceof ServerLevel level) {
            level.updateNeighbourForOutputSignal(getBlockPos(), getLevel().getBlockState(getBlockPos()).getBlock());
            level.getChunkSource().blockChanged(getBlockPos());
            setChanged();
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void load(CompoundTag compoundNBT) {
        super.load(compoundNBT);
        inventory.deserializeNBT(compoundNBT.getCompound("Ingredients"));
        toastingProgress = compoundNBT.getInt("ToastingProgress");
        smokeParticlesProgress = compoundNBT.getInt("SmokeParticlesProgress");
        if (compoundNBT.hasUUID("PlayerToasting")) {
            playerToasting = compoundNBT.getUUID("PlayerToasting");
        }
    }

    @Override
    public void saveAdditional(CompoundTag compoundNBT) {
        compoundNBT.put("Ingredients", inventory.serializeNBT());
        compoundNBT.putInt("ToastingProgress", toastingProgress);
        compoundNBT.putInt("SmokeParticlesProgress", smokeParticlesProgress);
        if (playerToasting != null) {
            compoundNBT.putUUID("PlayerToasting", playerToasting);
        }
        super.saveAdditional(compoundNBT);
    }

    public void tick() {
        if (level == null) {
            return;
        }

        BlockPos pos = getBlockPos();
        if (isToasting()) {
            if (--toastingProgress <= 0) {
                stopToasting();
            } else {
                if (toastingProgress % 4 == 0) {
                    level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.05F, toastingProgress % 8 == 0 ? 2 : 1.9F);
                }
            }
        }

        if (toastingProgress == TOASTING_TIME / 3) {
            smokeParticlesProgress = TOASTING_TIME / 3 + SMOKE_PARTICLES_TIME;
        }

        if (smokeParticlesProgress > 0) {
            if (smokeParticlesProgress-- % 3 == 0) {
                level.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 0, 0.03, 0);
            }
        }
    }
}
