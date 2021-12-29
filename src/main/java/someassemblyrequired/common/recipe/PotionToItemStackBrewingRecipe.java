package someassemblyrequired.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;

public class PotionToItemStackBrewingRecipe extends BrewingRecipe {

    private final Potion inputPotion;

    public PotionToItemStackBrewingRecipe(Potion inputPotion, Ingredient ingredient, ItemStack result) {
        super(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), inputPotion)), ingredient, result);
        this.inputPotion = inputPotion;
    }

    @Override
    public boolean isInput(ItemStack input) {
        if (input.getItem() != Items.POTION) {
            return false;
        }
        return PotionUtils.getPotion(input) == inputPotion;
    }
}
