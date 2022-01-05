package someassemblyrequired.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.block.redstonetoaster.RedstoneToasterBlock;
import someassemblyrequired.common.block.sandwich.SandwichBlock;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.util.Util;

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
}
