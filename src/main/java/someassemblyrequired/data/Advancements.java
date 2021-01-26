package someassemblyrequired.data;

import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.data.AdvancementProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.advancements.HusbandryAdvancements;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModAdvancements;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;
import someassemblyrequired.common.util.SandwichBuilder;
import someassemblyrequired.common.util.Util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public class Advancements extends AdvancementProvider {

    private final Path PATH;

    public Advancements(DataGenerator generator) {
        super(generator);
        PATH = generator.getOutputFolder();
    }

    @Override
    public void act(DirectoryCache cache) {
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (advancement) -> {
            if (!set.add(advancement.getId())) {
                throw new IllegalStateException("Duplicate advancement " + advancement.getId());
            } else {
                Path path1 = getPath(PATH, advancement);

                try {
                    IDataProvider.save((new GsonBuilder()).setPrettyPrinting().create(), cache, advancement.copy().serialize(), path1);
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

            Advancement obtainBreadSlice = registerSimple(
                    consumer,
                    root,
                    new ItemStack(ModItems.KITCHEN_KNIFE.get()),
                    InventoryChangeTrigger.Instance.forItems(ItemPredicate.Builder.create().tag(ModTags.BREAD_SLICES).build()),
                    "obtain_bread_slice",
                    false
            );

            Advancement obtainSandwich = registerSimple(
                    consumer,
                    obtainBreadSlice,
                    SandwichBuilder.create()
                            .add(ModItems.LETTUCE_LEAF.get())
                            .build(),
                    InventoryChangeTrigger.Instance.forItems(ModItems.SANDWICH.get()),
                    "obtain_sandwich",
                    false
            );

            registerSimple(
                    consumer,
                    obtainBreadSlice,
                    new ItemStack(ModBlocks.REDSTONE_TOASTER.get()),
                    InventoryChangeTrigger.Instance.forItems(ModItems.TOASTED_BREAD_SLICE.get()),
                    "obtain_toasted_bread_slice",
                    false
            );

            registerSimple(
                    consumer,
                    obtainSandwich,
                    SandwichBuilder.create()
                            .addFakeSpread(16262179)
                            .build(),
                    ModAdvancements.ADD_POTION_TO_SANDWICH.instance(),
                    "add_potion_to_sandwich",
                    true
            );

            registerSimple(
                    consumer,
                    obtainSandwich,
                    SandwichBuilder.blt(),
                    ModAdvancements.CONSUME_BLT_SANDWICH.instance(),
                    "consume_blt_sandwich",
                    true
            );

            registerSimple(
                    consumer,
                    obtainSandwich,
                    SandwichBuilder.create()
                            .add(ModItems.CHOPPED_BEETROOT.get())
                            .addBread()
                            .add(ModItems.CHOPPED_BEETROOT.get())
                            .build(),
                    ModAdvancements.CONSUME_DOUBLE_DECKER_SANDWICH.instance(),
                    "consume_double_decker_sandwich",
                    true
            );
        }

        private static Advancement registerSimple(Consumer<Advancement> consumer, Advancement parent, ItemStack display, CriterionInstance criterion, String name, boolean hidden) {
            return Advancement.Builder.builder().withParent(parent).withDisplay(display,
                    new TranslationTextComponent("advancement." + SomeAssemblyRequired.MODID + "." + name + ".title"),
                    new TranslationTextComponent("advancement." + SomeAssemblyRequired.MODID + "." + name + ".description"),
                    null, FrameType.TASK, true, true, hidden)
                    .withCriterion(name, criterion)
                    .register(consumer, Util.prefix(name).toString());
        }
    }
}
