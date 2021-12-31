package someassemblyrequired.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTables extends LootTableProvider {

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> tables = new ArrayList<>();

    public LootTables(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        tables.clear();

        for (Block block : ModBlocks.getSandwichAssemblyTables()) {
            addStandardDropTable(block);
        }
        for (Block block : ModBlocks.getToasters()) {
            addStandardDropTable(block);
        }


        addBlockLootTable(ModBlocks.LETTUCE.get(), createCropWithBonusSeedsLootTable((CropBlock) ModBlocks.LETTUCE.get(), ModItems.LETTUCE_HEAD.get(), ModItems.LETTUCE_SEEDS.get()));

        addChestLootTables();

        return tables;
    }

    private void addChestLootTables() {
        addChestLootTable("inject/chests/village/village_desert_house", LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .name("main")
                        .setRolls(ConstantValue.exactly(1))
                        .add(emptyEntry(12))
                )
        );

        addChestLootTable("inject/chests/village/village_savanna_house", LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .name("main")
                        .setRolls(ConstantValue.exactly(1))
                        .add(itemEntry(ModItems.LETTUCE_HEAD.get(), 1, 1, 3))
                        .add(itemEntry(ModItems.LETTUCE_SEEDS.get(), 1, 1, 3))
                        .add(emptyEntry(12))
                )
        );

        addChestLootTable("inject/chests/village/village_plains_house", LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .name("main")
                        .setRolls(ConstantValue.exactly(1))
                        .add(itemEntry(ModItems.LETTUCE_HEAD.get(), 1, 1, 4))
                        .add(itemEntry(ModItems.LETTUCE_SEEDS.get(), 2, 1, 4))
                        .add(emptyEntry(12))
                )
        );

        addChestLootTable("inject/chests/village/village_taiga_house", LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .name("main")
                        .setRolls(ConstantValue.exactly(1))
                        .add(itemEntry(ModItems.LETTUCE_HEAD.get(), 2, 1, 3))
                        .add(itemEntry(ModItems.LETTUCE_SEEDS.get(), 2, 1, 3))
                        .add(emptyEntry(12))
                )
        );

        addChestLootTable("inject/chests/village/village_snowy_house", LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .name("main")
                        .setRolls(ConstantValue.exactly(1))
                        .add(itemEntry(ModItems.LETTUCE_HEAD.get(), 2, 2, 6))
                        .add(itemEntry(ModItems.LETTUCE_SEEDS.get(), 4, 2, 6))
                        .add(emptyEntry(12))
                )
        );

        addChestLootTable("inject/chests/igloo_chest", LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .name("main")
                        .setRolls(ConstantValue.exactly(2))
                        .add(itemEntry(ModItems.LETTUCE_HEAD.get(), 6, 1, 4))
                        .add(itemEntry(ModItems.LETTUCE_SEEDS.get(), 6, 1, 4))
                        .add(emptyEntry(12))
                )
        );

        addChestLootTable("inject/chests/shipwreck_supply", LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .name("main")
                        .setRolls(ConstantValue.exactly(2))
                        .add(itemEntry(ModItems.LETTUCE_HEAD.get(), 1, 2, 6))
                        .add(itemEntry(ModItems.LETTUCE_SEEDS.get(), 1, 2, 6))
                        .add(emptyEntry(12))
                )
        );
    }

    private static LootTable.Builder createCropWithBonusSeedsLootTable(CropBlock cropsBlock, Item cropItem, Item seedItem) {
        LootItemCondition.Builder condition = createAgeCondition(cropsBlock);
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(cropItem)
                                .when(condition)
                                .otherwise(LootItem.lootTableItem(seedItem))
                        )
                )
                .withPool(LootPool.lootPool()
                        .when(condition)
                        .add(LootItem.lootTableItem(seedItem)
                                .apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))
                        )
                )
                .apply(ApplyExplosionDecay.explosionDecay());
    }

    private static LootTable.Builder createSeedCropLootTable(CropBlock cropsBlock, Item cropItem, Item seedItem) {
        LootItemCondition.Builder condition = createAgeCondition(cropsBlock);
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .when(InvertedLootItemCondition.invert(condition))
                        .add(LootItem.lootTableItem(seedItem))
                )
                .withPool(LootPool.lootPool()
                        .when(condition)
                        .add(LootItem.lootTableItem(cropItem))
                )
                .withPool(LootPool.lootPool()
                        .when(condition)
                        .add(LootItem.lootTableItem(cropItem)
                                .apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))
                        )
                )
                .apply(ApplyExplosionDecay.explosionDecay());
    }

    private static LootItemCondition.Builder createAgeCondition(CropBlock cropsBlock) {
        //noinspection OptionalGetWithoutIsPresent
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropsBlock)
                .setProperties(StatePropertiesPredicate.Builder.properties()
                        .hasProperty(cropsBlock.getAgeProperty(), cropsBlock.getAgeProperty().getPossibleValues().stream().max(Integer::compare).get())
                );
    }

    private static LootPoolEntryContainer.Builder<?> itemEntry(Item item, int weight, int min, int max) {
        return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
    }

    private static LootPoolEntryContainer.Builder<?> emptyEntry(int weight) {
        return EmptyLootItem.emptyItem().setWeight(weight);
    }


    private void addStandardDropTable(Block block) {
        addBlockLootTable(block, LootTable.lootTable().withPool(createStandardDrops(block)));
    }

    private void addBlockLootTable(Block block, LootTable.Builder lootTable) {
        tables.add(Pair.of(() -> lootBuilder -> lootBuilder.accept(block.getLootTable(), lootTable), LootContextParamSets.BLOCK));
    }

    private void addChestLootTable(String location, LootTable.Builder lootTable) {
        tables.add(Pair.of(() -> lootBuilder -> lootBuilder.accept(Util.id(location), lootTable), LootContextParamSets.ALL_PARAMS));
    }

    private LootPool.Builder createStandardDrops(ItemLike itemProvider) {
        return LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(ExplosionCondition.survivesExplosion())
                .add(LootItem.lootTableItem(itemProvider));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        map.forEach((location, lootTable) -> net.minecraft.world.level.storage.loot.LootTables.validate(validationtracker, location, lootTable));
    }
}
