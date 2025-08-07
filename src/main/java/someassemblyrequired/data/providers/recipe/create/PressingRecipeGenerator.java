package someassemblyrequired.data.providers.recipe.create;

import com.simibubi.create.api.data.recipe.PressingRecipeGen;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.integration.farmersdelight.FarmersDelightCompat;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.registry.ModDataComponents;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class PressingRecipeGenerator extends PressingRecipeGen {

    public PressingRecipeGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, SomeAssemblyRequired.MOD_ID);

        stompSandwich(ModItems.HAMBURGER.get(), FarmersDelightCompat.createBurger());
        stompSandwich(ModItems.BACON_SANDWICH.get(), FarmersDelightCompat.createBLT());
        stompSandwich(ModItems.EGG_SANDWICH.get(), SandwichItem.makeSandwich(ModItems.FRIED_EGG.get(), ModItems.FRIED_EGG.get()));
        stompSandwich(ModItems.CHICKEN_SANDWICH.get(), SandwichItem.makeSandwich(someassemblyrequired.registry.ModItems.CHOPPED_CARROT.get(), ModItems.COOKED_CHICKEN_CUTS.get(), ModItems.CABBAGE_LEAF.get()));
    }

    private void stompSandwich(Item result, ItemStack sandwich) {
        ResourceLocation id = SomeAssemblyRequired.id(BuiltInRegistries.ITEM.getKey(result).getPath());
        create(id, builder -> {
                    builder.output(result);
                    return builder.whenModLoaded(ModCompat.FARMERSDELIGHT)
                            .require(DataComponentIngredient.of(false,
                                    ModDataComponents.SANDWICH_CONTENTS,
                                    SandwichContents.get(sandwich),
                                    sandwich.getItem()
                            ));
                }
        );
    }

    @Override
    protected GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name, UnaryOperator<StandardProcessingRecipe.Builder<PressingRecipe>> transform) {
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(getBuilder(name.get()))
                        .whenModLoaded(ModCompat.CREATE)
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }
}
