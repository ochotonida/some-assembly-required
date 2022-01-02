package someassemblyrequired.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;

import javax.annotation.Nullable;

public class ItemTags extends ItemTagsProvider {

    public ItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, SomeAssemblyRequired.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        copy(BlockTags.NON_FLAMMABLE_WOOD, net.minecraft.tags.ItemTags.NON_FLAMMABLE_WOOD);

        tag(net.minecraft.tags.ItemTags.PIGLIN_LOVED).add(
                ModItems.GOLDEN_APPLE_SLICES.get(),
                ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(),
                ModItems.CHOPPED_GOLDEN_CARROT.get()
        );

        addModTags();
        addForgeTags();
    }

    @SuppressWarnings("unchecked")
    private void addModTags() {
        tag(ModTags.BURNT_FOOD).add(
                ModItems.CHARRED_BREAD_SLICE.get(),
                ModItems.CHARRED_FOOD.get()
        );

        tag(ModTags.SANDWICH_BREADS).addTags(
                ModTags.BREAD,
                ModTags.BREAD_SLICES,
                ModTags.TORTILLAS
        );

        tag(ModTags.SMALL_FOODS).add(
                Items.DRIED_KELP
        );

        tag(ModTags.TOASTER_METALS).addTags(
                Tags.Items.INGOTS_IRON,
                Tags.Items.NUGGETS_IRON,
                Tags.Items.INGOTS_GOLD,
                Tags.Items.NUGGETS_GOLD
        ).add(
                ModItems.GOLDEN_APPLE_SLICES.get(),
                ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(),
                ModItems.CHOPPED_GOLDEN_CARROT.get(),
                Items.IRON_AXE,
                Items.IRON_HOE,
                Items.IRON_PICKAXE,
                Items.IRON_SHOVEL,
                Items.IRON_SWORD,
                Items.IRON_BOOTS,
                Items.IRON_LEGGINGS,
                Items.IRON_CHESTPLATE,
                Items.IRON_HELMET,
                Items.IRON_HORSE_ARMOR,
                Items.GOLDEN_AXE,
                Items.GOLDEN_HOE,
                Items.GOLDEN_PICKAXE,
                Items.GOLDEN_SHOVEL,
                Items.GOLDEN_SWORD,
                Items.GOLDEN_BOOTS,
                Items.GOLDEN_LEGGINGS,
                Items.GOLDEN_CHESTPLATE,
                Items.GOLDEN_HELMET,
                Items.GOLDEN_HORSE_ARMOR,
                Items.GOLDEN_CARROT,
                Items.GOLDEN_APPLE,
                Items.ENCHANTED_GOLDEN_APPLE,
                Items.CHAINMAIL_BOOTS,
                Items.CHAINMAIL_LEGGINGS,
                Items.CHAINMAIL_CHESTPLATE,
                Items.CHAINMAIL_HELMET,
                Items.BUCKET,
                Items.COD_BUCKET,
                Items.LAVA_BUCKET,
                Items.MILK_BUCKET,
                Items.PUFFERFISH_BUCKET,
                Items.SALMON_BUCKET,
                Items.TROPICAL_FISH_BUCKET,
                Items.WATER_BUCKET,
                Items.MINECART,
                Items.CHEST_MINECART,
                Items.COMMAND_BLOCK_MINECART,
                Items.FURNACE_MINECART,
                Items.HOPPER_MINECART,
                Items.TNT_MINECART,
                Items.COMPASS,
                Items.FLINT_AND_STEEL,
                Items.SHEARS,
                Items.SNOWBALL,
                Items.CLOCK
        );
    }

    @SuppressWarnings("unchecked")
    private void addForgeTags() {
        tag(ModTags.COOKED_BACON);
        tag(ModTags.CROPS_TOMATOES);

        tag(ModTags.BREAD).addTags(
                ModTags.BREAD_WHEAT
        );

        tag(ModTags.BREAD_WHEAT).add(
                Items.BREAD
        );

        tag(ModTags.BREAD_SLICES).addTags(
                ModTags.BREAD_SLICES_WHEAT
        );

        tag(ModTags.BREAD_SLICES_WHEAT).add(
                ModItems.BREAD_SLICE.get(),
                ModItems.TOASTED_BREAD_SLICE.get(),
                ModItems.CHARRED_BREAD_SLICE.get()
        );

        tag(ModTags.CROPS).addTags(
                ModTags.CROPS_LETTUCE
        );

        tag(ModTags.CROPS_LETTUCE).add(
                ModItems.LETTUCE_HEAD.get(),
                ModItems.LETTUCE_LEAF.get()
        );

        tag(ModTags.FRUITS).addTags(
                ModTags.FRUITS_APPLE
        );

        tag(ModTags.FRUITS_APPLE).add(
                Items.APPLE,
                ModItems.APPLE_SLICES.get()
        );

        tag(ModTags.SALAD_INGREDIENTS).addTags(
                ModTags.SALAD_INGREDIENTS_LETTUCE
        );

        tag(ModTags.SALAD_INGREDIENTS_LETTUCE).add(
                ModItems.LETTUCE_HEAD.get(),
                ModItems.LETTUCE_LEAF.get()
        );

        tag(ModTags.SEEDS).addTags(
                ModTags.SEEDS_LETTUCE
        );

        tag(ModTags.SEEDS_LETTUCE).add(
                ModItems.LETTUCE_SEEDS.get()
        );

        tag(ModTags.TORTILLAS);
        tag(ModTags.VEGETABLES).addTags(
                ModTags.VEGETABLES_BEETROOT,
                ModTags.VEGETABLES_CARROT,
                ModTags.VEGETABLES_LETTUCE,
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

        tag(ModTags.VEGETABLES_LETTUCE).add(
                ModItems.LETTUCE_HEAD.get(),
                ModItems.LETTUCE_LEAF.get()
        );

        tag(ModTags.VEGETABLES_TOMATO).add(
                ModItems.TOMATO_SLICES.get()
        );

        tag(ModTags.VINEGAR_INGREDIENTS).addTags(
                ModTags.VINEGAR_INGREDIENTS_APPLE
        );

        tag(ModTags.VINEGAR_INGREDIENTS_APPLE).add(
                Items.APPLE,
                ModItems.APPLE_SLICES.get()
        );
    }
}
