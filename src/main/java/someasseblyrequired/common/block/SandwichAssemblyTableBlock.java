package someasseblyrequired.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import someasseblyrequired.common.block.tileentity.SandwichAssemblyTableTileEntity;
import someasseblyrequired.common.init.Items;
import someasseblyrequired.common.init.Tags;
import someasseblyrequired.common.init.TileEntityTypes;
import someasseblyrequired.common.item.spreadtype.SpreadType;
import someasseblyrequired.common.item.spreadtype.SpreadTypeManager;

public class SandwichAssemblyTableBlock extends HorizontalBlock {

    public SandwichAssemblyTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityTypes.SANDWICH_ASSEMBLY_TABLE.get().create();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing());
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof SandwichAssemblyTableTileEntity)) {
            return ActionResultType.PASS;
        }
        SandwichAssemblyTableTileEntity sandwichTable = (SandwichAssemblyTableTileEntity) tileEntity;

        if (player.isSneaking()) {
            // a sandwich must contain at least one ingredient
            if (sandwichTable.getAmountOfItems() == 0) {
                player.sendStatusMessage(new TranslationTextComponent("message.someassemblyrequired.bottom_bread"), true);
                // a sandwich must have bread as the top and bottom ingredient
            } else if (!sandwichTable.hasBreadAsTopIngredient()) {
                player.sendStatusMessage(new TranslationTextComponent("message.someassemblyrequired.top_bread"), true);
            } else {
                world.playSound(player, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 0.5F, 1.4F);
                ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, SandwichBlock.createSandwich(tileEntity));
                item.setPickupDelay(5);
                world.addEntity(item);
                sandwichTable.removeItems();
            }
            // remove the top ingredient if the player is not holding anything
        } else if (player.getHeldItem(Hand.OFF_HAND).isEmpty() && player.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
            if (sandwichTable.getAmountOfItems() > 0) {
                ItemStack ingredient = sandwichTable.removeTopIngredient();
                if (!ingredient.isEmpty()) {
                    if (!player.isCreative()) {
                        ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, ingredient);
                        item.setPickupDelay(5);
                        world.addEntity(item);
                    }
                    world.playSound(player, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 0.3F, 1.6F);
                } else {
                    world.playSound(player, pos, SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundCategory.BLOCKS, 0.3F, 1.6F);
                }
            }
        } else if (!addIngredient(sandwichTable, world, pos, player, hand)) {
            return ActionResultType.FAIL;
        }
        return ActionResultType.SUCCESS;
    }

    private boolean addIngredient(SandwichAssemblyTableTileEntity sandwichTable, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getHeldItem(hand);
        // try to add the ingredient
        if (sandwichTable.addIngredient(heldItem)) {
            SpreadType spreadType = SpreadTypeManager.INSTANCE.getSpreadType(heldItem.getItem());
            if (spreadType == null) {
                world.playSound(player, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 0.3F, 1.3F);
            } else {
                world.playSound(player, pos, SoundEvents.BLOCK_HONEY_BLOCK_PLACE, SoundCategory.BLOCKS, 0.3F, 1.3F);
            }
            if (!player.isCreative()) {
                // decrease the player's held item by one if the ingredient successfully got added
                player.getHeldItem(hand).shrink(1);

                // add the container item of the spreadtype to the player's inventory if applicable
                if (spreadType != null && spreadType.hasContainer(heldItem)) {
                    ItemStack container = new ItemStack(spreadType.getContainer(heldItem), 1);
                    if (player.getHeldItem(hand).isEmpty()) {
                        player.setHeldItem(hand, container);
                    } else {
                        player.addItemStackToInventory(container);
                    }
                }
            }
        } else if (heldItem.isFood() || heldItem.getItem() == Items.SPREAD.get() || SpreadTypeManager.INSTANCE.hasSpreadType(heldItem.getItem())) {
            if (sandwichTable.getAmountOfItems() == 0 && !Tags.BREAD.contains(heldItem.getItem())) {
                player.sendStatusMessage(new TranslationTextComponent("message.someassemblyrequired.bottom_bread"), true);
            } else if (sandwichTable.getAmountOfItems() == sandwichTable.getInventorySize()) {
                player.sendStatusMessage(new TranslationTextComponent("message.someassemblyrequired.full_sandwich"), true);
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        // drop all contained items and update the comparator output if the block is removed
        if (!newState.isIn(state.getBlock())) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof SandwichAssemblyTableTileEntity) {
                for (ItemStack ingredient : ((SandwichAssemblyTableTileEntity) tileEntity).removeItems()) {
                    ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, ingredient);
                    world.addEntity(item);
                }

                world.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(BlockState blockState, World world, BlockPos pos) {
        // comparator input = max(15, <items on assembly table>)
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            IItemHandler handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(EmptyHandler.INSTANCE);
            for (int slot = 0; slot < handler.getSlots() && slot <= 15; slot++) {
                if (handler.getStackInSlot(slot).isEmpty()) {
                    return slot;
                }
            }
            return 15;
        }
        return 0;
    }
}
