package someassemblyrequired.common.ingredient;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModItems;

public class MilkBucketIngredient implements SandwichIngredient {

    private static final ItemStack CONTAINER = new ItemStack(Items.BUCKET);
    private static final ItemStack DISPLAY_ITEM;
    private static final Component NAME = new TranslatableComponent("ingredient.%s.milk_bucket".formatted(SomeAssemblyRequired.MODID));

    static {
        DISPLAY_ITEM = new ItemStack(ModItems.SPREAD.get());
        DISPLAY_ITEM.getOrCreateTag().putInt("Color", 0xEEFDFF);
    }

    @Override
    public ItemStack getDisplayItem(ItemStack item) {
        return DISPLAY_ITEM;
    }

    @Override
    public ItemStack getContainer(ItemStack item) {
        return CONTAINER;
    }

    @Override
    public Component getFullName(ItemStack item) {
        return NAME;
    }

    @Override
    public void onEaten(ItemStack item, LivingEntity entity) {
        if (!entity.getLevel().isClientSide) {
            entity.curePotionEffects(item);
        }
    }
}
