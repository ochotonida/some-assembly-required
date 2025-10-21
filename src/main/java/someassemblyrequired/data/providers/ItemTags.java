package someassemblyrequired.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ItemTags extends ItemTagsProvider {

    private final Ingredients ingredients;

    public ItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper, Ingredients ingredients) {
        super(packOutput, lookupProvider, blockTags, SomeAssemblyRequired.MOD_ID, existingFileHelper);
        this.ingredients = ingredients;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(HolderLookup.Provider provider) {
        tag(net.minecraft.tags.ItemTags.PIGLIN_LOVED).add(
                ModItems.GOLDEN_APPLE_SLICES.get(),
                ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(),
                ModItems.CHOPPED_GOLDEN_CARROT.get()
        );

        tag(ModTags.SANDWICH_BREAD).addTags(
                ModTags.BREAD_SLICES,
                ModTags.BURGER_BUNS
        );

        tag(ModTags.BURGER_BUNS).add(
                ModItems.BURGER_BUN_BOTTOM.get(),
                ModItems.BURGER_BUN_TOP.get()
        );

        tag(ModTags.BREAD_SLICES).add(
                ModItems.BREAD_SLICE.get(),
                ModItems.TOASTED_BREAD_SLICE.get()
        );

        var tagBuilder = tag(ModTags.SPECIAL_SANDWICH_FILLINGS);
        for (ResourceKey<Item> item : ingredients.collectSpecialFillings()) {
            if (isLoadedByDefault(item)) {
                tagBuilder.add(item);
            } else {
                tagBuilder.addOptional(item.location());
            }
        }
    }

    private boolean isLoadedByDefault(ResourceKey<Item> key) {
        return key.location().getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE)
                || key.location().getNamespace().equals(SomeAssemblyRequired.MOD_ID);
    }
}
