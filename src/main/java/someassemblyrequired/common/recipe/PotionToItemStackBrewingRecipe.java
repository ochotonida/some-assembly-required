package someassemblyrequired.common.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.brewing.BrewingRecipe;

// need to extend BrewingRecipe so JEI knows what it's supposed to do
public class PotionToItemStackBrewingRecipe extends BrewingRecipe {

    private final Potion inputPotion;
    // BrewingRecipeRegistry doesnt seem to like item tag ingredients for some reason
    private final ITag<Item> ingredient;

    public PotionToItemStackBrewingRecipe(Potion inputPotion, ITag<Item> ingredient, ItemStack result) {
        super(Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), inputPotion)), Ingredient.EMPTY, result);
        this.inputPotion = inputPotion;
        this.ingredient = ingredient;
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
        return this.ingredient.contains(ingredient.getItem());
    }

    @Override
    public Ingredient getIngredient() {
        // override this to return the actual ingredient
        return Ingredient.fromTag(ingredient);
    }
}
