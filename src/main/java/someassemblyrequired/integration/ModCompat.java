package someassemblyrequired.integration;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import someassemblyrequired.integration.create.CreateCompat;
import someassemblyrequired.integration.farmersdelight.FarmersDelightCompat;

public class ModCompat {

    public static final String CREATE = "create";
    public static final String FARMERS_DELIGHT = "farmersdelight";

    public static void setup() {
        if (isCreateLoaded()) CreateCompat.setup();
        if (isFarmersDelightLoaded()) FarmersDelightCompat.setup();
    }

    public static void addCustomIngredientModels() {
        if (isFarmersDelightLoaded()) FarmersDelightCompat.addCustomIngredientModels();
    }

    public static void addSandwichSubtypes(NonNullList<ItemStack> items) {
        if (isFarmersDelightLoaded()) FarmersDelightCompat.addSandwichSubtypes(items);
    }

    public static boolean isCreateLoaded() {
        return isLoaded(CREATE);
    }

    public static boolean isFarmersDelightLoaded() {
        return isLoaded(FARMERS_DELIGHT);
    }

    private static boolean isLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
