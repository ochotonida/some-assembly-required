package someassemblyrequired.data;

import com.google.common.collect.Sets;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.HusbandryAdvancements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.data.ExistingFileHelper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModAdvancementTriggers;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;
import someassemblyrequired.common.item.sandwich.SandwichItem;
import someassemblyrequired.common.util.Util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public class Advancements extends AdvancementProvider {

    private final DataGenerator.PathProvider pathProvider;

    public Advancements(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, helper);
        this.pathProvider = generator.createPathProvider(DataGenerator.Target.DATA_PACK, "advancements");
    }

    @Override
    public void run(CachedOutput cache) {
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (advancement) -> {
            if (!set.add(advancement.getId())) {
                throw new IllegalStateException("Duplicate advancement " + advancement.getId());
            } else {
                Path path = this.pathProvider.json(advancement.getId());

                try {
                    DataProvider.saveStable(cache, advancement.deconstruct().serializeToJson(), path);
                } catch (IOException ioexception) {
                    SomeAssemblyRequired.LOGGER.error("Couldn't save advancement {}", path, ioexception);
                }
            }
        };

        new SomeAssemblyRequiredAdvancements().accept(consumer);
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
                    SandwichItem.makeSandwich(ModItems.TOMATO_SLICES.get(), ModItems.CHOPPED_CARROT.get()),
                    InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SANDWICH.get()),
                    "obtain_sandwich",
                    false
            );

            addAdvancement(
                    consumer,
                    obtainBreadSlice,
                    new ItemStack(ModItems.TOASTED_BREAD_SLICE.get()),
                    InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TOASTED_BREAD_SLICE.get()),
                    "obtain_toasted_bread_slice",
                    false
            );

            addAdvancement(
                    consumer,
                    obtainSandwich,
                    SandwichItem.makeSandwich(Potions.NIGHT_VISION),
                    ModAdvancementTriggers.CONSUME_POTION_SANDWICH.instance(),
                    "add_potion_to_sandwich",
                    true
            );

            addAdvancement(
                    consumer,
                    obtainSandwich,
                    SandwichItem.makeSandwich(
                            ModItems.TOMATO_SLICES.get(),
                            ModItems.CHOPPED_CARROT.get(),
                            ModItems.BREAD_SLICE.get(),
                            ModItems.TOMATO_SLICES.get(),
                            ModItems.CHOPPED_CARROT.get()
                    ),
                    ModAdvancementTriggers.CONSUME_DOUBLE_DECKER_SANDWICH.instance(),
                    "consume_double_decker_sandwich",
                    true
            );
        }

        private static Advancement addAdvancement(Consumer<Advancement> consumer, Advancement parent, ItemStack display, AbstractCriterionTriggerInstance criterion, String name, boolean hidden) {
            return Advancement.Builder.advancement().parent(parent).display(display,
                    Util.translate("advancement.%s.title".formatted(name)),
                    Util.translate("advancement.%s.description".formatted(name)),
                    null, FrameType.TASK, true, true, hidden)
                    .addCriterion(name, criterion)
                    .save(consumer, Util.id(name).toString());
        }
    }
}
