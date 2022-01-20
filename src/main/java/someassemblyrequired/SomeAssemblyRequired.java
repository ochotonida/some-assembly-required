package someassemblyrequired;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import someassemblyrequired.common.config.ModConfig;
import someassemblyrequired.common.ingredient.IngredientPropertiesManager;
import someassemblyrequired.common.ingredient.Ingredients;
import someassemblyrequired.common.init.*;
import someassemblyrequired.common.network.NetworkHandler;
import someassemblyrequired.common.recipe.PotionToItemStackBrewingRecipe;
import someassemblyrequired.integration.ModCompat;

@Mod(SomeAssemblyRequired.MODID)
public class SomeAssemblyRequired {

    public static final String MODID = "some_assembly_required";

    public static final Logger LOGGER = LogManager.getLogger();

    public SomeAssemblyRequired() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> SomeAssemblyRequiredClient::new);

        ModCompat.setup();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.REGISTRY.register(modEventBus);
        ModBlocks.REGISTRY.register(modEventBus);
        ModBlockEntityTypes.REGISTRY.register(modEventBus);
        ModSoundEvents.REGISTRY.register(modEventBus);

        modEventBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.addListener(IngredientPropertiesManager::onAddReloadListener);
        MinecraftForge.EVENT_BUS.addListener(IngredientPropertiesManager::onDataPackReload);

        ModAdvancementTriggers.register();
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModConfig.registerServer();
            Ingredients.addBehaviors();
            NetworkHandler.register();
            registerBrewingRecipes();
            ModItems.registerCompostables();
        });
    }

    private static void registerBrewingRecipes() {
        BrewingRecipeRegistry.addRecipe(
                new PotionToItemStackBrewingRecipe(
                        Potions.THICK,
                        Ingredient.of(Items.EGG),
                        new ItemStack(ModItems.MAYONNAISE_BOTTLE.get())
                )
        );
    }
}
