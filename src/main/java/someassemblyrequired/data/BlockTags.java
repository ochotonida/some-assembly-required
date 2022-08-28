package someassemblyrequired.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModTags;

public class BlockTags extends BlockTagsProvider {

    public BlockTags(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, SomeAssemblyRequired.MODID, helper);
    }

    @Override
    protected void addTags() {
        tag(ModTags.SANDWICHING_STATIONS).add(ModBlocks.SANDWICHING_STATION.get());
    }
}
