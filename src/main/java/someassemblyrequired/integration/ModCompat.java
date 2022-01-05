package someassemblyrequired.integration;

import net.minecraftforge.fml.ModList;
import someassemblyrequired.integration.create.CreateCompat;

public class ModCompat {

    public static final String CREATE = "create";
    public static final String FARMERS_DELIGHT = "farmersdelight";

    public static void setup() {
        if (isCreateLoaded()) {
            CreateCompat.setup();
        }
    }

    public static boolean isCreateLoaded() {
        return isLoaded(CREATE);
    }

    private static boolean isLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
