package someassemblyrequired.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModBlocks;

import javax.annotation.Nullable;

public class BlockTags extends BlockTagsProvider {

    public BlockTags(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, SomeAssemblyRequired.MODID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(net.minecraft.tags.BlockTags.CROPS).add(
                ModBlocks.LETTUCE.get(),
                ModBlocks.TOMATOES.get()
        );
        getOrCreateBuilder(net.minecraft.tags.BlockTags.NON_FLAMMABLE_WOOD).add(
                ModBlocks.CRIMSON_CUTTING_BOARD.get(),
                ModBlocks.WARPED_CUTTING_BOARD.get()
        );
    }
}
