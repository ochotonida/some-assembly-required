package someassemblyrequired.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
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

        tag(ModTags.COOKED_BACON);

        tag(ModTags.FRUITS).addTags(
                ModTags.FRUITS_APPLE
        );

        tag(ModTags.FRUITS_APPLE).add(
                Items.APPLE,
                ModItems.APPLE_SLICES.get()
        );

        tag(ModTags.SALAD_INGREDIENTS);

        tag(ModTags.VEGETABLES).addTags(
                ModTags.VEGETABLES_BEETROOT,
                ModTags.VEGETABLES_CARROT,
                ModTags.VEGETABLES_TOMATO
        );

        tag(ModTags.VEGETABLES_BEETROOT).add(
                Items.BEETROOT,
                ModItems.CHOPPED_BEETROOT.get()
        );

        tag(ModTags.VEGETABLES_CARROT).add(
                Items.CARROT,
                ModItems.CHOPPED_CARROT.get()
        );

        tag(ModTags.VEGETABLES_TOMATO).add(
                ModItems.TOMATO_SLICES.get()
        );
    }
}
