package someassemblyrequired.integration.create;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.components.deployer.DeployerRecipeSearchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import someassemblyrequired.common.ingredient.Ingredients;

public class CreateCompat {

    public static void setup() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(CreateCompat::onCommonSetup);

        MinecraftForge.EVENT_BUS.addListener(CreateCompat::onDeployerRecipeSearch);
    }

    public static void onCommonSetup(FMLCommonSetupEvent event) {
        Ingredients.addBehavior(AllItems.BUILDERS_TEA.get(), new BuildersTeaBehavior());
    }

    public static void onDeployerRecipeSearch(DeployerRecipeSearchEvent event) {
        event.addRecipe(() -> SandwichDeployingRecipe.createRecipe(event.getInventory()), 150);
    }
}
