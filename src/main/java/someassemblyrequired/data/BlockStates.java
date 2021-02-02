package someassemblyrequired.data;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.block.RedstoneToasterBlock;
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
        horizontalBlock(ModBlocks.SANDWICH.get(), $ -> sandwichModel, BlockStateProperties.WATERLOGGED);

        // lettuce
        getVariantBuilder(ModBlocks.LETTUCE.get())
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(createLettuceModel(state.get(((CropsBlock) ModBlocks.LETTUCE.get()).getAgeProperty())))
                        .build()
                );

        // tomato base model
        createTomatoesBaseModel();

        // tomatoes
        getVariantBuilder(ModBlocks.TOMATOES.get())
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(createTomatoesModel(state.get(((CropsBlock) ModBlocks.TOMATOES.get()).getAgeProperty())))
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

        // cutting board base model
        models().withExistingParent("block/cutting_board", "thin_block")
                .texture("particle", "#texture")
                .element()
                .from(1, 0, 2)
                .to(15, 1, 14)
                .allFaces((direction, face) -> face.texture("#texture"))
                .end();

        sandwichAssemblyTable(ModBlocks.OAK_SANDWICH_ASSEMBLY_TABLE.get(), "oak");
        sandwichAssemblyTable(ModBlocks.SPRUCE_SANDWICH_ASSEMBLY_TABLE.get(), "spruce");
        sandwichAssemblyTable(ModBlocks.BIRCH_SANDWICH_ASSEMBLY_TABLE.get(), "birch");
        sandwichAssemblyTable(ModBlocks.JUNGLE_SANDWICH_ASSEMBLY_TABLE.get(), "jungle");
        sandwichAssemblyTable(ModBlocks.ACACIA_SANDWICH_ASSEMBLY_TABLE.get(), "acacia");
        sandwichAssemblyTable(ModBlocks.DARK_OAK_SANDWICH_ASSEMBLY_TABLE.get(), "dark_oak");
        sandwichAssemblyTable(ModBlocks.CRIMSON_SANDWICH_ASSEMBLY_TABLE.get(), "crimson");
        sandwichAssemblyTable(ModBlocks.WARPED_SANDWICH_ASSEMBLY_TABLE.get(), "warped");

        cuttingBoard(ModBlocks.OAK_CUTTING_BOARD.get(), "oak", "log");
        cuttingBoard(ModBlocks.SPRUCE_CUTTING_BOARD.get(), "spruce", "log");
        cuttingBoard(ModBlocks.BIRCH_CUTTING_BOARD.get(), "birch", "log");
        cuttingBoard(ModBlocks.JUNGLE_CUTTING_BOARD.get(), "jungle", "log");
        cuttingBoard(ModBlocks.ACACIA_CUTTING_BOARD.get(), "acacia", "log");
        cuttingBoard(ModBlocks.DARK_OAK_CUTTING_BOARD.get(), "dark_oak", "log");
        cuttingBoard(ModBlocks.CRIMSON_CUTTING_BOARD.get(), "crimson", "stem");
        cuttingBoard(ModBlocks.WARPED_CUTTING_BOARD.get(), "warped", "stem");

        toaster(ModBlocks.REDSTONE_TOASTER.get());
        toaster(ModBlocks.STICKY_REDSTONE_TOASTER.get());
    }

    private ResourceLocation prefixBlock(String path) {
        return Util.prefix("block/" + path);
    }

    private ResourceLocation prefixItem(String path) {
        return Util.prefix("item/" + path);
    }

    private void horizontalBlock(Block block, Function<BlockState, ModelFile> modelFunction, Property<?>... ignored) {
        getVariantBuilder(block)
                .forAllStatesExcept(state -> ConfiguredModel.builder()
                        .modelFile(modelFunction.apply(state))
                        .rotationY(((int) state.get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle() + 180) % 360)
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

    private void cuttingBoard(Block block, String woodType, String logName) {
        ModelFile model = models()
                .withExistingParent("block/" + woodType + "_cutting_board", prefixBlock("cutting_board"))
                .texture("texture", new ResourceLocation("block/stripped_" + woodType + "_" + logName));
        horizontalBlock(block, $ -> model, BlockStateProperties.WATERLOGGED);
    }

    private void toaster(Block block) {
        getVariantBuilder(block)
                .forAllStatesExcept(state -> ConfiguredModel.builder()
                        .rotationY(((int) state.get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle() + 180) % 360)
                        .modelFile(models().getExistingFile(prefixBlock(state.get(RedstoneToasterBlock.TOASTING) ? "toasting_redstone_toaster" : "idle_redstone_toaster")))
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

    private ModelFile createTomatoesModel(int growthStage) {
        return models()
                .withExistingParent(prefixBlock("tomatoes_stage" + growthStage).toString(), prefixBlock("tomatoes"))
                .texture("crop", prefixBlock("tomatoes_stage" + growthStage));
    }

    private void createTomatoesBaseModel() {
        BlockModelBuilder model = models().getBuilder("block/tomatoes")
                .ao(false)
                .texture("particle", "#crop")
                .texture("pole", prefixBlock("tomatoes_pole"))
                .element()
                .from(7, -1, 7)
                .to(9, 15, 9)
                .face(Direction.NORTH).end()
                .face(Direction.EAST).end()
                .face(Direction.SOUTH).end()
                .face(Direction.WEST).end()
                .faces((direction, face) ->
                        face.uvs(direction.getHorizontalIndex() * 2, 0, direction.getHorizontalIndex() * 2 + 2, 16)
                )
                .face(Direction.UP)
                .uvs(8, 0, 10, 2)
                .end()
                .texture("#pole")
                .end();

        addCropLeaves(model, 16, 2, 0, element -> element.faces((direction, face) -> face.uvs(0, 0, 16, 16)));
    }

    private void addCropLeaves(BlockModelBuilder model, int size, int offsetFromOrigin, float angle, Consumer<ModelBuilder<?>.ElementBuilder> consumer) {
        Direction.Plane.HORIZONTAL.getDirectionValues().forEach(direction -> {
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
                    .axis(direction.rotateY().getAxis())
                    .end()
                    .face(direction).end()
                    .face(direction.getOpposite()).end()
                    .texture("#crop");
            consumer.accept(element);
        });
    }
}
