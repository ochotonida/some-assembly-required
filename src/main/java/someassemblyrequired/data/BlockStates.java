package someassemblyrequired.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.block.SandwichBlock;
import someassemblyrequired.init.ModBlocks;
import someassemblyrequired.util.Util;

import java.util.function.Function;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SomeAssemblyRequired.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModelFile sandwichModel = models()
                .getBuilder(prefixBlock("sandwich").toString())
                .texture("particle", prefixItem("bread_slice"));

        horizontalBlock(ModBlocks.SANDWICH.get(), state -> sandwichModel, BlockStateProperties.WATERLOGGED, SandwichBlock.SIZE);

        String name = ModBlocks.SANDWICHING_STATION.getId().getPath();
        ModelFile model = models().cubeBottomTop(
                "block/" + name,
                prefixBlock(name + "_side"),
                prefixBlock(name + "_bottom"),
                prefixBlock(name + "_top")
        );

        simpleBlock(ModBlocks.SANDWICHING_STATION.get(), model);
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
}
