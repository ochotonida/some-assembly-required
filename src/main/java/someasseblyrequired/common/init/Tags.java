package someasseblyrequired.common.init;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import someasseblyrequired.SomeAssemblyRequired;

public class Tags {

    public static final ITag.INamedTag<Item> BREADS = ItemTags.createOptional(new ResourceLocation(SomeAssemblyRequired.MODID, "breads"));

}
