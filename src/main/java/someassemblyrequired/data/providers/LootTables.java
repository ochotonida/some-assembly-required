package someassemblyrequired.data.providers;

import com.google.common.base.Preconditions;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
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
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.loot.OptionalLootItem;
import someassemblyrequired.loot.SetSandwichContentsFunction;
import someassemblyrequired.loot.SmeltMatchingItemFunction;
import someassemblyrequired.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static someassemblyrequired.registry.ModItems.*;

// TODO sandwich loot tables can probably be simplified a bit by using inline nested loot tables
@SuppressWarnings("SameParameterValue")
public class LootTables extends LootTableProvider {

    private final List<SubProviderEntry> lootTables = new ArrayList<>();

    private final ExistingFileHelper existingFileHelper;
    private final LootModifiers lootModifiers;

    protected static final ResourceKey<LootTable> VILLAGE_SANDWICH = SomeAssemblyRequired.key(Registries.LOOT_TABLE, "inject/chests/village_house");

    public LootTables(PackOutput packOutput, ExistingFileHelper existingFileHelper, LootModifiers lootModifiers, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, Set.of(), List.of(), registries);
        this.existingFileHelper = existingFileHelper;
        this.lootModifiers = lootModifiers;
    }

    @Override
    public List<SubProviderEntry> getTables() {
        lootTables.clear();

        addStandardDropTable(ModBlocks.SANDWICHING_STATION.get());
        addSandwichLootTables();

        for (LootModifiers.Builder lootBuilder : lootModifiers.lootBuilders) {
            addLootTable(lootBuilder.getName(), lootBuilder.createLootTable(), lootBuilder.getParameterSet());
        }

        return lootTables;
    }

    private void addSandwichLootTables() {
        ResourceKey<LootTable> sandwich = SomeAssemblyRequired.key(Registries.LOOT_TABLE, "sandwich/sandwich");
        ResourceKey<LootTable> sandwichLayer = SomeAssemblyRequired.key(Registries.LOOT_TABLE, "sandwich/sandwich_layer");
        ResourceKey<LootTable> special = SomeAssemblyRequired.key(Registries.LOOT_TABLE, "sandwich/special");
        ResourceKey<LootTable> burger = SomeAssemblyRequired.key(Registries.LOOT_TABLE, "sandwich/burger");

        ResourceKey<LootTable> protein = SomeAssemblyRequired.key(Registries.LOOT_TABLE, "sandwich/ingredients/protein");
        ResourceKey<LootTable> vegetables = SomeAssemblyRequired.key(Registries.LOOT_TABLE, "sandwich/ingredients/vegetables");

        addLootTable(protein.location().getPath(),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(whenNotLoaded(Items.COOKED_BEEF, 1, ModCompat.FARMERSDELIGHT))
                        .add(whenNotLoaded(Items.COOKED_PORKCHOP, 1, ModCompat.FARMERSDELIGHT))
                        .add(whenNotLoaded(Items.COOKED_SALMON, 1, ModCompat.FARMERSDELIGHT))
                        .add(whenNotLoaded(Items.COOKED_COD, 1, ModCompat.FARMERSDELIGHT))

                        .add(whenLoaded(ModItems.COOKED_BACON.get(), 3))
                        .add(whenLoaded(ModItems.FRIED_EGG.get(), 4))
                        .add(whenLoaded(ModItems.COOKED_CHICKEN_CUTS.get(), 3))
                        .add(whenLoaded(ModItems.COOKED_MUTTON_CHOPS.get(), 2))
                        .add(whenLoaded(ModItems.COOKED_COD_SLICE.get(), 1))
                        .add(whenLoaded(ModItems.COOKED_SALMON_SLICE.get(), 1))
                )
                , LootContextParamSets.CHEST
        );

        addLootTable(vegetables.location().getPath(),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(item(Items.SWEET_BERRIES, 1))

                        .add(item(CHOPPED_CARROT.get(), 3))
                        .add(item(CHOPPED_GOLDEN_CARROT.get(), 1))
                        .add(item(APPLE_SLICES.get(), 2))
                        .add(item(GOLDEN_APPLE_SLICES.get(), 1))
                        .add(item(CHOPPED_BEETROOT.get(), 3))
                        .add(item(TOMATO_SLICES.get(), 4))

                        .add(whenLoaded(ModItems.CABBAGE_LEAF.get(), 4))
                )
                , LootContextParamSets.CHEST
        );

        addLootTable(sandwichLayer.location().getPath(),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(item(BREAD_SLICE.get()))
                ).withPool(LootPool.lootPool()
                        .add(AlternativesEntry.alternatives(
                                    NestedLootTable.lootTableReference(protein).when(chance(0.7))
                                ).otherwise(
                                    NestedLootTable.lootTableReference(vegetables)
                                )
                        )
                ).withPool(LootPool.lootPool()
                        .add(NestedLootTable.lootTableReference(vegetables))
                )
                , LootContextParamSets.CHEST
        );

        addLootTable(sandwich.location().getPath(),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(NestedLootTable.lootTableReference(sandwichLayer))
                ).withPool(LootPool.lootPool()
                        .add(NestedLootTable.lootTableReference(sandwichLayer))
                        .when(chance(0.1))
                ).withPool(LootPool.lootPool()
                        .add(item(BREAD_SLICE.get()))
                ), LootContextParamSets.CHEST
        );

        addLootTable(special.location().getPath(),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(item(BREAD_SLICE.get()))
                ).withPool(LootPool.lootPool()
                        .add(item(Items.HONEY_BOTTLE, 15))
                        .add(item(TOASTED_BREAD_SLICE.get(), 1))
                        .add(item(Items.MILK_BUCKET, 1))
                        .add(item(Items.POTATO, 1))

                        .add(whenLoaded(ModItems.SQUID_INK_PASTA.get(), 1))
                        .add(whenLoaded(ModItems.EGG_SANDWICH.get(), 1))
                        .add(whenLoaded(ModItems.HOT_COCOA.get(), 1))
                        .add(whenLoaded(ModItems.PUMPKIN_SOUP.get(), 1))
                        .add(whenLoaded(ModItems.PASTA_WITH_MEATBALLS.get(), 1))
                        .add(whenLoaded(ModItems.RATATOUILLE.get(), 1))

                        .add(whenLoaded(AllFluids.CHOCOLATE.get().getBucket(), 15))
                        .add(whenLoaded(AllItems.BUILDERS_TEA.get(), 1))
                        .add(whenLoaded(AllItems.CHOCOLATE_BERRIES.get(), 1))
                ).withPool(LootPool.lootPool()
                        .add(item(BREAD_SLICE.get()))
                ),
                LootContextParamSets.CHEST
        );

        addLootTable(burger.location().getPath(),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(item(BURGER_BUN_BOTTOM.get()))
                ).withPool(LootPool.lootPool()
                        .add(whenLoaded(ModItems.BEEF_PATTY.get(), 5))
                        // .add(whenLoaded(MDItems.VEGAN_PATTY.get(), 5))
                        .add(whenLoaded(ModItems.COOKED_BACON.get(), 2))
                        .add(whenLoaded(ModItems.COOKED_MUTTON_CHOPS.get(), 1))
                        .add(whenLoaded(ModItems.FRIED_EGG.get(), 2))
                        .add(whenNotLoaded(Items.COOKED_BEEF, 1, ModCompat.FARMERSDELIGHT))
                        .add(whenNotLoaded(Items.COOKED_PORKCHOP, 1, ModCompat.FARMERSDELIGHT))
                ).withPool(LootPool.lootPool()
                        .when(chance(0.2))
                        .add(whenLoaded(ModItems.COOKED_BACON.get(), 1))
                        .add(whenLoaded(ModItems.TOMATO_SAUCE.get(), 1))
                ).withPool(LootPool.lootPool()
                        .when(chance(0.5))
                        .add(whenLoaded(ModItems.CABBAGE_LEAF.get(), 1))
                ).withPool(LootPool.lootPool()
                        .when(chance(0.5))
                        .add(item(TOMATO_SLICES.get()))
                ).withPool(LootPool.lootPool()
                        .when(chance(0.6))
                        .add(item(SLICED_ONION.get()))
                ).withPool(LootPool.lootPool()
                        .when(chance(0.2))
                        .add(item(Items.SWEET_BERRIES, 1))
                        .add(item(CHOPPED_CARROT.get(), 3))
                        .add(item(CHOPPED_GOLDEN_CARROT.get(), 1))
                        .add(item(APPLE_SLICES.get(), 2))
                        .add(item(GOLDEN_APPLE_SLICES.get(), 1))
                        .add(item(CHOPPED_BEETROOT.get(), 3))
                ).withPool(LootPool.lootPool()
                        .add(item(BURGER_BUN_TOP.get()))
                ),
                LootContextParamSets.CHEST
        );

        addLootTableUnchecked(VILLAGE_SANDWICH.location().getPath(),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .when(chance(0.4))
                        .add(sandwich()
                                .setWeight(4)
                                .apply(count(2, 5))
                                .apply(SetSandwichContentsFunction.setIngredients().withEntry(
                                        AlternativesEntry.alternatives(
                                                NestedLootTable.lootTableReference(special)
                                                        .when(chance(0.15)),
                                                NestedLootTable.lootTableReference(sandwich)
                                                        .when(chance(0.25))
                                                        .apply(toastBread())
                                        ).otherwise(
                                                NestedLootTable.lootTableReference(sandwich)
                                        )
                                ))
                        ).add(sandwich()
                                .apply(count(2, 5))
                                .apply(SetSandwichContentsFunction.setIngredients().withEntry(
                                        NestedLootTable.lootTableReference(burger)
                                ))
                        )
                ), LootContextParamSets.CHEST
        );
    }

    private void addStandardDropTable(Block block) {
        addBlockLootTable(block, LootTable.lootTable().withPool(createStandardDrops(block)));
    }

    private void addBlockLootTable(Block block, LootTable.Builder lootTable) {
        lootTables.add(new SubProviderEntry(registries -> lootBuilder -> lootBuilder.accept(block.getLootTable(), lootTable), LootContextParamSets.BLOCK));
    }

    private LootPool.Builder createStandardDrops(ItemLike itemProvider) {
        return LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(ExplosionCondition.survivesExplosion())
                .add(LootItem.lootTableItem(itemProvider));
    }

    protected static LootItemConditionalFunction.Builder<?> toastBread() {
        return SmeltMatchingItemFunction.smeltMatching(BREAD_SLICE);
    }

    protected static LootPoolSingletonContainer.Builder<?> sandwich(Holder<Potion> potion) {
        return sandwich(potion(potion));
    }

    protected static LootPoolSingletonContainer.Builder<?> sandwich(Item item) {
        return sandwich(item(item));
    }

    protected static LootPoolSingletonContainer.Builder<?> sandwich(LootPoolSingletonContainer.Builder<?> ingredient) {
        return sandwich().apply(SetSandwichContentsFunction.setIngredients()
                .withEntry(item(BREAD_SLICE.get()))
                .withEntry(ingredient)
                .withEntry(item(BREAD_SLICE.get()))
        );
    }

    protected static LootPoolSingletonContainer.Builder<?> sandwich() {
        return item(SANDWICH.get());
    }

    protected static LootPoolSingletonContainer.Builder<?> potion(Holder<Potion> potion) {
        return LootTables.item(Items.POTION).apply(SetPotionFunction.setPotion(potion));
    }

    protected static LootPoolSingletonContainer.Builder<?> whenLoaded(Item item, int weight) {
        return OptionalLootItem.whenLoaded(item).setWeight(weight);
    }

    protected static LootPoolSingletonContainer.Builder<?> whenNotLoaded(Item item, int weight, String modid) {
        return OptionalLootItem.optionalLootItem(BuiltInRegistries.ITEM.getKey(item), new NotCondition(new ModLoadedCondition(modid))).setWeight(weight);
    }


    protected static LootPoolSingletonContainer.Builder<?> item(Item item) {
        return item(item, 1);
    }

    protected static LootPoolSingletonContainer.Builder<?> item(Item item, int weight) {
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
            Preconditions.checkArgument(existingFileHelper.exists(ResourceLocation.parse("loot_table/" + actualLocation + ".json"), PackType.SERVER_DATA), "Loot table %s does not exist in any known data pack", actualLocation);
        }
        lootTables.add(new SubProviderEntry(registries -> lootBuilder -> lootBuilder.accept(SomeAssemblyRequired.key(Registries.LOOT_TABLE, location), lootTable), lootParameterSet));
    }

    private void addLootTableUnchecked(String location, LootTable.Builder lootTable, LootContextParamSet lootParameterSet) {
        lootTables.add(new SubProviderEntry(registries -> lootBuilder -> lootBuilder.accept(SomeAssemblyRequired.key(Registries.LOOT_TABLE, location), lootTable), lootParameterSet));
    }
}
