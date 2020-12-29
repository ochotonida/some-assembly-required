package someasseblyrequired.common.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class SingleIngredientRecipe implements IRecipe<IInventory> {

    protected final IRecipeType<?> type;
    protected final ResourceLocation id;
    protected final String group;
    protected final Ingredient input;

    public SingleIngredientRecipe(IRecipeType<?> type, ResourceLocation id, String group, Ingredient input) {
        this.type = type;
        this.id = id;
        this.group = group;
        this.input = input;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(input);
        return ingredients;
    }

    @Override
    public boolean matches(IInventory inventory, World world) {
        if (inventory.isEmpty())
            return false;
        return input.test(inventory.getStackInSlot(0));
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public IRecipeType<?> getType() {
        return type;
    }
}
