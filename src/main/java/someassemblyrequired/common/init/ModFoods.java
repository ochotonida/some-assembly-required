package someassemblyrequired.common.init;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ModFoods {

    public static final Food SANDWICH = new Food.Builder()
            .setAlwaysEdible()
            .build();

    public static final Food CHARRED_FOOD = new Food.Builder()
            .hunger(1)
            .saturation(0.1F)
            .effect(() -> new EffectInstance(Effects.NAUSEA, 100, 0), 0.4F)
            .build();

    public static final Food CHARRED_MORSEL = new Food.Builder()
            .hunger(1)
            .saturation(0)
            .effect(() -> new EffectInstance(Effects.NAUSEA, 100, 0), 0.2F)
            .build();

    public static final Food BREAD_SLICE = new Food.Builder()
            .hunger(2)
            .saturation(0.5F)
            .fastToEat()
            .build();

    public static final Food TOASTED_BREAD_SLICE = new Food.Builder()
            .hunger(3)
            .saturation(0.6F)
            .fastToEat().build();

    public static final Food APPLE_SLICES = new Food.Builder()
            .hunger(2)
            .saturation(0.5F)
            .fastToEat()
            .build();

    public static final Food GOLDEN_APPLE_SLICES = new Food.Builder()
            .hunger(3).saturation(0.7F).fastToEat()
            .setAlwaysEdible()
            .effect(() -> new EffectInstance(Effects.REGENERATION, 50, 1), 1)
            .effect(() -> new EffectInstance(Effects.ABSORPTION, 1200, 0), 1)
            .build();

    public static final Food ENCHANTED_GOLDEN_APPLE_SLICES = new Food.Builder()
            .hunger(4)
            .saturation(0.8F)
            .fastToEat()
            .setAlwaysEdible()
            .effect(() -> new EffectInstance(Effects.REGENERATION, 200, 1), 1)
            .effect(() -> new EffectInstance(Effects.RESISTANCE, 3000, 0), 1)
            .effect(() -> new EffectInstance(Effects.FIRE_RESISTANCE, 3000, 0), 1)
            .effect(() -> new EffectInstance(Effects.ABSORPTION, 1200, 3), 1)
            .build();

    public static final Food CHOPPED_CARROT = new Food.Builder()
            .hunger(2)
            .saturation(0.5F)
            .fastToEat()
            .build();

    public static final Food CHOPPED_GOLDEN_CARROT = new Food.Builder()
            .hunger(3)
            .saturation(0.8F)
            .fastToEat()
            .build();

    public static final Food CHOPPED_BEETROOT = new Food.Builder()
            .hunger(3)
            .saturation(0.6F)
            .fastToEat()
            .build();

    public static final Food MAYONNAISE = new Food.Builder()
            .hunger(3)
            .saturation(0.1F)
            .build();

    public static final Food KETCHUP = new Food.Builder()
            .hunger(2)
            .saturation(0.2F)
            .build();

    public static final Food SWEET_BERRY_JAM = new Food.Builder()
            .hunger(5)
            .saturation(0.5F)
            .build();

    public static final Food PORK_CUTS = new Food.Builder()
            .hunger(2)
            .saturation(0.3F)
            .meat()
            .build();

    public static final Food BACON_STRIPS = new Food.Builder()
            .hunger(4)
            .saturation(0.7F)
            .meat()
            .build();

    public static final Food TOASTED_CRIMSON_FUNGUS = new Food.Builder()
            .hunger(4)
            .saturation(0.5F)
            .build();

    public static final Food SLICED_TOASTED_CRIMSON_FUNGUS = new Food.Builder()
            .hunger(2)
            .saturation(0.6F)
            .fastToEat()
            .build();

    public static final Food TOASTED_WARPED_FUNGUS = new Food.Builder()
            .hunger(5)
            .saturation(0.4F)
            .build();

    public static final Food SLICED_TOASTED_WARPED_FUNGUS = new Food.Builder()
            .hunger(3)
            .saturation(0.5F)
            .fastToEat()
            .build();

    public static final Food TOMATO = new Food.Builder()
            .hunger(4)
            .saturation(0.4F)
            .build();

    public static final Food TOMATO_SLICES = new Food.Builder()
            .hunger(2)
            .saturation(0.6F)
            .fastToEat()
            .build();

    public static final Food LETTUCE_HEAD = new Food.Builder()
            .hunger(5)
            .saturation(0.2F)
            .build();

    public static final Food LETTUCE_LEAF = new Food.Builder()
            .hunger(3)
            .saturation(0.4F)
            .build();
}
