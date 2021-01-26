package someassemblyrequired.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.Inverted;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.ExplosionDecay;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
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

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> tables = new ArrayList<>();

    public LootTables(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        tables.clear();

        for (Block block : ModBlocks.getSandwichAssemblyTables()) {
            addStandardDropTable(block);
        }
        for (Block block : ModBlocks.getCuttingBoards()) {
            addStandardDropTable(block);
        }
        for (Block block : ModBlocks.getToasters()) {
            addStandardDropTable(block);
        }


        addBlockLootTable(ModBlocks.LETTUCE.get(), createCropWithBonusSeedsLootTable((CropsBlock) ModBlocks.LETTUCE.get(), ModItems.LETTUCE_HEAD.get(), ModItems.LETTUCE_SEEDS.get()));
        addBlockLootTable(ModBlocks.TOMATOES.get(), createSeedCropLootTable((CropsBlock) ModBlocks.TOMATOES.get(), ModItems.TOMATO.get(), ModItems.TOMATO_SEEDS.get()));

        addChestLootTables();

        return tables;
    }

    private void addChestLootTables() {
        addChestLootTable("inject/chests/village/village_desert_house", LootTable.builder()
                .addLootPool(LootPool.builder()
                        .name("main")
                        .rolls(ConstantRange.of(1))
                        .addEntry(itemEntry(ModItems.TOMATO.get(), 6, 2, 6))
                        .addEntry(emptyEntry(12))
                )
        );

        addChestLootTable("inject/chests/village/village_savanna_house", LootTable.builder()
                .addLootPool(LootPool.builder()
                        .name("main")
                        .rolls(ConstantRange.of(1))
                        .addEntry(itemEntry(ModItems.TOMATO.get(), 4, 1, 3))
                        .addEntry(itemEntry(ModItems.LETTUCE_HEAD.get(), 1, 1, 3))
                        .addEntry(itemEntry(ModItems.LETTUCE_SEEDS.get(), 1, 1, 3))
                        .addEntry(emptyEntry(12))
                )
        );

        addChestLootTable("inject/chests/village/village_plains_house", LootTable.builder()
                .addLootPool(LootPool.builder()
                        .name("main")
                        .rolls(ConstantRange.of(1))
                        .addEntry(itemEntry(ModItems.TOMATO.get(), 3, 1, 4))
                        .addEntry(itemEntry(ModItems.LETTUCE_HEAD.get(), 1, 1, 4))
                        .addEntry(itemEntry(ModItems.LETTUCE_SEEDS.get(), 2, 1, 4))
                        .addEntry(emptyEntry(12))
                )
        );

        addChestLootTable("inject/chests/village/village_taiga_house", LootTable.builder()
                .addLootPool(LootPool.builder()
                        .name("main")
                        .rolls(ConstantRange.of(1))
                        .addEntry(itemEntry(ModItems.TOMATO.get(), 2, 1, 3))
                        .addEntry(itemEntry(ModItems.LETTUCE_HEAD.get(), 2, 1, 3))
                        .addEntry(itemEntry(ModItems.LETTUCE_SEEDS.get(), 2, 1, 3))
                        .addEntry(emptyEntry(12))
                )
        );

        addChestLootTable("inject/chests/village/village_snowy_house", LootTable.builder()
                .addLootPool(LootPool.builder()
                        .name("main")
                        .rolls(ConstantRange.of(1))
                        .addEntry(itemEntry(ModItems.LETTUCE_HEAD.get(), 2, 2, 6))
                        .addEntry(itemEntry(ModItems.LETTUCE_SEEDS.get(), 4, 2, 6))
                        .addEntry(emptyEntry(12))
                )
        );

        addChestLootTable("inject/chests/igloo_chest", LootTable.builder()
                .addLootPool(LootPool.builder()
                        .name("main")
                        .rolls(ConstantRange.of(2))
                        .addEntry(itemEntry(ModItems.LETTUCE_HEAD.get(), 6, 1, 4))
                        .addEntry(itemEntry(ModItems.LETTUCE_SEEDS.get(), 6, 1, 4))
                        .addEntry(emptyEntry(12))
                )
        );

        addChestLootTable("inject/chests/shipwreck_supply", LootTable.builder()
                .addLootPool(LootPool.builder()
                        .name("main")
                        .rolls(ConstantRange.of(2))
                        .addEntry(itemEntry(ModItems.TOMATO.get(), 4, 2, 6))
                        .addEntry(itemEntry(ModItems.LETTUCE_HEAD.get(), 1, 2, 6))
                        .addEntry(itemEntry(ModItems.LETTUCE_SEEDS.get(), 1, 2, 6))
                        .addEntry(emptyEntry(12))
                )
        );
    }

    private static LootTable.Builder createCropWithBonusSeedsLootTable(CropsBlock cropsBlock, Item cropItem, Item seedItem) {
        ILootCondition.IBuilder condition = createAgeCondition(cropsBlock);
        return LootTable.builder()
                .addLootPool(LootPool.builder()
                        .addEntry(ItemLootEntry.builder(cropItem)
                                .acceptCondition(condition)
                                .alternatively(ItemLootEntry.builder(seedItem))
                        )
                )
                .addLootPool(LootPool.builder()
                        .acceptCondition(condition)
                        .addEntry(ItemLootEntry.builder(seedItem)
                                .acceptFunction(ApplyBonus.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286F, 3))
                        )
                )
                .acceptFunction(ExplosionDecay.builder());
    }

    private static LootTable.Builder createSeedCropLootTable(CropsBlock cropsBlock, Item cropItem, Item seedItem) {
        ILootCondition.IBuilder condition = createAgeCondition(cropsBlock);
        return LootTable.builder()
                .addLootPool(LootPool.builder()
                        .acceptCondition(Inverted.builder(condition))
                        .addEntry(ItemLootEntry.builder(seedItem))
                )
                .addLootPool(LootPool.builder()
                        .acceptCondition(condition)
                        .addEntry(ItemLootEntry.builder(cropItem))
                )
                .addLootPool(LootPool.builder()
                        .acceptCondition(condition)
                        .addEntry(ItemLootEntry.builder(cropItem)
                                .acceptFunction(ApplyBonus.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286F, 3))
                        )
                )
                .acceptFunction(ExplosionDecay.builder());
    }

    private static ILootCondition.IBuilder createAgeCondition(CropsBlock cropsBlock) {
        //noinspection OptionalGetWithoutIsPresent
        return BlockStateProperty.builder(cropsBlock)
                .fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                        .withIntProp(cropsBlock.getAgeProperty(), cropsBlock.getAgeProperty().getAllowedValues().stream().max(Integer::compare).get())
                );
    }

    private static LootEntry.Builder<?> itemEntry(Item item, int weight, int min, int max) {
        return ItemLootEntry.builder(item).weight(weight).acceptFunction(SetCount.builder(RandomValueRange.of(min, max)));
    }

    private static LootEntry.Builder<?> emptyEntry(int weight) {
        return EmptyLootEntry.func_216167_a().weight(weight);
    }


    private void addStandardDropTable(Block block) {
        addBlockLootTable(block, LootTable.builder().addLootPool(createStandardDrops(block)));
    }

    private void addBlockLootTable(Block block, LootTable.Builder lootTable) {
        tables.add(Pair.of(() -> lootBuilder -> lootBuilder.accept(block.getLootTable(), lootTable), LootParameterSets.BLOCK));
    }

    private void addChestLootTable(String location, LootTable.Builder lootTable) {
        tables.add(Pair.of(() -> lootBuilder -> lootBuilder.accept(Util.prefix(location), lootTable), LootParameterSets.GENERIC));
    }

    private LootPool.Builder createStandardDrops(IItemProvider itemProvider) {
        return LootPool.builder().rolls(ConstantRange.of(1)).acceptCondition(SurvivesExplosion.builder())
                .addEntry(ItemLootEntry.builder(itemProvider));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        map.forEach((loc, table) -> LootTableManager.validateLootTable(validationtracker, loc, table));
    }
}
