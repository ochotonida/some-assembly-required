package someassemblyrequired.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
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

        tag(ModTags.LOOT_MEAT).add(
                Items.COOKED_BEEF,
                Items.COOKED_PORKCHOP
        ).addTags(
                ModTags.LOOT_FISH
        ).addOptional(
                vectorwing.farmersdelight.common.registry.ModItems.COOKED_BACON.getId()
        ).addOptional(
                vectorwing.farmersdelight.common.registry.ModItems.COOKED_CHICKEN_CUTS.getId()
        ).addOptional(
                vectorwing.farmersdelight.common.registry.ModItems.COOKED_MUTTON_CHOPS.getId()
        ).addOptional(
                vectorwing.farmersdelight.common.registry.ModItems.BEEF_PATTY.getId()
        );

        tag(ModTags.LOOT_FISH).add(
                Items.COOKED_COD,
                Items.COOKED_SALMON
        ).addOptional(
                vectorwing.farmersdelight.common.registry.ModItems.COOKED_SALMON_SLICE.getId()
        ).addOptional(
                vectorwing.farmersdelight.common.registry.ModItems.COOKED_COD_SLICE.getId()
        );

        tag(ModTags.LOOT_VEGETABLES).add(
                ModItems.CHOPPED_CARROT.get(),
                ModItems.CHOPPED_BEETROOT.get(),
                ModItems.TOMATO_SLICES.get(),
                ModItems.APPLE_SLICES.get()
        ).addOptional(
                vectorwing.farmersdelight.common.registry.ModItems.CABBAGE_LEAF.getId()
        );

        tag(ModTags.LOOT_OTHER).add(
                Items.HONEY_BOTTLE
        ).addOptional(
                vectorwing.farmersdelight.common.registry.ModItems.FRIED_EGG.getId()
        );

        tag(ModTags.LOOT_SPECIAL).add(
                ModItems.TOASTED_BREAD_SLICE.get(),
                Items.MILK_BUCKET,
                Items.POTATO
        ).addOptional(
                vectorwing.farmersdelight.common.registry.ModItems.SQUID_INK_PASTA.getId()
        ).addOptional(
                vectorwing.farmersdelight.common.registry.ModItems.EGG_SANDWICH.getId()
        ).addOptional(
                vectorwing.farmersdelight.common.registry.ModItems.HOT_COCOA.getId()
        ).addOptional(
                vectorwing.farmersdelight.common.registry.ModItems.PUMPKIN_SOUP.getId()
        ).addOptional(
                vectorwing.farmersdelight.common.registry.ModItems.PASTA_WITH_MEATBALLS.getId()
        ).addOptional(
                vectorwing.farmersdelight.common.registry.ModItems.RATATOUILLE.getId()
        ).addOptional(
                new ResourceLocation("create", "builders_tea")
        ).addOptional(
                new ResourceLocation("create", "chocolate_berries")
        );
    }
}
