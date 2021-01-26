package someassemblyrequired.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
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
    protected void registerTags() {
        copy(BlockTags.NON_FLAMMABLE_WOOD, net.minecraft.tags.ItemTags.NON_FLAMMABLE_WOOD);

        getOrCreateBuilder(net.minecraft.tags.ItemTags.PIGLIN_LOVED).add(
                ModItems.GOLDEN_APPLE_SLICES.get(),
                ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get(),
                ModItems.CHOPPED_GOLDEN_CARROT.get()
        );

        addModTags();
        addForgeTags();
    }

    @SuppressWarnings("unchecked")
    private void addModTags() {
        getOrCreateBuilder(ModTags.BURNT_FOOD).add(
                ModItems.CHARRED_BREAD_SLICE.get(),
                ModItems.CHARRED_FOOD.get()
        );

        getOrCreateBuilder(ModTags.SANDWICH_BREADS).addTags(
                ModTags.BREAD,
                ModTags.BREAD_SLICES,
                ModTags.TORTILLAS
        );

        getOrCreateBuilder(ModTags.SMALL_FOODS).add(
                Items.DRIED_KELP
        );

        getOrCreateBuilder(ModTags.TOASTER_METALS).addTags(
                Tags.Items.INGOTS_IRON,
                Tags.Items.NUGGETS_IRON,
                Tags.Items.INGOTS_GOLD,
                Tags.Items.NUGGETS_GOLD
        ).add(
                ModItems.KITCHEN_KNIFE.get(),
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
        getOrCreateBuilder(ModTags.BREAD).addTags(
                ModTags.BREAD_WHEAT
        );

        getOrCreateBuilder(ModTags.BREAD_WHEAT).add(
                Items.BREAD
        );

        getOrCreateBuilder(ModTags.BREAD_SLICES).addTags(
                ModTags.BREAD_SLICES_WHEAT
        );

        getOrCreateBuilder(ModTags.BREAD_SLICES_WHEAT).add(
                ModItems.BREAD_SLICE.get(),
                ModItems.TOASTED_WARPED_FUNGUS.get(),
                ModItems.CHARRED_BREAD_SLICE.get()
        );

        getOrCreateBuilder(ModTags.COOKED_BACON).add(
                ModItems.BACON_STRIPS.get()
        );

        getOrCreateBuilder(ModTags.COOKED_PORK).add(
                Items.COOKED_PORKCHOP,
                ModItems.BACON_STRIPS.get()
        );

        getOrCreateBuilder(ModTags.CROPS).addTags(
                ModTags.CROPS_LETTUCE,
                ModTags.CROPS_TOMATOES
        );

        getOrCreateBuilder(ModTags.CROPS_LETTUCE).add(
                ModItems.LETTUCE_HEAD.get(),
                ModItems.LETTUCE_LEAF.get()
        );

        getOrCreateBuilder(ModTags.CROPS_TOMATOES).add(
                ModItems.TOMATO.get()
        );

        getOrCreateBuilder(ModTags.FRUITS).addTags(
                ModTags.FRUITS_APPLE
        );

        getOrCreateBuilder(ModTags.FRUITS_APPLE).add(
                Items.APPLE,
                ModItems.APPLE_SLICES.get()
        );

        getOrCreateBuilder(ModTags.RAW_BACON).add(
                ModItems.PORK_CUTS.get()
        );

        getOrCreateBuilder(ModTags.RAW_PORK).add(
                Items.PORKCHOP,
                ModItems.PORK_CUTS.get()
        );

        getOrCreateBuilder(ModTags.SALAD_INGREDIENTS).addTags(
                ModTags.SALAD_INGREDIENTS_LETTUCE
        );

        getOrCreateBuilder(ModTags.SALAD_INGREDIENTS_LETTUCE).add(
                ModItems.LETTUCE_HEAD.get(),
                ModItems.LETTUCE_LEAF.get()
        );

        getOrCreateBuilder(ModTags.SEEDS).addTags(
                ModTags.SEEDS_LETTUCE,
                ModTags.SEEDS_TOMATO
        );

        getOrCreateBuilder(ModTags.SEEDS_LETTUCE).add(
                ModItems.LETTUCE_SEEDS.get()
        );

        getOrCreateBuilder(ModTags.SEEDS_TOMATO).add(
                ModItems.TOMATO_SEEDS.get()
        );

        getOrCreateBuilder(ModTags.TOOLS).addTags(
                ModTags.TOOLS_KNIVES
        );

        getOrCreateBuilder(ModTags.TOOLS_KNIVES).add(
                ModItems.KITCHEN_KNIFE.get()
        );

        getOrCreateBuilder(ModTags.TORTILLAS);
        getOrCreateBuilder(ModTags.VEGETABLES).addTags(
                ModTags.VEGETABLES_BEETROOT,
                ModTags.VEGETABLES_CARROT,
                ModTags.VEGETABLES_LETTUCE,
                ModTags.VEGETABLES_TOMATO
        );

        getOrCreateBuilder(ModTags.VEGETABLES_BEETROOT).add(
                Items.BEETROOT,
                ModItems.CHOPPED_BEETROOT.get()
        );

        getOrCreateBuilder(ModTags.VEGETABLES_CARROT).add(
                Items.CARROT,
                ModItems.CHOPPED_CARROT.get()
        );

        getOrCreateBuilder(ModTags.VEGETABLES_LETTUCE).add(
                ModItems.LETTUCE_HEAD.get(),
                ModItems.LETTUCE_LEAF.get()
        );

        getOrCreateBuilder(ModTags.VEGETABLES_TOMATO).add(
                ModItems.TOMATO.get(),
                ModItems.TOMATO_SLICES.get()
        );

        getOrCreateBuilder(ModTags.VINEGAR_INGREDIENTS).addTags(
                ModTags.VINEGAR_INGREDIENTS_APPLE
        );

        getOrCreateBuilder(ModTags.VINEGAR_INGREDIENTS_APPLE).add(
                Items.APPLE,
                ModItems.APPLE_SLICES.get()
        );
    }
}
