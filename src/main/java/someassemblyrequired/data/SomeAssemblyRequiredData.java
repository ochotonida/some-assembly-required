package someassemblyrequired.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.data.providers.*;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = SomeAssemblyRequired.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SomeAssemblyRequiredData {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        BlockTags blockTagsProvider = new BlockTags(packOutput, lookupProvider, helper);
        Ingredients ingredients = new Ingredients(packOutput);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new ItemTags(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), helper, ingredients));
        generator.addProvider(event.includeServer(), new Recipes(packOutput));
        generator.addProvider(event.includeServer(), new Advancements(packOutput, lookupProvider, helper));
        LootModifiers lootModifiers = new LootModifiers(packOutput);
        generator.addProvider(event.includeServer(), lootModifiers);
        generator.addProvider(event.includeServer(), new LootTables(packOutput, helper, lootModifiers));
        generator.addProvider(event.includeServer(), ingredients);

        if (event.includeServer()) {
            Recipes.registerAllProcessing(generator, packOutput);
        }

        BlockStates blockStates = new BlockStates(packOutput, helper);
        generator.addProvider(event.includeClient(), blockStates);
        generator.addProvider(event.includeClient(), new ItemModels(packOutput, blockStates.models().existingFileHelper));
        generator.addProvider(event.includeClient(), new SoundDefinitions(packOutput, helper));
    }
}
