package someassemblyrequired.data;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModTags;
import someassemblyrequired.common.loot.SetIngredientsFunction;
import someassemblyrequired.common.loot.SmeltMatchingItemFunction;
import someassemblyrequired.common.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("SameParameterValue")
public class LootTables extends LootTableProvider {

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> tables = new ArrayList<>();

    private final ExistingFileHelper existingFileHelper;
    private final LootModifiers lootModifiers;

    protected static final ResourceLocation VILLAGE_SANDWICH = Util.id("sandwich/village");

    public LootTables(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper, LootModifiers lootModifiers) {
        super(dataGenerator);
        this.existingFileHelper = existingFileHelper;
        this.lootModifiers = lootModifiers;
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        tables.clear();

        addStandardDropTable(ModBlocks.SANDWICHING_STATION.get());
        addSandwichLootTables();

        for (LootModifiers.Builder lootBuilder : lootModifiers.lootBuilders) {
            addLootTable(lootBuilder.getName(), lootBuilder.createLootTable(), lootBuilder.getParameterSet());
        }

        return tables;
    }

    private void addSandwichLootTables() {
        ResourceLocation genericSandwichLootTable = Util.id("sandwich/village_generic");
        ResourceLocation sandwichLayerLootTable = Util.id("sandwich/village_generic_layer");
        ResourceLocation specialSandwichLootTable = Util.id("sandwich/village_special");

        addLootTable(sandwichLayerLootTable.getPath(),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(item(ModItems.BREAD_SLICE.get()))
                ).withPool(LootPool.lootPool()
                        .add(AlternativesEntry.alternatives(
                                        TagEntry.expandTag(ModTags.LOOT_MEAT)
                                                .when(chance(0.65))
                                ).otherwise(
                                        TagEntry.expandTag(ModTags.LOOT_VEGETABLES)
                                )
                        )
                ).withPool(LootPool.lootPool()
                        .add(EntryGroup.list(
                                AlternativesEntry.alternatives(
                                        TagEntry.expandTag(ModTags.LOOT_VEGETABLES)
                                )
                        ))
                )
                , LootContextParamSets.CHEST
        );

        addLootTable(genericSandwichLootTable.getPath(),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(LootTableReference.lootTableReference(sandwichLayerLootTable))
                ).withPool(LootPool.lootPool()
                        .add(LootTableReference.lootTableReference(sandwichLayerLootTable))
                        .when(chance(0.1))
                ).withPool(LootPool.lootPool()
                        .add(item(ModItems.BREAD_SLICE.get()))
                ), LootContextParamSets.CHEST
        );

        addLootTable(specialSandwichLootTable.getPath(),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(item(ModItems.BREAD_SLICE.get()))
                ).withPool(LootPool.lootPool()
                        .add(AlternativesEntry.alternatives(
                                TagEntry.expandTag(ModTags.LOOT_SPECIAL).when(chance(0.1))
                        ).otherwise(
                                TagEntry.expandTag(ModTags.LOOT_OTHER)
                        ))
                ).withPool(LootPool.lootPool()
                        .add(item(ModItems.BREAD_SLICE.get()))
                ),
                LootContextParamSets.CHEST
        );

        addLootTable(VILLAGE_SANDWICH.getPath(),
                LootTable.lootTable().withPool(LootPool.lootPool().add(
                        sandwich().apply(
                                SetIngredientsFunction.setIngredients().withEntry(
                                        AlternativesEntry.alternatives(
                                                LootTableReference.lootTableReference(specialSandwichLootTable)
                                                        .when(chance(0.35))
                                        ).otherwise(
                                                LootTableReference.lootTableReference(genericSandwichLootTable).apply(
                                                        toastRandomly(0.25)
                                                )
                                        )
                                )
                        ).apply(count(3, 8))
                ).when(chance(0.3))), LootContextParamSets.CHEST);
    }


    private void addStandardDropTable(Block block) {
        addBlockLootTable(block, LootTable.lootTable().withPool(createStandardDrops(block)));
    }

    private void addBlockLootTable(Block block, LootTable.Builder lootTable) {
        tables.add(Pair.of(() -> lootBuilder -> lootBuilder.accept(block.getLootTable(), lootTable), LootContextParamSets.BLOCK));
    }

    private LootPool.Builder createStandardDrops(ItemLike itemProvider) {
        return LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(ExplosionCondition.survivesExplosion())
                .add(LootItem.lootTableItem(itemProvider));
    }

    protected static LootItemConditionalFunction.Builder<?> toastRandomly(double chance) {
        return SmeltMatchingItemFunction.smeltMatching(ModItems.BREAD_SLICE.get()).when(LootItemRandomChanceCondition.randomChance((float) chance));
    }

    protected static LootPoolSingletonContainer.Builder<?> sandwich(Potion potion) {
        return sandwich(potion(potion));
    }

    protected static LootPoolSingletonContainer.Builder<?> sandwich(Item item) {
        return sandwich(item(item));
    }

    protected static LootPoolSingletonContainer.Builder<?> sandwich(LootPoolSingletonContainer.Builder<?> ingredient) {
        return sandwich().apply(SetIngredientsFunction.setIngredients()
                .withEntry(item(ModItems.BREAD_SLICE.get()))
                .withEntry(ingredient)
                .withEntry(item(ModItems.BREAD_SLICE.get()))
        );
    }

    protected static LootPoolSingletonContainer.Builder<?> sandwich() {
        return item(ModItems.SANDWICH.get());
    }

    protected static LootPoolSingletonContainer.Builder<?> potion(Potion potion) {
        return LootTables.item(Items.POTION).apply(SetPotionFunction.setPotion(potion));
    }

    protected static LootPoolSingletonContainer.Builder<?> item(Item item) {
        return item(1, item);
    }

    protected static LootPoolSingletonContainer.Builder<?> item(int weight, Item item) {
        return LootItem.lootTableItem(item).setWeight(weight);
    }

    protected static LootItemConditionalFunction.Builder<?> count(int min, int max) {
        return SetItemCountFunction.setCount(UniformGenerator.between(min, max));
    }

    protected static LootItemCondition.Builder chance(double chance) {
        return LootItemRandomChanceCondition.randomChance((float) chance);
    }

    private void addLootTable(String location, LootTable.Builder lootTable, LootContextParamSet lootParameterSet) {
        if (location.startsWith("inject/")) {
            String actualLocation = location.replace("inject/", "");
            Preconditions.checkArgument(existingFileHelper.exists(new ResourceLocation("loot_tables/" + actualLocation + ".json"), PackType.SERVER_DATA), "Loot table %s does not exist in any known data pack", actualLocation);
        }
        tables.add(Pair.of(() -> lootBuilder -> lootBuilder.accept(new ResourceLocation(SomeAssemblyRequired.MODID, location), lootTable), lootParameterSet));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationContext) {
        map.forEach((location, lootTable) -> net.minecraft.world.level.storage.loot.LootTables.validate(validationContext, location, lootTable));
    }
}
