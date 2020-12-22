package someasseblyrequired.common.init;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.versions.forge.ForgeVersion;

public class Tags {

    public static final ITag.INamedTag<Item> BREADS = ItemTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID, "bread"));
    public static final ITag.INamedTag<Item> KNIVES = ItemTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID, "tools/knives"));
}
