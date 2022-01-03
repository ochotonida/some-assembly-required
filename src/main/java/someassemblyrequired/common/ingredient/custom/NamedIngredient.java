package someassemblyrequired.common.ingredient.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.ingredient.SandwichIngredient;

public record NamedIngredient(Component displayName) implements SandwichIngredient {

    public static NamedIngredient fromItem(Item item) {
        // noinspection ConstantConditions
        return new NamedIngredient(new TranslatableComponent("ingredient.%s.%s".formatted(SomeAssemblyRequired.MODID, item.getRegistryName().getPath())));
    }

    @Override
    public Component getDisplayName(ItemStack item) {
        return displayName;
    }
}
