package someassemblyrequired.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.data.recipe.create.ProcessingRecipeGenerator;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = SomeAssemblyRequired.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        if (event.includeServer()) {
            generator.addProvider(new ItemTags(generator, helper));
            generator.addProvider(new Recipes(generator));
            generator.addProvider(new Advancements(generator, helper));
            generator.addProvider(new LootTables(generator));
            generator.addProvider(new SandwichIngredients(generator));
            ProcessingRecipeGenerator.registerAll(generator);
        }
        if (event.includeClient()) {
            BlockStates blockStates = new BlockStates(generator, helper);
            generator.addProvider(blockStates);
            generator.addProvider(new ItemModels(generator, blockStates.models().existingFileHelper));
        }
    }
}
