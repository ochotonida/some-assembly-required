package someassemblyrequired.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipe;

public class PotionToItemStackBrewingRecipe extends BrewingRecipe {

    private final Potion inputPotion;

    public PotionToItemStackBrewingRecipe(Potion inputPotion, Ingredient ingredient, ItemStack result) {
        super(Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), inputPotion)), ingredient, result);
        this.inputPotion = inputPotion;
    }

    @Override
    public boolean isInput(ItemStack input) {
        if (input.getItem() != Items.POTION) {
            return false;
        }
        return PotionUtils.getPotionFromItem(input) == inputPotion;
    }
}
