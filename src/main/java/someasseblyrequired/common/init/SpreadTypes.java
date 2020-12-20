package someasseblyrequired.common.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import someasseblyrequired.SomeAssemblyRequired;
import someasseblyrequired.common.item.spreadtype.SpreadType;
import someasseblyrequired.common.item.spreadtype.PotionSpreadType;
import someasseblyrequired.common.item.spreadtype.SimpleSpreadType;

import javax.annotation.Nullable;

public class SpreadTypes {

    public static IForgeRegistry<SpreadType> SPREAD_TYPE_REGISTRY;

    public static void createRegistry() {
        SPREAD_TYPE_REGISTRY = new RegistryBuilder<SpreadType>().setType(SpreadType.class).setName(new ResourceLocation(SomeAssemblyRequired.MODID, "spread_type")).setDefaultKey(new ResourceLocation(SomeAssemblyRequired.MODID, "potion")).onValidate((registry, registryManager, i, location, spreadType0) -> {
            registry.forEach((spreadType1) -> {
                if (spreadType0 != spreadType1 && spreadType0.getIngredient() == spreadType1.getIngredient()) {
                    throw new IllegalStateException(String.format("Multiple spreadtypes registered for item %s: %s, %s", spreadType0.getIngredient().getRegistryName(), spreadType0.getRegistryName(), spreadType1.getRegistryName()));
                }
            });
        }).create();
    }

    @Nullable
    public static SpreadType findSpreadType(Item ingredient) {
        for (SpreadType spreadType : SPREAD_TYPE_REGISTRY.getValues()) {
            if (spreadType.getIngredient() == ingredient) {
                return spreadType;
            }
        }
        return null;
    }

    public static boolean hasSpreadType(Item ingredient) {
        return findSpreadType(ingredient) != null;
    }

    public static void register(IForgeRegistry<SpreadType> registry) {
        registry.registerAll(
                new PotionSpreadType().setRegistryName(SomeAssemblyRequired.MODID, "potion"),
                new SimpleSpreadType(Items.MUSHROOM_STEW, Items.BOWL, 0xAD7451).setRegistryName(SomeAssemblyRequired.MODID, "mushroom_stew"),
                new SimpleSpreadType(Items.RABBIT_STEW, Items.BOWL, 0xBF7234).setRegistryName(SomeAssemblyRequired.MODID, "rabbit_stew"),
                new SimpleSpreadType(Items.BEETROOT_SOUP, Items.BOWL, 0x8C0023).setRegistryName(SomeAssemblyRequired.MODID, "beetroot_soup"),
                new SimpleSpreadType(Items.HONEY_BOTTLE, Items.GLASS_BOTTLE, 0xF08A1D).setRegistryName(SomeAssemblyRequired.MODID, "honey_bottle"),
                new SimpleSpreadType(Items.SUSPICIOUS_STEW, Items.BOWL, 0xC3C45E).setRegistryName(SomeAssemblyRequired.MODID, "suspicious_stew")
        );
    }
}
