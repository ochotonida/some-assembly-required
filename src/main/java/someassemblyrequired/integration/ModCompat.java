package someassemblyrequired.integration;

import net.minecraftforge.fml.ModList;
import someassemblyrequired.integration.create.CreateCompat;
import someassemblyrequired.integration.diet.DietCompat;

public class ModCompat {

    public static final String CREATE = "create";
    public static final String DIET = "diet";

    public static void setup() {
        if (isCreateLoaded()) CreateCompat.setup();
        if (isDietLoaded()) DietCompat.setup();
    }

    public static boolean isCreateLoaded() {
        return isLoaded(CREATE);
    }

    public static boolean isDietLoaded() {
        return isLoaded(DIET);
    }

    private static boolean isLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
