package someasseblyrequired.common.init;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import someasseblyrequired.SomeAssemblyRequired;
import someasseblyrequired.client.SandwichItemRenderer;
import someasseblyrequired.common.item.SandwichItem;
import someasseblyrequired.common.item.SpreadItem;
import someasseblyrequired.common.item.TooltippedItem;

import static net.minecraft.item.Items.AIR;

@ObjectHolder(SomeAssemblyRequired.MODID)
public class Items {

    public static final Item SANDWICH = AIR;
    public static final Item SPREAD = AIR;
    public static final Item BREAD_SLICE = AIR;
    public static final Item TOASTED_BREAD_SLICE = AIR;

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                new BlockItem(Blocks.OAK_SANDWICH_ASSEMBLY_TABLE, new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "oak_sandwich_assembly_table"),
                new BlockItem(Blocks.SPRUCE_SANDWICH_ASSEMBLY_TABLE, new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "spruce_sandwich_assembly_table"),
                new BlockItem(Blocks.BIRCH_SANDWICH_ASSEMBLY_TABLE, new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "birch_sandwich_assembly_table"),
                new BlockItem(Blocks.JUNGLE_SANDWICH_ASSEMBLY_TABLE, new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "jungle_sandwich_assembly_table"),
                new BlockItem(Blocks.ACACIA_SANDWICH_ASSEMBLY_TABLE, new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "acacia_sandwich_assembly_table"),
                new BlockItem(Blocks.DARK_OAK_SANDWICH_ASSEMBLY_TABLE, new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "dark_oak_sandwich_assembly_table"),
                new BlockItem(Blocks.CRIMSON_SANDWICH_ASSEMBLY_TABLE, new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "crimson_sandwich_assembly_table"),
                new BlockItem(Blocks.WARPED_SANDWICH_ASSEMBLY_TABLE, new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "warped_sandwich_assembly_table"),

                new BlockItem(Blocks.OAK_CUTTING_BOARD, new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "oak_cutting_board"),
                new BlockItem(Blocks.SPRUCE_CUTTING_BOARD, new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "spruce_cutting_board"),
                new BlockItem(Blocks.BIRCH_CUTTING_BOARD, new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "birch_cutting_board"),
                new BlockItem(Blocks.JUNGLE_CUTTING_BOARD, new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "jungle_cutting_board"),
                new BlockItem(Blocks.ACACIA_CUTTING_BOARD, new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "acacia_cutting_board"),
                new BlockItem(Blocks.DARK_OAK_CUTTING_BOARD, new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "dark_oak_cutting_board"),
                new BlockItem(Blocks.CRIMSON_CUTTING_BOARD, new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "crimson_cutting_board"),
                new BlockItem(Blocks.WARPED_CUTTING_BOARD, new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "warped_cutting_board"),

                new SandwichItem(Blocks.SANDWICH, new Item.Properties().maxStackSize(8).food(new Food.Builder().setAlwaysEdible().build()).setISTER(() -> SandwichItemRenderer::new)).setRegistryName(SomeAssemblyRequired.MODID, "sandwich"),

                new TooltippedItem(new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB)).setRegistryName(SomeAssemblyRequired.MODID, "kitchen_knife"),
                new SpreadItem(new Item.Properties()).setRegistryName(SomeAssemblyRequired.MODID, "spread"),

                new TooltippedItem(new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB).food(new Food.Builder().hunger(2).saturation(0.5F).fastToEat().build())).setRegistryName(SomeAssemblyRequired.MODID, "bread_slice"),
                new TooltippedItem(new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB).food(new Food.Builder().hunger(3).saturation(0.6F).fastToEat().build())).setRegistryName(SomeAssemblyRequired.MODID, "toasted_bread_slice"),
                new TooltippedItem(new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB).food(new Food.Builder().hunger(2).saturation(0.5F).fastToEat().build())).setRegistryName(SomeAssemblyRequired.MODID, "apple_slices"),
                new TooltippedItem(new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB).food(new Food.Builder().hunger(3).saturation(0.7F).fastToEat().setAlwaysEdible().effect(() -> new EffectInstance(Effects.REGENERATION, 50, 1), 1).effect(() -> new EffectInstance(Effects.ABSORPTION, 1200, 0), 1).build())).setRegistryName(SomeAssemblyRequired.MODID, "golden_apple_slices"),
                new TooltippedItem(new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB).food(new Food.Builder().hunger(4).saturation(0.8F).fastToEat().setAlwaysEdible().effect(() -> new EffectInstance(Effects.REGENERATION, 200, 1), 1).effect(() -> new EffectInstance(Effects.RESISTANCE, 3000, 0), 1).effect(() -> new EffectInstance(Effects.FIRE_RESISTANCE, 3000, 0), 1).effect(() -> new EffectInstance(Effects.ABSORPTION, 1200, 3), 1).build()), true).setRegistryName(SomeAssemblyRequired.MODID, "enchanted_golden_apple_slices"),
                new TooltippedItem(new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB).food(new Food.Builder().hunger(2).saturation(0.5F).fastToEat().build())).setRegistryName(SomeAssemblyRequired.MODID, "chopped_carrot"),
                new TooltippedItem(new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB).food(new Food.Builder().hunger(3).saturation(0.8F).fastToEat().build())).setRegistryName(SomeAssemblyRequired.MODID, "chopped_golden_carrot"),
                new TooltippedItem(new Item.Properties().group(SomeAssemblyRequired.CREATIVE_TAB).food(new Food.Builder().hunger(3).saturation(0.6F).fastToEat().build())).setRegistryName(SomeAssemblyRequired.MODID, "chopped_beetroot")

        );
    }
}
