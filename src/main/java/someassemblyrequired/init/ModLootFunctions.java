package someassemblyrequired.init;

import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.loot.SetIngredientsFunction;
import someassemblyrequired.loot.SmeltMatchingItemFunction;

public class ModLootFunctions {

    public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTION_TYPES = DeferredRegister.create(Registry.LOOT_FUNCTION_REGISTRY, SomeAssemblyRequired.MODID);

    public static final RegistryObject<LootItemFunctionType> SET_INGREDIENTS = LOOT_FUNCTION_TYPES.register("set_ingredients", () -> new LootItemFunctionType(new SetIngredientsFunction.Serializer()));
    public static final RegistryObject<LootItemFunctionType> SMELT_MATCHING_ITEM = LOOT_FUNCTION_TYPES.register("smelt_matching_item", () -> new LootItemFunctionType(new SmeltMatchingItemFunction.Serializer()));

}
