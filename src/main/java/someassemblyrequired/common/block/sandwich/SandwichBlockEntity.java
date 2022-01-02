package someassemblyrequired.common.block.sandwich;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import someassemblyrequired.common.init.ModBlockEntityTypes;
import someassemblyrequired.common.item.sandwich.SandwichItemHandler;

import javax.annotation.Nullable;

public class SandwichBlockEntity extends BlockEntity {

    private final SandwichItemHandler sandwich = new SandwichItemHandler();
    private final LazyOptional<SandwichItemHandler> itemHandler = LazyOptional.of(() -> sandwich);

    public SandwichBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.SANDWICH.get(), pos, state);
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
    public void load(CompoundTag tag) {
        super.load(tag);
        sandwich.deserializeNBT(tag.getList("Sandwich", Tag.TAG_COMPOUND));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.put("Sandwich", sandwich.serializeNBT());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
        }
        return super.getCapability(capability, side);
    }
}
