package someassemblyrequired.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import someassemblyrequired.SomeAssemblyRequired;

public class ModTags {

    // c item tags
    public static final TagKey<Item> BREAD_SLICES = itemTag("c", "foods/bread_slice");
    // TODO inconsistent dough tags between farmer's delight/create
    public static final TagKey<Item> DOUGH = itemTag("c", "foods/dough");
    public static final TagKey<Item> DOUGHS = itemTag("c", "doughs");

    // mod item tags
    public static final TagKey<Item> SANDWICH_BREAD = itemTag("sandwich_bread");
    public static final TagKey<Item> BURGER_BUNS = itemTag("burger_buns");
    public static final TagKey<Item> SPECIAL_SANDWICH_FILLINGS = itemTag("special_sandwich_fillings");

    // mod block tags
    public static final TagKey<Block> SANDWICHING_STATIONS = blockTag("sandwiching_stations");

    public static TagKey<Item> itemTag(String path) {
        return itemTag(SomeAssemblyRequired.MOD_ID, path);
    }

    public static TagKey<Item> itemTag(String modId, String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, path));
    }

    public static TagKey<Block> blockTag(String path) {
        return TagKey.create(Registries.BLOCK, SomeAssemblyRequired.id(path));
    }
}
