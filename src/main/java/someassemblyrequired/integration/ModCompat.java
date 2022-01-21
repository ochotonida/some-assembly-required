package someassemblyrequired.integration;

import net.minecraftforge.fml.ModList;
import someassemblyrequired.integration.create.CreateCompat;
import someassemblyrequired.integration.diet.DietCompat;
import someassemblyrequired.integration.farmersdelight.FarmersDelightCompat;

public class ModCompat {

    public static final String CREATE = "create";
    public static final String DIET = "diet";
    public static final String FARMERS_DELIGHT = "farmersdelight";

    public static void setup() {
        if (isCreateLoaded()) CreateCompat.setup();
        if (isDietLoaded()) DietCompat.setup();
        if (isFarmersDelightLoaded()) FarmersDelightCompat.setup();
    }

    public static boolean isCreateLoaded() {
        return isLoaded(CREATE);
    }

    public static boolean isDietLoaded() {
        return isLoaded(DIET);
    }

    public static boolean isFarmersDelightLoaded() {
        return isLoaded(FARMERS_DELIGHT);
    }

    private static boolean isLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
