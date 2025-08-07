package someassemblyrequired.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import someassemblyrequired.data.providers.*;

import java.util.concurrent.CompletableFuture;

public class SomeAssemblyRequiredData {

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();

        BlockTags blockTagsProvider = new BlockTags(packOutput, registries, helper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new ItemTags(packOutput, registries, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(event.includeServer(), new Recipes(packOutput, registries));
        generator.addProvider(event.includeServer(), new Advancements(packOutput, registries, helper));
        LootModifiers lootModifiers = new LootModifiers(packOutput, registries);
        generator.addProvider(event.includeServer(), lootModifiers);
        generator.addProvider(event.includeServer(), new LootTables(packOutput, helper, lootModifiers, registries));
        generator.addProvider(event.includeServer(), new Ingredients(packOutput));
        generator.addProvider(event.includeServer(), new DataMaps(packOutput, registries));

        if (event.includeServer()) {
            Recipes.registerAllProcessing(generator, packOutput, registries);
        }

        BlockStates blockStates = new BlockStates(packOutput, helper);
        generator.addProvider(event.includeClient(), blockStates);
        generator.addProvider(event.includeClient(), new ItemModels(packOutput, blockStates.models().existingFileHelper));
        generator.addProvider(event.includeClient(), new SoundDefinitions(packOutput, helper));
    }
}
