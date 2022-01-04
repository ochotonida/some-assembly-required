package someassemblyrequired.data;

import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.block.redstonetoaster.RedstoneToasterBlock;
import someassemblyrequired.common.block.sandwich.SandwichBlock;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.util.Util;

import java.util.function.Consumer;
import java.util.function.Function;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SomeAssemblyRequired.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // sandwich
        ModelFile sandwichModel = models()
                .getBuilder(prefixBlock("sandwich").toString())
                .texture("particle", prefixItem("bread_slice"));
        horizontalBlock(ModBlocks.SANDWICH.get(), $ -> sandwichModel, BlockStateProperties.WATERLOGGED, SandwichBlock.SIZE);

        // lettuce
        getVariantBuilder(ModBlocks.LETTUCE.get())
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(createLettuceModel(state.getValue(((CropBlock) ModBlocks.LETTUCE.get()).getAgeProperty())))
                        .build()
                );

        // sandwich assembly table base model
        models().withExistingParent("block/sandwich_assembly_table", "cube")
                .texture("down", "#bottom")
                .texture("up", prefixBlock("sandwich_assembly_table_top"))
                .texture("north", "#side")
                .texture("south", "#front")
                .texture("east", "#side")
                .texture("west", "#side")
                .texture("particle", "#side")
                .transforms()
                .transform(ModelBuilder.Perspective.GUI)
                .rotation(30, 45, 0)
                .scale(0.625F)
                .end()
                .transform(ModelBuilder.Perspective.FIXED)
                .rotation(0, 180, 0)
                .scale(0.5F)
                .end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT)
                .rotation(0, 315, 0)
                .scale(0.4F)
                .end()
                .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT)
                .rotation(75, 315, 0)
                .translation(0, 2.5F, 0)
                .scale(0.375F)
                .end()
                .end();

        sandwichAssemblyTable(ModBlocks.OAK_SANDWICH_ASSEMBLY_TABLE.get(), "oak");
        sandwichAssemblyTable(ModBlocks.SPRUCE_SANDWICH_ASSEMBLY_TABLE.get(), "spruce");
        sandwichAssemblyTable(ModBlocks.BIRCH_SANDWICH_ASSEMBLY_TABLE.get(), "birch");
        sandwichAssemblyTable(ModBlocks.JUNGLE_SANDWICH_ASSEMBLY_TABLE.get(), "jungle");
        sandwichAssemblyTable(ModBlocks.ACACIA_SANDWICH_ASSEMBLY_TABLE.get(), "acacia");
        sandwichAssemblyTable(ModBlocks.DARK_OAK_SANDWICH_ASSEMBLY_TABLE.get(), "dark_oak");
        sandwichAssemblyTable(ModBlocks.CRIMSON_SANDWICH_ASSEMBLY_TABLE.get(), "crimson");
        sandwichAssemblyTable(ModBlocks.WARPED_SANDWICH_ASSEMBLY_TABLE.get(), "warped");

        toaster(ModBlocks.REDSTONE_TOASTER.get());
        toaster(ModBlocks.STICKY_REDSTONE_TOASTER.get());
    }

    private ResourceLocation prefixBlock(String path) {
        return Util.id("block/" + path);
    }

    private ResourceLocation prefixItem(String path) {
        return Util.id("item/" + path);
    }

    private void horizontalBlock(Block block, Function<BlockState, ModelFile> modelFunction, Property<?>... ignored) {
        getVariantBuilder(block)
                .forAllStatesExcept(state -> ConfiguredModel.builder()
                        .modelFile(modelFunction.apply(state))
                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                        .build(), ignored
                );
    }

    private void sandwichAssemblyTable(Block block, String woodType) {
        ModelFile model = models()
                .withExistingParent("block/" + woodType + "_sandwich_assembly_table", prefixBlock("sandwich_assembly_table"))
                .texture("front", prefixBlock(woodType + "_sandwich_assembly_table_front"))
                .texture("side", prefixBlock(woodType + "_sandwich_assembly_table_side"))
                .texture("bottom", new ResourceLocation("block/" + woodType + "_planks"));
        horizontalBlock(block, $ -> model);
    }

    private void toaster(Block block) {
        getVariantBuilder(block)
                .forAllStatesExcept(state -> ConfiguredModel.builder()
                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                        .modelFile(models().getExistingFile(prefixBlock(state.getValue(RedstoneToasterBlock.TOASTING) ? "toasting_redstone_toaster" : "idle_redstone_toaster")))
                        .build(), BlockStateProperties.WATERLOGGED, BlockStateProperties.POWERED);
    }

    private ModelFile createLettuceModel(int growthStage) {
        BlockModelBuilder model = models().getBuilder(prefixBlock("lettuce_stage" + growthStage).toString())
                .ao(false)
                .texture("crop", prefixBlock("lettuce_stage" + growthStage))
                .texture("particle", prefixItem("lettuce_head"));

        if (growthStage > 0) {
            int height = growthStage + 1 + (growthStage >= 3 ? 1 : 0);
            model.element()
                    .from(8 - growthStage, -1, 8 - growthStage)
                    .to(8 + growthStage, -1 + height, 8 + growthStage)
                    .face(Direction.NORTH).end()
                    .face(Direction.EAST).end()
                    .face(Direction.SOUTH).end()
                    .face(Direction.WEST).end()
                    .faces((direction, face) -> face.uvs(growthStage, 8, growthStage * 2, 8 + height / 2F))
                    .face(Direction.UP)
                    .uvs(0, 8, growthStage, 8 + growthStage)
                    .end()
                    .texture("#crop");
        }

        addCropLeaves(model, 8, growthStage, 45, element -> element.faces((faceDirection, face) -> {
            int offset = faceDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 4 : 0;
            face.uvs(0, offset, 8, 4 + offset);
        }));

        return model;
    }

    private void addCropLeaves(BlockModelBuilder model, int size, int offsetFromOrigin, float angle, Consumer<ModelBuilder<?>.ElementBuilder> consumer) {
        Direction.Plane.HORIZONTAL.stream().forEach(direction -> {
            int axisDirectionSign = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1 : -1;
            int xWidth = direction.getAxis() == Direction.Axis.Z ? 16 * axisDirectionSign : 0;
            int yWidth = direction.getAxis() == Direction.Axis.X ? 16 * axisDirectionSign : 0;
            int leafOriginY = -1;
            int leafOriginX = direction.getAxis() == Direction.Axis.X ? 8 - offsetFromOrigin * axisDirectionSign : 8;
            int leafOriginZ = direction.getAxis() == Direction.Axis.Z ? 8 + offsetFromOrigin * axisDirectionSign : 8;
            ModelBuilder<?>.ElementBuilder element = model.element()
                    .shade(false)
                    .from(leafOriginX - xWidth / 2F, leafOriginY, leafOriginZ + yWidth / 2F)
                    .to(leafOriginX + xWidth / 2F, leafOriginY + size, leafOriginZ - yWidth / 2F)
                    .rotation()
                    .origin(leafOriginX, leafOriginY, leafOriginZ)
                    .angle(angle * axisDirectionSign)
                    .axis(direction.getClockWise().getAxis())
                    .end()
                    .face(direction).end()
                    .face(direction.getOpposite()).end()
                    .texture("#crop");
            consumer.accept(element);
        });
    }
}
