package someassemblyrequired.integration.create;

import com.simibubi.create.AllFluids;
import com.simibubi.create.compat.jei.CreateJEI;
import com.simibubi.create.content.kinetics.deployer.DeployerRecipeSearchEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.fluids.FluidStack;
import someassemblyrequired.integration.create.itemattribute.ModItemAttributeTypes;
import someassemblyrequired.integration.create.recipe.SandwichFluidSpoutingRecipe;
import someassemblyrequired.integration.create.recipe.deployer.SandwichDeployingRecipe;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.registry.ModRecipeTypes;

import java.util.List;
import java.util.function.Consumer;

public class CreateCompat {

    public static void setup(IEventBus modEventBus) {
        ModItemAttributeTypes.ITEM_ATTRIBUTE_TYPES.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(CreateCompat::onDeployerRecipeSearch);
    }

    public static void onDeployerRecipeSearch(DeployerRecipeSearchEvent event) {
        event.addRecipe(() -> SandwichDeployingRecipe.createRecipe(event.getInventory()), 150);
    }

    public static void populateJEI(Consumer<ItemStack> items) {
        CreateJEI.getTypedRecipesExcluding(ModRecipeTypes.SANDWICH_SPOUTING.get(), recipe -> recipe.value().getSerializer() != ModRecipeTypes.SANDWICH_FLUID_SPOUTING_SERIALIZER.get())
                .stream()
                .map(recipe -> (SandwichFluidSpoutingRecipe) recipe.value())
                .map(recipe -> {
                    ItemStack filling = recipe.assemble(FluidStack.EMPTY);
                    if (List.of(
                            Items.HONEY_BOTTLE,
                            AllFluids.CHOCOLATE.getBucket().orElseThrow()
                    ).contains(filling.getItem())) {
                        return SandwichItem.makeToastSandwich(filling);
                    }
                    return SandwichItem.makeSandwich(filling);
                })
                .forEach(items);
    }
}
