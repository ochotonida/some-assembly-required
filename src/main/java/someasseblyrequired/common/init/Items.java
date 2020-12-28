package someasseblyrequired.common.init;

import net.minecraft.block.Block;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import someasseblyrequired.SomeAssemblyRequired;
import someasseblyrequired.client.SandwichItemRenderer;
import someasseblyrequired.common.item.*;

import static net.minecraft.item.Items.AIR;

@ObjectHolder(SomeAssemblyRequired.MODID)
public class Items {

    public static final Item CHARRED_MORSEL = AIR;

    public static final Item SANDWICH = AIR;
    public static final Item SPREAD = AIR;
    public static final Item TOASTED_BREAD_SLICE = AIR;
    public static final Item MAYONNAISE_BOTTLE = AIR;
    public static final Item CHARRED_FOOD = AIR;

    private static final ItemGroup CREATIVE_TAB = new ItemGroup(SomeAssemblyRequired.MODID) {

        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            NonNullList<ItemStack> ingredients = NonNullList.create();
            ingredients.add(new ItemStack(TOASTED_BREAD_SLICE));
            ItemStack spread = new ItemStack(Items.SPREAD);
            spread.getOrCreateTag().putInt("Color", 0xF08A1D);
            ingredients.add(spread);
            ingredients.add(new ItemStack(TOASTED_BREAD_SLICE));
            ItemStack sandwich = new ItemStack(SANDWICH);
            sandwich.getOrCreateChildTag("BlockEntityTag").put("Ingredients", new ItemStackHandler(ingredients).serializeNBT());
            return sandwich;
        }
    };

    private static Item createItem(Block block, String tooltipTranslationKey) {
        //noinspection ConstantConditions
        return new TooltippedBlockItem(block, new Item.Properties().group(CREATIVE_TAB), tooltipTranslationKey).setRegistryName(block.getRegistryName());
    }

    private static Item createItem(Block block) {
        //noinspection ConstantConditions
        return new TooltippedBlockItem(block, new Item.Properties().group(CREATIVE_TAB)).setRegistryName(block.getRegistryName());
    }

    public static void register(IForgeRegistry<Item> registry) {

        registry.registerAll(
                createItem(Blocks.OAK_SANDWICH_ASSEMBLY_TABLE, "sandwich_assembly_table"),
                createItem(Blocks.SPRUCE_SANDWICH_ASSEMBLY_TABLE, "sandwich_assembly_table"),
                createItem(Blocks.BIRCH_SANDWICH_ASSEMBLY_TABLE, "sandwich_assembly_table"),
                createItem(Blocks.JUNGLE_SANDWICH_ASSEMBLY_TABLE, "sandwich_assembly_table"),
                createItem(Blocks.ACACIA_SANDWICH_ASSEMBLY_TABLE, "sandwich_assembly_table"),
                createItem(Blocks.DARK_OAK_SANDWICH_ASSEMBLY_TABLE, "sandwich_assembly_table"),
                createItem(Blocks.CRIMSON_SANDWICH_ASSEMBLY_TABLE, "sandwich_assembly_table"),
                createItem(Blocks.WARPED_SANDWICH_ASSEMBLY_TABLE, "sandwich_assembly_table"),

                createItem(Blocks.OAK_CUTTING_BOARD, "cutting_board"),
                createItem(Blocks.SPRUCE_CUTTING_BOARD, "cutting_board"),
                createItem(Blocks.BIRCH_CUTTING_BOARD, "cutting_board"),
                createItem(Blocks.JUNGLE_CUTTING_BOARD, "cutting_board"),
                createItem(Blocks.ACACIA_CUTTING_BOARD, "cutting_board"),
                createItem(Blocks.DARK_OAK_CUTTING_BOARD, "cutting_board"),
                createItem(Blocks.CRIMSON_CUTTING_BOARD, "cutting_board"),
                createItem(Blocks.WARPED_CUTTING_BOARD, "cutting_board"),

                createItem(Blocks.REDSTONE_TOASTER),
                createItem(Blocks.STICKY_REDSTONE_TOASTER),

                new SandwichItem(Blocks.SANDWICH, new Item.Properties().maxStackSize(8).food(new Food.Builder().setAlwaysEdible().build()).setISTER(() -> SandwichItemRenderer::new)).setRegistryName(SomeAssemblyRequired.MODID, "sandwich"),

                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).maxStackSize(1)).setRegistryName(SomeAssemblyRequired.MODID, "kitchen_knife"),
                new SpreadItem(new Item.Properties()).setRegistryName(SomeAssemblyRequired.MODID, "spread"),

                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.BREAD_SLICE)).setRegistryName(SomeAssemblyRequired.MODID, "bread_slice"),
                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.TOASTED_BREAD_SLICE)).setRegistryName(SomeAssemblyRequired.MODID, "toasted_bread_slice"),
                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.CHARRED_FOOD)).setRegistryName(SomeAssemblyRequired.MODID, "charred_bread_slice"),
                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.APPLE_SLICES)).setRegistryName(SomeAssemblyRequired.MODID, "apple_slices"),
                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.GOLDEN_APPLE_SLICES)).setRegistryName(SomeAssemblyRequired.MODID, "golden_apple_slices"),
                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.ENCHANTED_GOLDEN_APPLE_SLICES), true).setRegistryName(SomeAssemblyRequired.MODID, "enchanted_golden_apple_slices"),
                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.CHOPPED_CARROT)).setRegistryName(SomeAssemblyRequired.MODID, "chopped_carrot"),
                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.CHOPPED_GOLDEN_CARROT)).setRegistryName(SomeAssemblyRequired.MODID, "chopped_golden_carrot"),
                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.CHOPPED_BEETROOT)).setRegistryName(SomeAssemblyRequired.MODID, "chopped_beetroot"),
                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.PORK_CUTS)).setRegistryName(SomeAssemblyRequired.MODID, "pork_cuts"),
                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.BACON_STRIPS)).setRegistryName(SomeAssemblyRequired.MODID, "bacon_strips"),
                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.TOASTED_CRIMSON_FUNGUS)).setRegistryName(SomeAssemblyRequired.MODID, "toasted_crimson_fungus"),
                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.TOASTED_WARPED_FUNGUS)).setRegistryName(SomeAssemblyRequired.MODID, "toasted_warped_fungus"),
                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.SLICED_TOASTED_CRIMSON_FUNGUS)).setRegistryName(SomeAssemblyRequired.MODID, "sliced_toasted_crimson_fungus"),
                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.SLICED_TOASTED_WARPED_FUNGUS)).setRegistryName(SomeAssemblyRequired.MODID, "sliced_toasted_warped_fungus"),

                new DrinkableBottleItem(new Item.Properties().group(CREATIVE_TAB).containerItem(net.minecraft.item.Items.GLASS_BOTTLE).maxStackSize(16).food(Foods.MAYONNAISE), SoundEvents.ITEM_HONEY_BOTTLE_DRINK).setRegistryName(SomeAssemblyRequired.MODID, "mayonnaise_bottle"),
                new DrinkableBottleItem(new Item.Properties().group(CREATIVE_TAB).containerItem(net.minecraft.item.Items.GLASS_BOTTLE).maxStackSize(16).food(Foods.SWEET_BERRY_JAM), SoundEvents.ITEM_HONEY_BOTTLE_DRINK).setRegistryName(SomeAssemblyRequired.MODID, "sweet_berry_jam_bottle"),

                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.CHARRED_MORSEL)).setRegistryName(SomeAssemblyRequired.MODID, "charred_morsel"),
                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.CHARRED_FOOD)).setRegistryName(SomeAssemblyRequired.MODID, "charred_food")
        );
    }

    public static class Foods {
        public static final Food CHARRED_FOOD = new Food.Builder().hunger(1).saturation(0.1F).effect(() -> new EffectInstance(Effects.NAUSEA, 100, 0), 0.4F).build();
        public static final Food CHARRED_MORSEL = new Food.Builder().hunger(1).saturation(0).effect(() -> new EffectInstance(Effects.NAUSEA, 100, 0), 0.2F).build();
        public static final Food BREAD_SLICE = new Food.Builder().hunger(2).saturation(0.5F).fastToEat().build();
        public static final Food TOASTED_BREAD_SLICE = new Food.Builder().hunger(3).saturation(0.6F).fastToEat().build();
        public static final Food APPLE_SLICES = new Food.Builder().hunger(2).saturation(0.5F).fastToEat().build();
        public static final Food GOLDEN_APPLE_SLICES = new Food.Builder().hunger(3).saturation(0.7F).fastToEat().setAlwaysEdible().effect(() -> new EffectInstance(Effects.REGENERATION, 50, 1), 1).effect(() -> new EffectInstance(Effects.ABSORPTION, 1200, 0), 1).build();
        public static final Food ENCHANTED_GOLDEN_APPLE_SLICES = new Food.Builder().hunger(4).saturation(0.8F).fastToEat().setAlwaysEdible().effect(() -> new EffectInstance(Effects.REGENERATION, 200, 1), 1).effect(() -> new EffectInstance(Effects.RESISTANCE, 3000, 0), 1).effect(() -> new EffectInstance(Effects.FIRE_RESISTANCE, 3000, 0), 1).effect(() -> new EffectInstance(Effects.ABSORPTION, 1200, 3), 1).build();
        public static final Food CHOPPED_CARROT = new Food.Builder().hunger(2).saturation(0.5F).fastToEat().build();
        public static final Food CHOPPED_GOLDEN_CARROT = new Food.Builder().hunger(3).saturation(0.8F).fastToEat().build();
        public static final Food CHOPPED_BEETROOT = new Food.Builder().hunger(3).saturation(0.6F).fastToEat().build();
        public static final Food MAYONNAISE = new Food.Builder().hunger(3).saturation(0.1F).build();
        public static final Food SWEET_BERRY_JAM = new Food.Builder().hunger(5).saturation(0.5F).build();
        public static final Food PORK_CUTS = new Food.Builder().hunger(2).saturation(0.3F).meat().build();
        public static final Food BACON_STRIPS = new Food.Builder().hunger(4).saturation(0.7F).meat().build();
        public static final Food TOASTED_CRIMSON_FUNGUS = (new Food.Builder()).hunger(4).saturation(0.5F).build();
        public static final Food SLICED_TOASTED_CRIMSON_FUNGUS = (new Food.Builder()).hunger(2).saturation(0.6F).fastToEat().build();
        public static final Food TOASTED_WARPED_FUNGUS = (new Food.Builder()).hunger(5).saturation(0.4F).build();
        public static final Food SLICED_TOASTED_WARPED_FUNGUS = (new Food.Builder()).hunger(3).saturation(0.5F).fastToEat().build();
    }
}
