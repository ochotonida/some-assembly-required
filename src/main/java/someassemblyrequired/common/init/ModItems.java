package someassemblyrequired.common.init;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.item.DrinkableBottleItem;
import someassemblyrequired.common.item.EnchantedGoldenAppleSlicesItem;
import someassemblyrequired.common.item.SpreadItem;
import someassemblyrequired.common.item.sandwich.SandwichItem;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class ModItems {

    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, SomeAssemblyRequired.MODID);

    private static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(SomeAssemblyRequired.MODID) {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(BREAD_SLICE.get());
        }
    };

    // sandwich assembly tables
    public static final RegistryObject<Item> OAK_SANDWICH_ASSEMBLY_TABLE = REGISTRY.register("oak_sandwich_assembly_table", () -> createBlockItem(ModBlocks.OAK_SANDWICH_ASSEMBLY_TABLE.get()));
    public static final RegistryObject<Item> SPRUCE_SANDWICH_ASSEMBLY_TABLE = REGISTRY.register("spruce_sandwich_assembly_table", () -> createBlockItem(ModBlocks.SPRUCE_SANDWICH_ASSEMBLY_TABLE.get()));
    public static final RegistryObject<Item> BIRCH_SANDWICH_ASSEMBLY_TABLE = REGISTRY.register("birch_sandwich_assembly_table", () -> createBlockItem(ModBlocks.BIRCH_SANDWICH_ASSEMBLY_TABLE.get()));
    public static final RegistryObject<Item> JUNGLE_SANDWICH_ASSEMBLY_TABLE = REGISTRY.register("jungle_sandwich_assembly_table", () -> createBlockItem(ModBlocks.JUNGLE_SANDWICH_ASSEMBLY_TABLE.get()));
    public static final RegistryObject<Item> ACACIA_SANDWICH_ASSEMBLY_TABLE = REGISTRY.register("acacia_sandwich_assembly_table", () -> createBlockItem(ModBlocks.ACACIA_SANDWICH_ASSEMBLY_TABLE.get()));
    public static final RegistryObject<Item> DARK_OAK_SANDWICH_ASSEMBLY_TABLE = REGISTRY.register("dark_oak_sandwich_assembly_table", () -> createBlockItem(ModBlocks.DARK_OAK_SANDWICH_ASSEMBLY_TABLE.get()));
    public static final RegistryObject<Item> CRIMSON_SANDWICH_ASSEMBLY_TABLE = REGISTRY.register("crimson_sandwich_assembly_table", () -> createBlockItem(ModBlocks.CRIMSON_SANDWICH_ASSEMBLY_TABLE.get()));
    public static final RegistryObject<Item> WARPED_SANDWICH_ASSEMBLY_TABLE = REGISTRY.register("warped_sandwich_assembly_table", () -> createBlockItem(ModBlocks.WARPED_SANDWICH_ASSEMBLY_TABLE.get()));

    // toasters
    public static final RegistryObject<Item> REDSTONE_TOASTER = REGISTRY.register("redstone_toaster", () -> createBlockItem(ModBlocks.REDSTONE_TOASTER.get()));
    public static final RegistryObject<Item> STICKY_REDSTONE_TOASTER = REGISTRY.register("sticky_redstone_toaster", () -> createBlockItem(ModBlocks.STICKY_REDSTONE_TOASTER.get()));

    // foods
    public static final RegistryObject<Item> BREAD_SLICE = REGISTRY.register("bread_slice", () -> createFoodItem(ModFoods.BREAD_SLICE));
    public static final RegistryObject<Item> TOASTED_BREAD_SLICE = REGISTRY.register("toasted_bread_slice", () -> createFoodItem(ModFoods.TOASTED_BREAD_SLICE));
    public static final RegistryObject<Item> CHARRED_BREAD_SLICE = REGISTRY.register("charred_bread_slice", () -> createFoodItem(ModFoods.CHARRED_FOOD));
    public static final RegistryObject<Item> CHARRED_MORSEL = REGISTRY.register("charred_morsel", () -> createFoodItem(ModFoods.CHARRED_MORSEL));
    public static final RegistryObject<Item> CHARRED_FOOD = REGISTRY.register("charred_food", () -> createFoodItem(ModFoods.CHARRED_FOOD));
    public static final RegistryObject<Item> APPLE_SLICES = REGISTRY.register("apple_slices", () -> createFoodItem(ModFoods.APPLE_SLICES));
    public static final RegistryObject<Item> GOLDEN_APPLE_SLICES = REGISTRY.register("golden_apple_slices", () -> new Item(new Item.Properties().tab(CREATIVE_TAB).food(ModFoods.GOLDEN_APPLE_SLICES).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> ENCHANTED_GOLDEN_APPLE_SLICES = REGISTRY.register("enchanted_golden_apple_slices", () -> new EnchantedGoldenAppleSlicesItem(new Item.Properties().tab(CREATIVE_TAB).food(ModFoods.ENCHANTED_GOLDEN_APPLE_SLICES).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> CHOPPED_CARROT = REGISTRY.register("chopped_carrot", () -> createFoodItem(ModFoods.CHOPPED_CARROT));
    public static final RegistryObject<Item> CHOPPED_GOLDEN_CARROT = REGISTRY.register("chopped_golden_carrot", () -> createFoodItem(ModFoods.CHOPPED_GOLDEN_CARROT));
    public static final RegistryObject<Item> CHOPPED_BEETROOT = REGISTRY.register("chopped_beetroot", () -> createFoodItem(ModFoods.CHOPPED_BEETROOT));
    public static final RegistryObject<Item> TOASTED_CRIMSON_FUNGUS = REGISTRY.register("toasted_crimson_fungus", () -> createFoodItem(ModFoods.TOASTED_CRIMSON_FUNGUS));
    public static final RegistryObject<Item> SLICED_TOASTED_CRIMSON_FUNGUS = REGISTRY.register("sliced_toasted_crimson_fungus", () -> createFoodItem(ModFoods.SLICED_TOASTED_CRIMSON_FUNGUS));
    public static final RegistryObject<Item> TOASTED_WARPED_FUNGUS = REGISTRY.register("toasted_warped_fungus", () -> createFoodItem(ModFoods.TOASTED_WARPED_FUNGUS));
    public static final RegistryObject<Item> SLICED_TOASTED_WARPED_FUNGUS = REGISTRY.register("sliced_toasted_warped_fungus", () -> createFoodItem(ModFoods.SLICED_TOASTED_WARPED_FUNGUS));
    public static final RegistryObject<Item> TOMATO_SLICES = REGISTRY.register("tomato_slices", () -> createFoodItem(ModFoods.TOMATO_SLICES));

    // spreadables
    public static final RegistryObject<Item> MAYONNAISE_BOTTLE = REGISTRY.register("mayonnaise_bottle", () -> createSpreadItem(ModFoods.MAYONNAISE));
    public static final RegistryObject<Item> KETCHUP_BOTTLE = REGISTRY.register("ketchup_bottle", () -> createSpreadItem(ModFoods.KETCHUP));
    public static final RegistryObject<Item> SWEET_BERRY_JAM_BOTTLE = REGISTRY.register("sweet_berry_jam_bottle", () -> createSpreadItem(ModFoods.SWEET_BERRY_JAM));

    // misc items
    public static final RegistryObject<SandwichItem> SANDWICH = REGISTRY.register("sandwich", () -> new SandwichItem(ModBlocks.SANDWICH.get(), new Item.Properties().stacksTo(12).tab(CREATIVE_TAB).food(ModFoods.SANDWICH)));
    public static final RegistryObject<Item> SPREAD = REGISTRY.register("spread", () -> new SpreadItem(new Item.Properties()));

    private static Item createBlockItem(Block block) {
        return new BlockItem(block, new Item.Properties().tab(CREATIVE_TAB));
    }

    private static Item createFuelBlockItem(Block block) {
        return new BlockItem(block, new Item.Properties().tab(CREATIVE_TAB)) {

            @Override
            public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
                return 300;
            }
        };
    }

    private static Item createFoodItem(FoodProperties food) {
        return new Item(new Item.Properties().tab(CREATIVE_TAB).food(food));
    }

    private static Item createSpreadItem(FoodProperties food) {
        return new DrinkableBottleItem(new Item.Properties().tab(CREATIVE_TAB).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(food), SoundEvents.HONEY_DRINK);
    }

    public static void registerCompostables() {
        // 30%
        ComposterBlock.COMPOSTABLES.put(BREAD_SLICE.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(TOASTED_BREAD_SLICE.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(CHARRED_BREAD_SLICE.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(CHARRED_FOOD.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(CHARRED_MORSEL.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(APPLE_SLICES.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(CHOPPED_CARROT.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(CHOPPED_BEETROOT.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(SLICED_TOASTED_CRIMSON_FUNGUS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(SLICED_TOASTED_CRIMSON_FUNGUS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(TOMATO_SLICES.get(), 0.3F);

        // 65%
        ComposterBlock.COMPOSTABLES.put(TOASTED_CRIMSON_FUNGUS.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(TOASTED_WARPED_FUNGUS.get(), 0.65F);
    }
}
