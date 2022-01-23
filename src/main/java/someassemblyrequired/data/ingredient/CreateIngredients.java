package someassemblyrequired.data.ingredient;

import com.simibubi.create.AllItems;
import net.minecraft.world.item.Item;

import java.util.Map;

public record CreateIngredients(Map<Item, IngredientBuilder> ingredients) {

    public void addIngredients() {
        builder(AllItems.BUILDERS_TEA.get()).setBottled().setSpread(0xdf8367).setSpreadSound();
    }

    private IngredientBuilder builder(Item item) {
        if (ingredients.containsKey(item)) {
            return ingredients.get(item);
        }
        IngredientBuilder builder = new IngredientBuilder(item);
        ingredients.put(item, builder);
        return builder;
    }
}
