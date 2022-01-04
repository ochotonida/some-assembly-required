package someassemblyrequired.integration.create;

import com.simibubi.create.content.contraptions.components.deployer.DeployerRecipeSearchEvent;
import net.minecraftforge.common.MinecraftForge;

public class CreateCompat {

    public static void setup() {
        MinecraftForge.EVENT_BUS.addListener(CreateCompat::onDeployerRecipeSearch);
    }

    public static void onDeployerRecipeSearch(DeployerRecipeSearchEvent event) {
        event.addRecipe(() -> SandwichDeployingRecipe.createRecipe(event.getInventory()), 150);
    }
}
