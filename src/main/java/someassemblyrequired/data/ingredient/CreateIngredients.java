package someassemblyrequired.data.ingredient;

import com.simibubi.create.AllItems;
import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.Set;

public class CreateIngredients {

    private static final Set<IngredientBuilder> ingredients = new HashSet<>();

    public static Set<IngredientBuilder> createIngredients() {
        ingredients.clear();

        builder(AllItems.BUILDERS_TEA.get()).setBottled().setSpread(0xa54f34);

        return ingredients;
    }

    private static IngredientBuilder builder(Item item) {
        IngredientBuilder builder = new IngredientBuilder(item);
        ingredients.add(builder);
        return builder;
    }
}
