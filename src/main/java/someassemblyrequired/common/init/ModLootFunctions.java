package someassemblyrequired.common.init;

import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import someassemblyrequired.common.loot.CreateSandwichFunction;
import someassemblyrequired.common.util.Util;

public class ModLootFunctions {

    public static final LootItemFunctionType CREATE_SANDWICH = new LootItemFunctionType(new CreateSandwichFunction.Serializer());

    public static void register() {
        Registry.register(Registry.LOOT_FUNCTION_TYPE, Util.id("create_sandwich"), CREATE_SANDWICH);
    }
}
