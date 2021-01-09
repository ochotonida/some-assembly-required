package someasseblyrequired.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class PotionToItemStackBrewingRecipe implements IBrewingRecipe {

    private final Potion inputPotion;
    private final Ingredient ingredient;
    private final ItemStack result;

    public PotionToItemStackBrewingRecipe(Potion inputPotion, Ingredient ingredient, ItemStack result) {
        this.inputPotion = inputPotion;
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean isInput(ItemStack input) {
        if (input.getItem() != Items.POTION) {
            return false;
        }
        return PotionUtils.getPotionFromItem(input) == inputPotion;
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return this.ingredient.test(ingredient);
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        return isInput(input) && isIngredient(ingredient) ? result.copy() : ItemStack.EMPTY;
    }
}
