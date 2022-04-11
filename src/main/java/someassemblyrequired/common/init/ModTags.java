package someassemblyrequired.common.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.versions.forge.ForgeVersion;
import someassemblyrequired.common.util.Util;

public class ModTags {

    // forge item tags
    public static final TagKey<Item> BREAD_SLICES = createForgeTag("bread_slices");
    public static final TagKey<Item> BREAD_SLICES_WHEAT = createForgeTag("bread_slices/wheat");

    private static TagKey<Item> createForgeTag(String path) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(ForgeVersion.MOD_ID, path));
    }

    // mod item tags
    public static final TagKey<Item> SANDWICH_BREAD = createModTag("sandwich_bread");

    private static TagKey<Item> createModTag(String path) {
        return TagKey.create(Registry.ITEM_REGISTRY, Util.id(path));
    }
}
