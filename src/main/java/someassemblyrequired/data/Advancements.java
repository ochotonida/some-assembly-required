package someassemblyrequired.data;

import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.HusbandryAdvancements;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModAdvancementTriggers;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;
import someassemblyrequired.common.item.sandwich.SandwichBuilder;
import someassemblyrequired.common.util.Util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public class Advancements extends AdvancementProvider {

    private final Path PATH;

    public Advancements(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, helper);
        PATH = generator.getOutputFolder();
    }

    @Override
    public void run(HashCache cache) {
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (advancement) -> {
            if (!set.add(advancement.getId())) {
                throw new IllegalStateException("Duplicate advancement " + advancement.getId());
            } else {
                Path path1 = getPath(PATH, advancement);

                try {
                    DataProvider.save((new GsonBuilder()).setPrettyPrinting().create(), cache, advancement.deconstruct().serializeToJson(), path1);
                } catch (IOException ioexception) {
                    SomeAssemblyRequired.LOGGER.error("Couldn't save advancement {}", path1, ioexception);
                }
            }
        };

        new SomeAssemblyRequiredAdvancements().accept(consumer);
    }

    private static Path getPath(Path pathIn, Advancement advancementIn) {
        return pathIn.resolve("data/" + advancementIn.getId().getNamespace() + "/advancements/" + advancementIn.getId().getPath() + ".json");
    }

    public static class SomeAssemblyRequiredAdvancements implements Consumer<Consumer<Advancement>> {

        private Advancement root;

        public void setRoot(Advancement root) {
            this.root = root;
        }

        @Override
        @SuppressWarnings("unused")
        public void accept(Consumer<Advancement> consumer) {
            new HusbandryAdvancements().accept(advancement -> {
                if (advancement.getId().equals(new ResourceLocation("minecraft:husbandry/plant_seed"))) {
                    setRoot(advancement);
                }
            });

            Advancement obtainBreadSlice = addAdvancement(
                    consumer,
                    root,
                    new ItemStack(ModItems.BREAD_SLICE.get()),
                    InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModTags.BREAD_SLICES).build()),
                    "obtain_bread_slice",
                    false
            );

            Advancement obtainSandwich = addAdvancement(
                    consumer,
                    obtainBreadSlice,
                    SandwichBuilder.builder()
                            .add(Items.COOKED_BEEF)
                            .build(),
                    InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SANDWICH.get()),
                    "obtain_sandwich",
                    false
            );

            addAdvancement(
                    consumer,
                    obtainBreadSlice,
                    new ItemStack(ModBlocks.REDSTONE_TOASTER.get()),
                    InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TOASTED_BREAD_SLICE.get()),
                    "obtain_toasted_bread_slice",
                    false
            );

            addAdvancement(
                    consumer,
                    obtainSandwich,
                    SandwichBuilder.builder()
                            .addFakeSpread(16262179)
                            .build(),
                    ModAdvancementTriggers.ADD_POTION_TO_SANDWICH.instance(),
                    "add_potion_to_sandwich",
                    true
            );

            addAdvancement(
                    consumer,
                    obtainSandwich,
                    SandwichBuilder.blt(),
                    ModAdvancementTriggers.CONSUME_BLT_SANDWICH.instance(),
                    "consume_blt_sandwich",
                    true
            );

            addAdvancement(
                    consumer,
                    obtainSandwich,
                    SandwichBuilder.builder()
                            .add(ModItems.CHOPPED_BEETROOT.get())
                            .addBread()
                            .add(ModItems.CHOPPED_BEETROOT.get())
                            .build(),
                    ModAdvancementTriggers.CONSUME_DOUBLE_DECKER_SANDWICH.instance(),
                    "consume_double_decker_sandwich",
                    true
            );
        }

        private static Advancement addAdvancement(Consumer<Advancement> consumer, Advancement parent, ItemStack display, AbstractCriterionTriggerInstance criterion, String name, boolean hidden) {
            return Advancement.Builder.advancement().parent(parent).display(display,
                    new TranslatableComponent("advancement." + SomeAssemblyRequired.MODID + "." + name + ".title"),
                    new TranslatableComponent("advancement." + SomeAssemblyRequired.MODID + "." + name + ".description"),
                    null, FrameType.TASK, true, true, hidden)
                    .addCriterion(name, criterion)
                    .save(consumer, Util.id(name).toString());
        }
    }
}
