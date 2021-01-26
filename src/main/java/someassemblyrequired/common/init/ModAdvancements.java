package someassemblyrequired.common.init;

import net.minecraft.advancements.CriteriaTriggers;
import someassemblyrequired.common.advancement.ItemTrigger;
import someassemblyrequired.common.util.Util;

public class ModAdvancements {

    public static ItemTrigger ADD_POTION_TO_SANDWICH = new ItemTrigger(Util.prefix("add_potion_to_sandwich"));
    public static ItemTrigger CONSUME_DOUBLE_DECKER_SANDWICH = new ItemTrigger(Util.prefix("consume_double_decker_sandwich"));
    public static ItemTrigger CONSUME_BLT_SANDWICH = new ItemTrigger(Util.prefix("consume_blt_sandwich"));

    public static void register() {
        CriteriaTriggers.register(ADD_POTION_TO_SANDWICH);
        CriteriaTriggers.register(CONSUME_DOUBLE_DECKER_SANDWICH);
        CriteriaTriggers.register(CONSUME_BLT_SANDWICH);
    }
}
