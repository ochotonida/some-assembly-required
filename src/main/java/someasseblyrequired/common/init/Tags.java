package someasseblyrequired.common.init;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.versions.forge.ForgeVersion;
import someasseblyrequired.SomeAssemblyRequired;

public class Tags {

    public static final ITag.INamedTag<Item> KNIVES = ItemTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID, "tools/knives"));
    public static final ITag.INamedTag<Item> BREAD_LOAVES = ItemTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID, "bread"));
    public static final ITag.INamedTag<Item> TOMATOES = ItemTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID, "crops/tomato"));
    public static final ITag.INamedTag<Item> LETTUCE = ItemTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID, "crops/lettuce"));

    public static final ITag.INamedTag<Item> BREAD = ItemTags.createOptional(new ResourceLocation(SomeAssemblyRequired.MODID, "sandwich_breads"));
    public static final ITag.INamedTag<Item> TOASTER_METALS = ItemTags.createOptional(new ResourceLocation(SomeAssemblyRequired.MODID, "toaster_metals"));
    public static final ITag.INamedTag<Item> SMALL_FOODS = ItemTags.createOptional(new ResourceLocation(SomeAssemblyRequired.MODID, "small_foods"));
}
