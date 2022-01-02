package someassemblyrequired.common.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraftforge.versions.forge.ForgeVersion;
import someassemblyrequired.common.util.Util;

public class ModTags {

    // forge item tags
    public static final Tag.Named<Item> BREAD = createForgeTag("bread");
    public static final Tag.Named<Item> BREAD_WHEAT = createForgeTag("bread/wheat");
    public static final Tag.Named<Item> BREAD_SLICES = createForgeTag("bread_slices");
    public static final Tag.Named<Item> BREAD_SLICES_WHEAT = createForgeTag("bread_slices/wheat");
    public static final Tag.Named<Item> COOKED_BACON = createForgeTag("cooked_bacon");
    public static final Tag.Named<Item> CROPS = createForgeTag("crops");
    public static final Tag.Named<Item> CROPS_LETTUCE = createForgeTag("crops/lettuce");
    public static final Tag.Named<Item> CROPS_TOMATOES = createForgeTag("crops/tomato");
    public static final Tag.Named<Item> FRUITS = createForgeTag("fruits");
    public static final Tag.Named<Item> FRUITS_APPLE = createForgeTag("fruits/apple");
    public static final Tag.Named<Item> SALAD_INGREDIENTS = createForgeTag("salad_ingredients");
    public static final Tag.Named<Item> SALAD_INGREDIENTS_LETTUCE = createForgeTag("salad_ingredients/lettuce");
    public static final Tag.Named<Item> SEEDS = createForgeTag("seeds");
    public static final Tag.Named<Item> SEEDS_LETTUCE = createForgeTag("seeds/lettuce");
    public static final Tag.Named<Item> TORTILLAS = createForgeTag("tortillas");
    public static final Tag.Named<Item> VEGETABLES = createForgeTag("vegetables");
    public static final Tag.Named<Item> VEGETABLES_BEETROOT = createForgeTag("vegetables/beetroot");
    public static final Tag.Named<Item> VEGETABLES_CARROT = createForgeTag("vegetables/carrot");
    public static final Tag.Named<Item> VEGETABLES_LETTUCE = createForgeTag("vegetables/lettuce");
    public static final Tag.Named<Item> VEGETABLES_TOMATO = createForgeTag("vegetables/tomato");
    public static final Tag.Named<Item> VINEGAR_INGREDIENTS = createForgeTag("vinegar_ingredients");
    public static final Tag.Named<Item> VINEGAR_INGREDIENTS_APPLE = createForgeTag("vinegar_ingredients/apple");

    private static Tag.Named<Item> createForgeTag(String path) {
        return ItemTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID, path));
    }

    // mod item tags
    public static final Tag.Named<Item> BURNT_FOOD = createModTag("burnt_food");
    public static final Tag.Named<Item> SMALL_FOODS = createModTag("small_foods");
    public static final Tag.Named<Item> TOASTER_METALS = createModTag("toaster_metals");

    private static Tag.Named<Item> createModTag(String path) {
        return ItemTags.createOptional(Util.id(path));
    }
}
