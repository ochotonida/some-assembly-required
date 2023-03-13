package someassemblyrequired.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.init.ModBlocks;
import someassemblyrequired.init.ModTags;

public class BlockTags extends BlockTagsProvider {

    public BlockTags(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, SomeAssemblyRequired.MODID, helper);
    }

    @Override
    protected void addTags() {
        tag(ModTags.SANDWICHING_STATIONS).add(ModBlocks.SANDWICHING_STATION.get());
        tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.SANDWICHING_STATION.get());
    }
}
