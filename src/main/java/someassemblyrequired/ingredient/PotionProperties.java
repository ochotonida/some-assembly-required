package someassemblyrequired.ingredient;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.registry.ModDataComponents;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModSoundEvents;

import java.util.Optional;

public class PotionProperties implements IngredientPropertiesBase {

    private final ItemStack displayItem;

    public PotionProperties() {
        displayItem = new ItemStack(ModItems.SPREAD.get());
    }

    @Override
    public FoodProperties getFood(ItemStack item, LivingEntity entity) {
        FoodProperties.Builder builder = new FoodProperties.Builder();

        PotionContents contents = item.get(DataComponents.POTION_CONTENTS);
        if (contents != null) {
            for (MobEffectInstance mobEffect : contents.getAllEffects()) {
                builder.effect(() -> new MobEffectInstance(mobEffect), 1);
            }
        }
        builder.usingConvertsTo(Items.GLASS_BOTTLE);

        return builder.build();
    }

    @Override
    public ItemStack getDisplayItem(ItemStack item) {
        PotionContents contents = item.get(DataComponents.POTION_CONTENTS);
        if (contents != null) {
            displayItem.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, !contents.is(Potions.WATER));
            displayItem.set(ModDataComponents.SPREAD_COLOR, (contents.getColor() & 0xFFFFFF) | 0xCC000000);
        }
        return displayItem;
    }

    @Override
    public Component getDisplayName(ItemStack item) {
        PotionContents contents = item.get(DataComponents.POTION_CONTENTS);
        if (contents != null && !item.has(DataComponents.CUSTOM_NAME) && contents.is(Potions.WATER)) {
            return SomeAssemblyRequired.translate("ingredient.water_bottle");
        }
        return IngredientPropertiesBase.super.getDisplayName(item);
    }

    @Override
    public Component getFullName(ItemStack item) {
        PotionContents contents = item.get(DataComponents.POTION_CONTENTS);
        if (contents != null && !item.has(DataComponents.CUSTOM_NAME) && contents.is(Potions.WATER)) {
            return getDisplayName(item);
        }
        return IngredientPropertiesBase.super.getFullName(item);
    }

    @Override
    public Optional<FoodProperties> food() {
        return Optional.empty();
    }

    @Override
    public Optional<Component> displayName() {
        return Optional.empty();
    }

    @Override
    public Optional<Component> fullName() {
        return Optional.empty();
    }

    @Override
    public ItemStack displayItem() {
        return displayItem;
    }

    @Override
    public boolean renderAsItem() {
        return true;
    }

    @Override
    public Holder<SoundEvent> sound() {
        return ModSoundEvents.ADD_ITEM_MOIST;
    }
}
