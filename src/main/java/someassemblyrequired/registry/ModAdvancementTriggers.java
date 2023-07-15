package someassemblyrequired.registry;

import net.minecraft.advancements.CriteriaTriggers;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.advancement.ItemTrigger;

public class ModAdvancementTriggers {

    public static final ItemTrigger CONSUME_POTION_SANDWICH = new ItemTrigger(SomeAssemblyRequired.id("consume_potion_sandwich"));
    public static final ItemTrigger CONSUME_DOUBLE_DECKER_SANDWICH = new ItemTrigger(SomeAssemblyRequired.id("consume_double_decker_sandwich"));

    public static void register() {
        CriteriaTriggers.register(CONSUME_POTION_SANDWICH);
        CriteriaTriggers.register(CONSUME_DOUBLE_DECKER_SANDWICH);
    }
}
