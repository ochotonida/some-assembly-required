package someassemblyrequired.common.ingredient.behavior;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.ingredient.IngredientProperties;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModSoundEvents;

public class PotionProperties extends IngredientProperties {

    private static final ItemStack CONTAINER = new ItemStack(Items.GLASS_BOTTLE);

    private final ItemStack displayItem;

    public PotionProperties() {
        super(null, null, null, ItemStack.EMPTY, ItemStack.EMPTY, ModSoundEvents.ADD_SPREAD.get());
        displayItem = new ItemStack(ModItems.SPREAD.get());
        displayItem.getOrCreateTag();
    }

    @Override
    public FoodProperties getFood(ItemStack item) {
        return null;
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
        return super.getDisplayName(item);
    }

    @Override
    public ItemStack getContainer(ItemStack item) {
        return CONTAINER;
    }
}
