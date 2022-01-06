package someassemblyrequired.common.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.versions.forge.ForgeVersion;
import someassemblyrequired.common.util.Util;

public class ModTags {

    // forge item tags
    public static final Tags.IOptionalNamedTag<Item> BREAD_SLICES = createForgeTag("bread_slices");
    public static final Tag.Named<Item> BREAD_SLICES_WHEAT = createForgeTag("bread_slices/wheat");
    public static final Tag.Named<Item> COOKED_BACON = createForgeTag("cooked_bacon");
    public static final Tag.Named<Item> FRUITS = createForgeTag("fruits");
    public static final Tag.Named<Item> FRUITS_APPLE = createForgeTag("fruits/apple");
    public static final Tag.Named<Item> SALAD_INGREDIENTS = createForgeTag("salad_ingredients");
    public static final Tag.Named<Item> VEGETABLES = createForgeTag("vegetables");
    public static final Tag.Named<Item> VEGETABLES_BEETROOT = createForgeTag("vegetables/beetroot");
    public static final Tag.Named<Item> VEGETABLES_CARROT = createForgeTag("vegetables/carrot");
    public static final Tag.Named<Item> VEGETABLES_TOMATO = createForgeTag("vegetables/tomato");

    private static Tags.IOptionalNamedTag<Item> createForgeTag(String path) {
        return ItemTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID, path));
    }

    // mod item tags
    public static final Tag.Named<Item> BURNT_FOOD = createModTag("burnt_food");
    public static final Tag.Named<Item> SMALL_FOODS = createModTag("small_foods");
    public static final Tag.Named<Item> TOASTER_METALS = createModTag("toaster_metals");

    private static Tags.IOptionalNamedTag<Item> createModTag(String path) {
        return ItemTags.createOptional(Util.id(path));
    }
}
