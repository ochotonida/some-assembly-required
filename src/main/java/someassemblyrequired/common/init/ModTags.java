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
    public static final Tag.Named<Item> BREAD_SLICES = createForgeTag("bread_slices");
    public static final Tag.Named<Item> BREAD_SLICES_WHEAT = createForgeTag("bread_slices/wheat");

    private static Tags.IOptionalNamedTag<Item> createForgeTag(String path) {
        return ItemTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID, path));
    }

    // mod item tags
    public static final Tag.Named<Item> SANDWICH_BREAD = createModTag("sandwich_bread");

    private static Tags.IOptionalNamedTag<Item> createModTag(String path) {
        return ItemTags.createOptional(Util.id(path));
    }
}
