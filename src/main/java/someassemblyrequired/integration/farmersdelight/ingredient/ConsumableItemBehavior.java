package someassemblyrequired.integration.farmersdelight.ingredient;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.ingredient.behavior.IngredientBehavior;
import vectorwing.farmersdelight.common.item.ConsumableItem;

public record ConsumableItemBehavior(ConsumableItem item) implements IngredientBehavior {

    @Override
    public void onEaten(ItemStack ingredient, LivingEntity entity) {
        if (!entity.getLevel().isClientSide()) {
            item.affectConsumer(ingredient, entity.getLevel(), entity);
        }
    }
}
