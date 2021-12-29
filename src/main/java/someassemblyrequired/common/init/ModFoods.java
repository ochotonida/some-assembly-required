package someassemblyrequired.common.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {

    public static final FoodProperties SANDWICH = new FoodProperties.Builder()
            .alwaysEat()
            .build();

    public static final FoodProperties CHARRED_FOOD = new FoodProperties.Builder()
            .nutrition(1)
            .saturationMod(0.1F)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.4F)
            .build();

    public static final FoodProperties CHARRED_MORSEL = new FoodProperties.Builder()
            .nutrition(1)
            .saturationMod(0)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.2F)
            .build();

    public static final FoodProperties BREAD_SLICE = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.5F)
            .fast()
            .build();

    public static final FoodProperties TOASTED_BREAD_SLICE = new FoodProperties.Builder()
            .nutrition(3)
            .saturationMod(0.6F)
            .fast().build();

    public static final FoodProperties APPLE_SLICES = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.5F)
            .fast()
            .build();

    public static final FoodProperties GOLDEN_APPLE_SLICES = new FoodProperties.Builder()
            .nutrition(3).saturationMod(0.7F).fast()
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 50, 1), 1)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 1200, 0), 1)
            .build();

    public static final FoodProperties ENCHANTED_GOLDEN_APPLE_SLICES = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.8F)
            .fast()
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), 1)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3000, 0), 1)
            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3000, 0), 1)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 1200, 3), 1)
            .build();

    public static final FoodProperties CHOPPED_CARROT = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.5F)
            .fast()
            .build();

    public static final FoodProperties CHOPPED_GOLDEN_CARROT = new FoodProperties.Builder()
            .nutrition(3)
            .saturationMod(0.8F)
            .fast()
            .build();

    public static final FoodProperties CHOPPED_BEETROOT = new FoodProperties.Builder()
            .nutrition(3)
            .saturationMod(0.6F)
            .fast()
            .build();

    public static final FoodProperties MAYONNAISE = new FoodProperties.Builder()
            .nutrition(3)
            .saturationMod(0.1F)
            .build();

    public static final FoodProperties KETCHUP = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.2F)
            .build();

    public static final FoodProperties SWEET_BERRY_JAM = new FoodProperties.Builder()
            .nutrition(5)
            .saturationMod(0.5F)
            .build();

    public static final FoodProperties PORK_CUTS = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.3F)
            .meat()
            .build();

    public static final FoodProperties BACON_STRIPS = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.7F)
            .meat()
            .build();

    public static final FoodProperties TOASTED_CRIMSON_FUNGUS = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.5F)
            .build();

    public static final FoodProperties SLICED_TOASTED_CRIMSON_FUNGUS = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.6F)
            .fast()
            .build();

    public static final FoodProperties TOASTED_WARPED_FUNGUS = new FoodProperties.Builder()
            .nutrition(5)
            .saturationMod(0.4F)
            .build();

    public static final FoodProperties SLICED_TOASTED_WARPED_FUNGUS = new FoodProperties.Builder()
            .nutrition(3)
            .saturationMod(0.5F)
            .fast()
            .build();

    public static final FoodProperties TOMATO = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.4F)
            .build();

    public static final FoodProperties TOMATO_SLICES = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.6F)
            .fast()
            .build();

    public static final FoodProperties LETTUCE_HEAD = new FoodProperties.Builder()
            .nutrition(5)
            .saturationMod(0.2F)
            .build();

    public static final FoodProperties LETTUCE_LEAF = new FoodProperties.Builder()
            .nutrition(3)
            .saturationMod(0.4F)
            .build();
}
