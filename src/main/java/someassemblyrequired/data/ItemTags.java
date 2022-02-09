package someassemblyrequired.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;

import javax.annotation.Nullable;

public class ItemTags extends ItemTagsProvider {

    public ItemTags(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, new BlockTagsProvider(dataGenerator, SomeAssemblyRequired.MODID, existingFileHelper), SomeAssemblyRequired.MODID, existingFileHelper);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags() {
        tag(net.minecraft.tags.ItemTags.PIGLIN_LOVED).add(
                ModItems.GOLDEN_APPLE_SLICES.get(),
                ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(),
                ModItems.CHOPPED_GOLDEN_CARROT.get()
        );

        tag(ModTags.SANDWICH_BREAD).addTags(
                ModTags.BREAD_SLICES
        );

        tag(ModTags.BREAD_SLICES).addTags(
                ModTags.BREAD_SLICES_WHEAT
        );

        tag(ModTags.BREAD_SLICES_WHEAT).add(
                ModItems.BREAD_SLICE.get(),
                ModItems.TOASTED_BREAD_SLICE.get()
        );
    }
}
