package someasseblyrequired.common.init;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import someasseblyrequired.SomeAssemblyRequired;
import someasseblyrequired.client.SandwichItemRenderer;
import someasseblyrequired.common.item.DrinkableBottleItem;
import someasseblyrequired.common.item.SandwichItem;
import someasseblyrequired.common.item.SpreadItem;
import someasseblyrequired.common.item.TooltippedItem;

import static net.minecraft.item.Items.AIR;

@ObjectHolder(SomeAssemblyRequired.MODID)
public class Items {

    public static final Item CHARRED_MORSEL = AIR;

    public static final Item SANDWICH = AIR;
    public static final Item SPREAD = AIR;
    public static final Item BREAD_SLICE = AIR;
    public static final Item TOASTED_BREAD_SLICE = AIR;
    public static final Item MAYONNAISE_BOTTLE = AIR;
    public static final Item CHARRED_FOOD = AIR;
    private static final ItemGroup CREATIVE_TAB = new ItemGroup(SomeAssemblyRequired.MODID) {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(BREAD_SLICE);
        }
    };

    private static Item createItem(Block block) {
        //noinspection ConstantConditions
        return new BlockItem(block, new Item.Properties().group(CREATIVE_TAB)).setRegistryName(block.getRegistryName());
    }

    public static void register(IForgeRegistry<Item> registry) {

        registry.registerAll(
                createItem(Blocks.OAK_SANDWICH_ASSEMBLY_TABLE),
                createItem(Blocks.SPRUCE_SANDWICH_ASSEMBLY_TABLE),
                createItem(Blocks.BIRCH_SANDWICH_ASSEMBLY_TABLE),
                createItem(Blocks.JUNGLE_SANDWICH_ASSEMBLY_TABLE),
                createItem(Blocks.ACACIA_SANDWICH_ASSEMBLY_TABLE),
                createItem(Blocks.DARK_OAK_SANDWICH_ASSEMBLY_TABLE),
                createItem(Blocks.CRIMSON_SANDWICH_ASSEMBLY_TABLE),
                createItem(Blocks.WARPED_SANDWICH_ASSEMBLY_TABLE),

                createItem(Blocks.OAK_CUTTING_BOARD),
                createItem(Blocks.SPRUCE_CUTTING_BOARD),
                createItem(Blocks.BIRCH_CUTTING_BOARD),
                createItem(Blocks.JUNGLE_CUTTING_BOARD),
                createItem(Blocks.ACACIA_CUTTING_BOARD),
                createItem(Blocks.DARK_OAK_CUTTING_BOARD),
                createItem(Blocks.CRIMSON_CUTTING_BOARD),
                createItem(Blocks.WARPED_CUTTING_BOARD),

                new BlockItem(Blocks.REDSTONE_TOASTER, new Item.Properties().group(CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "redstone_toaster"),
                new BlockItem(Blocks.STICKY_REDSTONE_TOASTER, new Item.Properties().group(CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "sticky_redstone_toaster"),

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

                new DrinkableBottleItem(new Item.Properties().group(CREATIVE_TAB).containerItem(net.minecraft.item.Items.GLASS_BOTTLE).maxStackSize(16).food(Foods.MAYONNAISE), SoundEvents.ITEM_HONEY_BOTTLE_DRINK).setRegistryName(SomeAssemblyRequired.MODID, "mayonnaise_bottle"),

                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.CHARRED_MORSEL)).setRegistryName(SomeAssemblyRequired.MODID, "charred_morsel"),
                new TooltippedItem(new Item.Properties().group(CREATIVE_TAB).food(Foods.CHARRED_FOOD)).setRegistryName(SomeAssemblyRequired.MODID, "charred_food"),
                new Item(new Item.Properties().group(CREATIVE_TAB).food(Foods.CHARRED_MEAT)).setRegistryName(SomeAssemblyRequired.MODID, "charred_beef")
        );
    }

    public static class Foods {
        public static Food CHARRED_FOOD = new Food.Builder().hunger(1).saturation(0.1F).effect(() -> new EffectInstance(Effects.NAUSEA, 100, 0), 0.4F).build();
        public static Food CHARRED_MORSEL = new Food.Builder().hunger(1).saturation(0).effect(() -> new EffectInstance(Effects.NAUSEA, 100, 0), 0.2F).build();
        public static Food CHARRED_MEAT = new Food.Builder().hunger(1).saturation(0.1F).effect(() -> new EffectInstance(Effects.NAUSEA, 100, 0), 0.4F).meat().build();
        public static Food BREAD_SLICE = new Food.Builder().hunger(2).saturation(0.5F).fastToEat().build();
        public static Food TOASTED_BREAD_SLICE = new Food.Builder().hunger(3).saturation(0.6F).fastToEat().build();
        public static Food APPLE_SLICES = new Food.Builder().hunger(2).saturation(0.5F).fastToEat().build();
        public static Food GOLDEN_APPLE_SLICES = new Food.Builder().hunger(3).saturation(0.7F).fastToEat().setAlwaysEdible().effect(() -> new EffectInstance(Effects.REGENERATION, 50, 1), 1).effect(() -> new EffectInstance(Effects.ABSORPTION, 1200, 0), 1).build();
        public static Food ENCHANTED_GOLDEN_APPLE_SLICES = new Food.Builder().hunger(4).saturation(0.8F).fastToEat().setAlwaysEdible().effect(() -> new EffectInstance(Effects.REGENERATION, 200, 1), 1).effect(() -> new EffectInstance(Effects.RESISTANCE, 3000, 0), 1).effect(() -> new EffectInstance(Effects.FIRE_RESISTANCE, 3000, 0), 1).effect(() -> new EffectInstance(Effects.ABSORPTION, 1200, 3), 1).build();
        public static Food CHOPPED_CARROT = new Food.Builder().hunger(2).saturation(0.5F).fastToEat().build();
        public static Food CHOPPED_GOLDEN_CARROT = new Food.Builder().hunger(3).saturation(0.8F).fastToEat().build();
        public static Food CHOPPED_BEETROOT = new Food.Builder().hunger(3).saturation(0.6F).fastToEat().build();
        public static Food MAYONNAISE = new Food.Builder().hunger(3).saturation(0.1F).build();
    }
}
