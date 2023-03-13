package someassemblyrequired.integration.farmersdelight;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.integration.farmersdelight.ingredient.ConsumableItemBehavior;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.registry.ModItems;

public class FarmersDelightCompat {

    public static void setup() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(FarmersDelightCompat::onCommonSetup);
    }

    public static ItemStack createBLT() {
        return SandwichItem.makeSandwich(
                someassemblyrequired.init.ModItems.TOMATO_SLICES.get(),
                ModItems.CABBAGE_LEAF.get(),
                ModItems.COOKED_BACON.get()
        );
    }

    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(
                () -> ForgeRegistries.ITEMS.getValues()
                        .stream()
                        .filter(item -> item instanceof ConsumableItem)
                        .map(item -> (ConsumableItem) item)
                        .map(ConsumableItemBehavior::new)
                        .forEach(behavior -> Ingredients.addBehavior(behavior.item(), behavior))
        );
    }
}
