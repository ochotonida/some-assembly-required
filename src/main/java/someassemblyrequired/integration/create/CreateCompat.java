package someassemblyrequired.integration.create;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.components.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.contraptions.components.deployer.DeployerRecipeSearchEvent;
import com.simibubi.create.content.contraptions.fluids.actors.FillingRecipe;
import com.simibubi.create.content.contraptions.fluids.potion.PotionFluidHandler;
import com.simibubi.create.content.contraptions.itemAssembly.SequencedAssemblyRecipeBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import someassemblyrequired.common.ingredient.Ingredients;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.item.sandwich.SandwichItemHandler;
import someassemblyrequired.common.util.Util;

import java.util.HashSet;

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

    public static HashSet<Recipe<?>> createSandwichDeployingRecipes() {
        NonNullList<ItemStack> sandwiches = NonNullList.create();
        ModItems.SANDWICH.get().fillItemCategory(ModItems.CREATIVE_TAB, sandwiches);
        HashSet<Recipe<?>> recipes = new HashSet<>();

        for (int i = 0; i < sandwiches.size(); i++) {
            ItemStack sandwich = sandwiches.get(i);
            SandwichItemHandler handler = SandwichItemHandler.get(sandwich).orElseThrow();

            SequencedAssemblyRecipeBuilder builder = new SequencedAssemblyRecipeBuilder(Util.id("example" + (i + 1)))
                    .require(Ingredient.of(handler.getItems().get(0)))
                    .transitionTo(ModItems.SANDWICH.get())
                    .loops(1)
                    .addOutput(sandwich, 1);

            for (int j = 1; j < handler.getItems().size(); j++) {
                ItemStack ingredient = handler.getItems().get(j);
                if (ingredient.is(Items.POTION)) {
                    Potion potion = PotionUtils.getPotion(ingredient);
                    builder.addStep(FillingRecipe::new, recipe -> recipe.require(PotionFluidHandler.potionIngredient(potion, 250)));
                } else if (ingredient.is(Items.HONEY_BOTTLE)) {
                    builder.addStep(FillingRecipe::new, recipe -> recipe.require(AllFluids.HONEY.get(), 250));
                } else {
                    builder.addStep(DeployerApplicationRecipe::new, recipe -> recipe.require(Ingredient.of(ingredient)));
                }
            }

            recipes.add(builder.build());
        }

        return recipes;
    }
}
