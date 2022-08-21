package someassemblyrequired.common.init;

import net.minecraft.advancements.CriteriaTriggers;
import someassemblyrequired.common.advancement.ItemTrigger;
import someassemblyrequired.common.util.Util;

public class ModAdvancementTriggers {

    public static final ItemTrigger CONSUME_POTION_SANDWICH = new ItemTrigger(Util.id("add_potion_to_sandwich"));
    public static final ItemTrigger CONSUME_DOUBLE_DECKER_SANDWICH = new ItemTrigger(Util.id("consume_double_decker_sandwich"));

    public static void register() {
        CriteriaTriggers.register(CONSUME_POTION_SANDWICH);
        CriteriaTriggers.register(CONSUME_DOUBLE_DECKER_SANDWICH);
    }
}
