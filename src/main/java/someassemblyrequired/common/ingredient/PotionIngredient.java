package someassemblyrequired.common.ingredient;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModItems;

public class PotionIngredient implements SandwichIngredient {

    private final ItemStack container = new ItemStack(Items.GLASS_BOTTLE);
    private final FoodProperties food = new FoodProperties.Builder().alwaysEat().build();
    private final ItemStack displayItem;

    public PotionIngredient() {
        displayItem = new ItemStack(ModItems.SPREAD.get());
        displayItem.getOrCreateTag();
    }

    @Override
    public FoodProperties getFood(ItemStack item) {
        return food;
    }

    @Override
    public void onEaten(ItemStack item, Player player) {
        for (MobEffectInstance mobEffect : PotionUtils.getMobEffects(item)) {
            if (mobEffect.getEffect().isInstantenous()) {
                mobEffect.getEffect().applyInstantenousEffect(player, player, player, mobEffect.getAmplifier(), 1);
            } else {
                player.addEffect(new MobEffectInstance(mobEffect));
            }
        }
    }

    @Override
    public ItemStack getDisplayItem(ItemStack item) {
        Potion potion = PotionUtils.getPotion(item);
        // noinspection ConstantConditions
        displayItem.getTag().putBoolean("HasEffect", potion != Potions.WATER);
        displayItem.getTag().putInt("Color", PotionUtils.getColor(potion));
        return displayItem;
    }

    @Override
    public Component getDisplayName(ItemStack item) {
        Potion potion = PotionUtils.getPotion(item);
        if (!item.hasCustomHoverName() && potion == Potions.WATER) {
            return new TranslatableComponent("ingredient.%s.water_bottle".formatted(SomeAssemblyRequired.MODID));
        }
        return SandwichIngredient.super.getDisplayName(item);
    }

    @Override
    public ItemStack getContainer(ItemStack item) {
        return container;
    }
}
