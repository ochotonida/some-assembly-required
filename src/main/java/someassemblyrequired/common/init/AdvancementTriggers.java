package someassemblyrequired.common.init;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.advancement.ItemTrigger;

public class AdvancementTriggers {

    public static ItemTrigger ADD_POTION_TO_SANDWICH = new ItemTrigger(new ResourceLocation(SomeAssemblyRequired.MODID, "add_potion_to_sandwich"));
    public static ItemTrigger CONSUME_DOUBLE_DECKER_SANDWICH = new ItemTrigger(new ResourceLocation(SomeAssemblyRequired.MODID, "consume_double_decker_sandwich"));
    public static ItemTrigger CONSUME_BLT_SANDWICH = new ItemTrigger(new ResourceLocation(SomeAssemblyRequired.MODID, "consume_blt_sandwich"));

    public static void register() {
        CriteriaTriggers.register(ADD_POTION_TO_SANDWICH);
        CriteriaTriggers.register(CONSUME_DOUBLE_DECKER_SANDWICH);
        CriteriaTriggers.register(CONSUME_BLT_SANDWICH);
    }
}
