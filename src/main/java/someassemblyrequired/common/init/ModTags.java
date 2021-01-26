package someassemblyrequired.common.init;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.versions.forge.ForgeVersion;
import someassemblyrequired.common.util.Util;

public class ModTags {

    // forge item tags
    public static final ITag.INamedTag<Item> BREAD = createForgeTag("bread");
    public static final ITag.INamedTag<Item> BREAD_WHEAT = createForgeTag("bread/wheat");
    public static final ITag.INamedTag<Item> BREAD_SLICES = createForgeTag("bread_slices");
    public static final ITag.INamedTag<Item> BREAD_SLICES_WHEAT = createForgeTag("bread_slices/wheat");
    public static final ITag.INamedTag<Item> COOKED_BACON = createForgeTag("cooked_bacon");
    public static final ITag.INamedTag<Item> COOKED_PORK = createForgeTag("cooked_pork");
    public static final ITag.INamedTag<Item> CROPS = createForgeTag("crops");
    public static final ITag.INamedTag<Item> CROPS_LETTUCE = createForgeTag("crops/lettuce");
    public static final ITag.INamedTag<Item> CROPS_TOMATOES = createForgeTag("crops/tomato");
    public static final ITag.INamedTag<Item> FRUITS = createForgeTag("fruits");
    public static final ITag.INamedTag<Item> FRUITS_APPLE = createForgeTag("fruits/apple");
    public static final ITag.INamedTag<Item> RAW_BACON = createForgeTag("raw_bacon");
    public static final ITag.INamedTag<Item> RAW_PORK = createForgeTag("raw_pork");
    public static final ITag.INamedTag<Item> SALAD_INGREDIENTS = createForgeTag("salad_ingredients");
    public static final ITag.INamedTag<Item> SALAD_INGREDIENTS_LETTUCE = createForgeTag("salad_ingredients/lettuce");
    public static final ITag.INamedTag<Item> SEEDS = createForgeTag("seeds");
    public static final ITag.INamedTag<Item> SEEDS_LETTUCE = createForgeTag("seeds/lettuce");
    public static final ITag.INamedTag<Item> SEEDS_TOMATO = createForgeTag("seeds/tomato");
    public static final ITag.INamedTag<Item> TOOLS = createForgeTag("tools");
    public static final ITag.INamedTag<Item> TOOLS_KNIVES = createForgeTag("tools/knives");
    public static final ITag.INamedTag<Item> TORTILLAS = createForgeTag("tortillas");
    public static final ITag.INamedTag<Item> VEGETABLES = createForgeTag("vegetables");
    public static final ITag.INamedTag<Item> VEGETABLES_BEETROOT = createForgeTag("vegetables/beetroot");
    public static final ITag.INamedTag<Item> VEGETABLES_CARROT = createForgeTag("vegetables/carrot");
    public static final ITag.INamedTag<Item> VEGETABLES_LETTUCE = createForgeTag("vegetables/lettuce");
    public static final ITag.INamedTag<Item> VEGETABLES_TOMATO = createForgeTag("vegetables/tomato");
    public static final ITag.INamedTag<Item> VINEGAR_INGREDIENTS = createForgeTag("vinegar_ingredients");
    public static final ITag.INamedTag<Item> VINEGAR_INGREDIENTS_APPLE = createForgeTag("vinegar_ingredients/apple");

    private static ITag.INamedTag<Item> createForgeTag(String path) {
        return ItemTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID, path));
    }

    // mod item tags
    public static final ITag.INamedTag<Item> BURNT_FOOD = createModTag("burnt_food");
    public static final ITag.INamedTag<Item> SANDWICH_BREADS = createModTag("sandwich_breads");
    public static final ITag.INamedTag<Item> SMALL_FOODS = createModTag("small_foods");
    public static final ITag.INamedTag<Item> TOASTER_METALS = createModTag("toaster_metals");

    private static ITag.INamedTag<Item> createModTag(String path) {
        return ItemTags.createOptional(Util.prefix(path));
    }
}
