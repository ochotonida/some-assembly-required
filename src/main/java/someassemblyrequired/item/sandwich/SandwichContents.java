package someassemblyrequired.item.sandwich;

import com.mojang.serialization.Codec;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import someassemblyrequired.config.ModConfig;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.registry.ModDataComponents;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModTags;

import java.util.*;
import java.util.function.Function;

public final class SandwichContents extends AbstractList<ItemStack> {

    public static final SandwichContents EMPTY = new SandwichContents(List.of());

    public static final Codec<SandwichContents> CODEC = ItemStack.CODEC.listOf(0, 256).xmap(SandwichContents::new, Function.identity());
    public static final StreamCodec<RegistryFriendlyByteBuf, SandwichContents> STREAM_CODEC = ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()).map(SandwichContents::new, Function.identity());
    private final List<ItemStack> items;

    public SandwichContents(List<ItemStack> items) {
        this.items = items.stream().map(ItemStack::copy).toList();
    }

    public static SandwichContents get(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.SANDWICH_CONTENTS, EMPTY);
    }

    public static Optional<SandwichContents> maybeGet(ItemStack stack) {
        return Optional.ofNullable(stack.get(ModDataComponents.SANDWICH_CONTENTS));
    }

    public SandwichContents dropLast() {
        List<ItemStack> items = new ArrayList<>(this);
        items.removeLast();
        return new SandwichContents(items);
    }

    public SandwichContents concat(List<ItemStack> toAdd) {
        if (toAdd.stream().anyMatch(ItemStack::isEmpty)) {
            throw new IllegalArgumentException("Cannot add empty item to sandwich");
        }
        List<ItemStack> items = new ArrayList<>(this);
        toAdd.stream()
                .map(ItemStack::copy)
                .peek(stack -> stack.setCount(1))
                .forEach(items::add);
        return new SandwichContents(items);
    }

    public int getTotalHeight() {
        int size = 0;
        for (ItemStack item : this) {
            size += Ingredients.getHeight(item);
        }
        return size;
    }

    public int nutrition(@Nullable LivingEntity entity) {
        int result = 0;
        for (ItemStack stack : this) {
            result += Ingredients.getFood(stack, entity).nutrition();
        }
        return result;
    }

    public float saturation(@Nullable LivingEntity entity) {
        float result = 0;
        for (ItemStack stack : this) {
            result += Ingredients.getFood(stack, entity).saturation();
        }
        return result;
    }

    public boolean isBurger() {
        int bunCount = countItems(ModTags.BURGER_BUNS);
        int breadCount = countItems(ModTags.SANDWICH_BREAD) - bunCount;

        return bunCount > breadCount;
    }

    public boolean hasTopAndBottomBread() {
        return !isEmpty()
                && getFirst().is(ModTags.SANDWICH_BREAD)
                && getLast().is(ModTags.SANDWICH_BREAD);
    }

    public boolean isDoubleDecker() {
        if (size() < 5 || !hasTopAndBottomBread()
                || get(1).is(ModTags.SANDWICH_BREAD)
                || get(size() - 2).is(ModTags.SANDWICH_BREAD)
        ) {
            return false;
        }

        int breadCount = 0;
        for (ItemStack item : this) {
            if (item.is(ModTags.SANDWICH_BREAD)) {
                breadCount++;
            }
        }
        return breadCount == 3;
    }

    public FoodProperties createFoodProperties(@Nullable LivingEntity entity) {
        int nutrition = nutrition(entity);
        float saturationModifier = 0.5F * saturation(entity) / nutrition;
        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturationModifier);

        Set<Item> uniqueIngredients = new HashSet<>();
        Map<MobEffectInstance, Float> effects = new LinkedHashMap<>();
        for (ItemStack stack : this) {
            FoodProperties food = Ingredients.getFood(stack, entity);
            if (food.nutrition() > 0 && food.effects().isEmpty() && !stack.is(ModTags.SANDWICH_BREAD)) {
                uniqueIngredients.add(stack.getItem());
            }
        }
        if (ModConfig.serverSpec.isLoaded()) {
            addBonusEffect(effects, uniqueIngredients.size());
        }
        for (ItemStack stack : this) {
            for (FoodProperties.PossibleEffect effect : Ingredients.getFood(stack, entity).effects()) {
                effects.computeIfPresent(effect.effect(), (i, f) -> 1 - (1 - f) * (1 - effect.probability()));
                effects.putIfAbsent(effect.effect(), effect.probability());
            }
        }
        effects.forEach((instance, p) -> builder.effect(() -> new MobEffectInstance(instance), p));

        return builder.build();
    }

    private void addBonusEffect(Map<MobEffectInstance, Float> effects, int uniqueIngredientCount) {
        String effectName = isBurger() ? ModConfig.server.burgerBonusEffect.get() : ModConfig.server.sandwichBonusEffect.get();
        List<Integer> durations = isBurger() ? ModConfig.server.burgerEffectDurations.get() : ModConfig.server.sandwichEffectDurations.get();
        uniqueIngredientCount = Math.min(durations.size() - 1, uniqueIngredientCount);
        ResourceLocation effectId;
        try {
            effectId = ResourceLocation.parse(effectName);
        } catch (ResourceLocationException e) {
            return;
        }
        Optional<? extends Holder<MobEffect>> effect = BuiltInRegistries.MOB_EFFECT.getHolder(effectId);
        if (effect.isPresent() && !durations.isEmpty()) {
            int duration = durations.get(uniqueIngredientCount);
            if (duration > 0) {
                effects.put(new MobEffectInstance(effect.get(), duration * 20, 0), 1F);
            }
        }
    }

    public ItemStack makeItem() {
        if (isEmpty()) {
            return ItemStack.EMPTY;
        } else if (size() == 1) {
            return getFirst().copy();
        } else {
            ItemStack result = new ItemStack(ModItems.SANDWICH.get());
            result.set(ModDataComponents.SANDWICH_CONTENTS.get(), this);
            return result;
        }
    }

    public int countItems(TagKey<Item> tagKey) {
        int result = 0;
        for (ItemStack ingredient : this) {
            if (ingredient.is(tagKey)) {
                result++;
            }
        }
        return result;
    }

    @Override
    public ItemStack get(int index) {
        return items.get(index);
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SandwichContents contents)) return false;

        return ItemStack.listMatches(this, contents);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int hashCode() {
        return ItemStack.hashStackList(this);
    }
}
