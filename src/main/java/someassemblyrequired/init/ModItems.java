package someassemblyrequired.init;

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
import someassemblyrequired.item.EnchantedGoldenAppleSlicesItem;
import someassemblyrequired.item.SpreadItem;
import someassemblyrequired.item.sandwich.SandwichItem;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SomeAssemblyRequired.MODID);

    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(SomeAssemblyRequired.MODID) {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(BREAD_SLICE.get());
        }
    };

    // sandwich assembly tables
    public static final RegistryObject<Item> SANDWICHING_STATION = ITEMS.register("sandwiching_station", () -> createBlockItem(ModBlocks.SANDWICHING_STATION.get()));
    // foods
    public static final RegistryObject<Item> BREAD_SLICE = ITEMS.register("bread_slice", () -> createFoodItem(ModFoods.BREAD_SLICE));
    public static final RegistryObject<Item> TOASTED_BREAD_SLICE = ITEMS.register("toasted_bread_slice", () -> createFoodItem(ModFoods.TOASTED_BREAD_SLICE));
    public static final RegistryObject<Item> BURGER_BUN = ITEMS.register("burger_bun", () -> createFoodItem(ModFoods.BURGER_BUN));
    public static final RegistryObject<Item> BURGER_BUN_BOTTOM = ITEMS.register("burger_bun_bottom", () -> createFoodItem(ModFoods.BURGER_BUN_BOTTOM));
    public static final RegistryObject<Item> BURGER_BUN_TOP = ITEMS.register("burger_bun_top", () -> createFoodItem(ModFoods.BURGER_BUN_TOP));
    public static final RegistryObject<Item> APPLE_SLICES = ITEMS.register("apple_slices", () -> createFoodItem(ModFoods.APPLE_SLICES));
    public static final RegistryObject<Item> GOLDEN_APPLE_SLICES = ITEMS.register("golden_apple_slices", () -> new Item(new Item.Properties().tab(CREATIVE_TAB).food(ModFoods.GOLDEN_APPLE_SLICES).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> ENCHANTED_GOLDEN_APPLE_SLICES = ITEMS.register("enchanted_golden_apple_slices", () -> new EnchantedGoldenAppleSlicesItem(new Item.Properties().tab(CREATIVE_TAB).food(ModFoods.ENCHANTED_GOLDEN_APPLE_SLICES).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> CHOPPED_CARROT = ITEMS.register("chopped_carrot", () -> createFoodItem(ModFoods.CHOPPED_CARROT));
    public static final RegistryObject<Item> CHOPPED_GOLDEN_CARROT = ITEMS.register("chopped_golden_carrot", () -> createFoodItem(ModFoods.CHOPPED_GOLDEN_CARROT));
    public static final RegistryObject<Item> CHOPPED_BEETROOT = ITEMS.register("chopped_beetroot", () -> createFoodItem(ModFoods.CHOPPED_BEETROOT));
    public static final RegistryObject<Item> TOMATO_SLICES = ITEMS.register("tomato_slices", () -> createFoodItem(ModFoods.TOMATO_SLICES));

    // misc items
    public static final RegistryObject<SandwichItem> SANDWICH = ITEMS.register("sandwich", () -> new SandwichItem(ModBlocks.SANDWICH.get(), new Item.Properties().stacksTo(12).tab(CREATIVE_TAB).food(ModFoods.EMPTY)));
    public static final RegistryObject<Item> SPREAD = ITEMS.register("spread", () -> new SpreadItem(new Item.Properties()));

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

    public static void registerCompostables() {
        ComposterBlock.COMPOSTABLES.put(BREAD_SLICE.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(TOASTED_BREAD_SLICE.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(BURGER_BUN.get(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(BURGER_BUN_BOTTOM.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(BURGER_BUN_TOP.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(APPLE_SLICES.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(CHOPPED_CARROT.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(CHOPPED_BEETROOT.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(TOMATO_SLICES.get(), 0.3F);
    }
}
