package someassemblyrequired.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.SetStewEffectFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.init.ModItems;
import someassemblyrequired.loot.RollLootTableModifier;
import someassemblyrequired.loot.SandwichLootEnabledCondition;
import someassemblyrequired.util.Util;

import java.util.ArrayList;
import java.util.List;

public class LootModifiers extends GlobalLootModifierProvider {

    protected final List<Builder> lootBuilders = new ArrayList<>();

    public LootModifiers(DataGenerator generator) {
        super(generator, SomeAssemblyRequired.MODID);
    }

    private void addLoot() {
        addSandwich(BuiltInLootTables.UNDERWATER_RUIN_BIG, Potions.WATER);
        addSandwich(BuiltInLootTables.BURIED_TREASURE, Potions.WATER_BREATHING);
        addSandwich(BuiltInLootTables.END_CITY_TREASURE, Items.CHORUS_FRUIT);
        addSandwich(BuiltInLootTables.ABANDONED_MINESHAFT, Potions.NIGHT_VISION);
        addSandwich(BuiltInLootTables.SIMPLE_DUNGEON, ModItems.GOLDEN_APPLE_SLICES.get());
        addSandwich(BuiltInLootTables.NETHER_BRIDGE, Potions.FIRE_RESISTANCE);
        addSandwich(BuiltInLootTables.ANCIENT_CITY, Potions.REGENERATION);
        builder(BuiltInLootTables.SHIPWRECK_SUPPLY, 0.05).getLootPool().add(LootTables.sandwich(
                LootTables.item(Items.SUSPICIOUS_STEW).apply(SetStewEffectFunction.stewEffect()
                        .withEffect(MobEffects.NIGHT_VISION, UniformGenerator.between(7, 10))
                        .withEffect(MobEffects.JUMP, UniformGenerator.between(7, 10))
                        .withEffect(MobEffects.WEAKNESS, UniformGenerator.between(6, 8))
                        .withEffect(MobEffects.BLINDNESS, UniformGenerator.between(5, 7))
                        .withEffect(MobEffects.POISON, UniformGenerator.between(10, 20))
                        .withEffect(MobEffects.SATURATION, UniformGenerator.between(7, 10))
                )
        ));
        builder(BuiltInLootTables.SPAWN_BONUS_CHEST, 1).getLootPool().add(
                LootTables.sandwich(Items.HONEY_BOTTLE).apply(LootTables.count(3, 8))
        );
    }

    private void addSandwich(ResourceLocation lootTable, Item item) {
        builder(lootTable, 0.05).getLootPool().add(LootTables.sandwich(item));
    }

    private void addSandwich(ResourceLocation lootTable, Potion potion) {
        builder(lootTable, 0.05).getLootPool().add(LootTables.sandwich(potion));
    }

    protected Builder builder(ResourceLocation lootTable, double chance) {
        Builder builder = new Builder(lootTable.getPath());
        builder.lootModifierCondition(LootTableIdCondition.builder(lootTable).build());
        builder.lootModifierCondition(SandwichLootEnabledCondition.sandwichLootEnabled());
        if (chance != 1) {
            builder.lootPoolCondition(LootItemRandomChanceCondition.randomChance((float) chance));
        }

        lootBuilders.add(builder);
        return builder;
    }

    @Override
    protected void start() {
        addLoot();

        for (Builder lootBuilder : lootBuilders) {
            add(lootBuilder.getName(), lootBuilder.build());
        }

        for (ResourceLocation lootTable : List.of(
                BuiltInLootTables.VILLAGE_DESERT_HOUSE,
                BuiltInLootTables.VILLAGE_SAVANNA_HOUSE,
                BuiltInLootTables.VILLAGE_PLAINS_HOUSE,
                BuiltInLootTables.VILLAGE_TAIGA_HOUSE,
                BuiltInLootTables.VILLAGE_SNOWY_HOUSE
        )) {
            List<LootItemCondition> conditions = new ArrayList<>();
            conditions.add(SandwichLootEnabledCondition.sandwichLootEnabled());
            conditions.add(LootTableIdCondition.builder(lootTable).build());
            add("inject/" + lootTable.getPath(), new RollLootTableModifier(conditions.toArray(new LootItemCondition[]{}), LootTables.VILLAGE_SANDWICH));
        }
    }

    @SuppressWarnings({"UnusedReturnValue", "unused"})
    protected static class Builder {

        private final String lootTableName;
        private final LootPool.Builder lootPool = LootPool.lootPool();
        private final List<LootItemCondition> conditions;

        private LootContextParamSet paramSet = LootContextParamSets.CHEST;

        private Builder(String lootTableName) {
            this.lootTableName = "inject/" + lootTableName;
            this.conditions = new ArrayList<>();
        }

        private RollLootTableModifier build() {
            return new RollLootTableModifier(conditions.toArray(new LootItemCondition[]{}), Util.id(getName()));
        }

        protected LootTable.Builder createLootTable() {
            return new LootTable.Builder().withPool(lootPool);
        }

        public LootContextParamSet getParameterSet() {
            return paramSet;
        }

        public LootPool.Builder getLootPool() {
            return lootPool;
        }

        protected String getName() {
            return lootTableName;
        }

        private Builder parameterSet(LootContextParamSet paramSet) {
            this.paramSet = paramSet;
            return this;
        }

        private Builder lootPoolCondition(LootItemCondition.Builder condition) {
            lootPool.when(condition);
            return this;
        }

        private Builder lootModifierCondition(LootItemCondition condition) {
            conditions.add(condition);
            return this;
        }
    }
}
