package someassemblyrequired.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.data.recipe.create.ProcessingRecipeGenerator;
import someassemblyrequired.integration.ModCompat;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = SomeAssemblyRequired.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();


        generator.addProvider(event.includeServer(), new ItemTags(generator, helper));
        generator.addProvider(event.includeServer(), new BlockTags(generator, helper));
        generator.addProvider(event.includeServer(), new Recipes(generator));
        generator.addProvider(event.includeServer(), new Advancements(generator, helper));
        LootModifiers lootModifiers = new LootModifiers(generator);
        generator.addProvider(event.includeServer(), lootModifiers);
        generator.addProvider(event.includeServer(), new LootTables(generator, helper, lootModifiers));
        generator.addProvider(event.includeServer(), new Ingredients(generator));
        if (ModCompat.isCreateLoaded()) {
            ProcessingRecipeGenerator.registerAll(event.includeServer(), generator);
        }

        BlockStates blockStates = new BlockStates(generator, helper);
        generator.addProvider(event.includeClient(), blockStates);
        generator.addProvider(event.includeClient(), new ItemModels(generator, blockStates.models().existingFileHelper));
        generator.addProvider(event.includeClient(), new SoundDefinitions(generator, helper));
    }
}
