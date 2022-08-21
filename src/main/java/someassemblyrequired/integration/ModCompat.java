package someassemblyrequired.integration;

import net.minecraftforge.fml.ModList;
import someassemblyrequired.integration.create.CreateCompat;
import someassemblyrequired.integration.diet.DietCompat;
import someassemblyrequired.integration.farmersdelight.FarmersDelightCompat;

public class ModCompat {

    public static final String FARMERSDELIGHT = "farmersdelight";
    public static final String CREATE = "create";
    public static final String DIET = "diet";
    public static final String JEI = "jei";

    public static void setup() {
        if (isFarmersDelightLoaded()) FarmersDelightCompat.setup();
        if (isCreateLoaded()) CreateCompat.setup();
        if (isDietLoaded()) DietCompat.setup();
    }

    public static boolean isFarmersDelightLoaded() {
        return isLoaded(FARMERSDELIGHT);
    }

    public static boolean isCreateLoaded() {
        return isLoaded(CREATE);
    }

    public static boolean isDietLoaded() {
        return isLoaded(DIET);
    }

    public static boolean isJEILoaded() {
        return isLoaded(JEI);
    }

    private static boolean isLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
