package someassemblyrequired.data.ingredient;

import com.simibubi.create.AllItems;
import someassemblyrequired.data.Ingredients;

public class CreateIngredients {

    public static void addIngredients(Ingredients ingredients) {
        ingredients.builder(AllItems.BUILDERS_TEA.get()).setBottled().setSpread(0xdf8367).setSpreadSound();
    }
}
