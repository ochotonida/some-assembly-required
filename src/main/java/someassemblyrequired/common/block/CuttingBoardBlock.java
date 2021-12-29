package someassemblyrequired.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import someassemblyrequired.common.block.tileentity.CuttingBoardTileEntity;
import someassemblyrequired.common.init.ModTags;
import someassemblyrequired.common.init.ModTileEntityTypes;

import java.util.List;

public class CuttingBoardBlock extends WaterLoggableHorizontalBlock {

    private static final VoxelShape SHAPE_NORTH_SOUTH = Block.box(1, 0, 2, 15, 1, 14);
    private static final VoxelShape SHAPE_EAST_WEST = Block.box(2, 0, 1, 14, 1, 15);

    public CuttingBoardBlock(Properties properties) {
        super(properties);
    }

    public static boolean isKnife(ItemStack stack) {
        return ModTags.TOOLS_KNIVES.contains(stack.getItem()) || stack.getItem() instanceof SwordItem;
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (!(tileEntity instanceof CuttingBoardTileEntity)) {
            return InteractionResult.PASS;
        }

        CuttingBoardTileEntity cuttingBoard = (CuttingBoardTileEntity) tileEntity;
        ItemStack heldItem = player.getItemInHand(hand);

        if (!cuttingBoard.hasIngredient() && !heldItem.isEmpty()) {
            return addIngredient(heldItem, cuttingBoard, world, pos, player, hand) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        } else if (heldItem.isEmpty() && hand == InteractionHand.MAIN_HAND) {
            return removeIngredient(cuttingBoard, world, pos, player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        } else {
            return cutIngredient(heldItem, cuttingBoard, world, pos, player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
    }

    private boolean addIngredient(ItemStack ingredient, CuttingBoardTileEntity cuttingBoard, Level world, BlockPos pos, Player player, InteractionHand hand) {
        if (!player.getItemInHand(InteractionHand.OFF_HAND).isEmpty() && hand.equals(InteractionHand.MAIN_HAND) && !(player.getItemInHand(hand).getItem() instanceof BlockItem)) {
            // prefer off-hand placement of items
            return false;
        }

        // play block place sound if the item is a block
        if (ingredient.getItem() instanceof BlockItem) {
            Block block = ((BlockItem) ingredient.getItem()).getBlock();
            SoundType soundType = block.getSoundType(block.defaultBlockState(), world, pos, player);
            world.playSound(player, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, 0.5F * (soundType.getVolume() + 1) / 2, soundType.getPitch() * 0.8F);
        } else if (isKnife(ingredient)) {
            world.playSound(player, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 0.5F, 0.8F);
        } else {
            world.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.2F, 0.8F);
        }

        boolean hasAddedIngredient = cuttingBoard.addIngredient(player.isCreative() ? ingredient.copy() : ingredient);
        assert hasAddedIngredient;

        return true;
    }

    private boolean cutIngredient(ItemStack tool, CuttingBoardTileEntity cuttingBoard, Level world, BlockPos pos, Player player) {
        List<ItemStack> results = cuttingBoard.processIngredient(tool);
        if (results.isEmpty()) {
            return false;
        }

        for (ItemStack result : results) {
            ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, result);
            item.setPickUpDelay(5);
            world.addFreshEntity(item);
        }

        world.playSound(player, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 0.7F, 0.8F);

        return true;
    }

    private boolean removeIngredient(CuttingBoardTileEntity cuttingBoard, Level world, BlockPos pos, Player player) {
        ItemStack ingredient = cuttingBoard.removeIngredient();
        if (!ingredient.isEmpty()) {
            if (!player.isCreative()) {
                ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, ingredient);
                item.setPickUpDelay(5);
                world.addFreshEntity(item);
            }
            world.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.2F, 1F);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return ModTileEntityTypes.CUTTING_BOARD.get().create();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (!(tileEntity instanceof CuttingBoardTileEntity)) {
            return 0;
        }

        return ((CuttingBoardTileEntity) tileEntity).hasIngredient() ? 15 : 0;

    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() == Direction.Axis.X ? SHAPE_EAST_WEST : SHAPE_NORTH_SOUTH;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return getShape(state, worldIn, pos, context);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!newState.is(state.getBlock())) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof CuttingBoardTileEntity) {
                // drop contained ingredient
                ItemStack ingredient = ((CuttingBoardTileEntity) tileEntity).removeIngredient();
                if (!ingredient.isEmpty()) {
                    ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, ingredient);
                    world.addFreshEntity(item);
                }

                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }
}
