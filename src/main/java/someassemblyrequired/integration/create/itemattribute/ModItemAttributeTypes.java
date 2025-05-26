package someassemblyrequired.integration.create.itemattribute;

import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import com.simibubi.create.content.logistics.item.filter.attribute.SingletonItemAttribute;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.registry.ModDataComponents;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class ModItemAttributeTypes {

    public static final DeferredRegister<ItemAttributeType> ITEM_ATTRIBUTE_TYPES = DeferredRegister.create(CreateBuiltInRegistries.ITEM_ATTRIBUTE_TYPE, SomeAssemblyRequired.MOD_ID);

    public static final Supplier<ItemAttributeType> OPEN_FACED = sandwichSingleton("open_faced", contents -> !contents.hasTopAndBottomBread());
    public static final Supplier<ItemAttributeType> BURGER = sandwichSingleton("burger", SandwichContents::isBurger);
    public static final Supplier<ItemAttributeType> DOUBLE_DECKER = sandwichSingleton("double_decker", SandwichContents::isDoubleDecker);

    private static Supplier<ItemAttributeType> sandwichSingleton(String id, Predicate<SandwichContents> predicate) {
        return singleton(id, stack -> {
            SandwichContents contents = stack.get(ModDataComponents.SANDWICH_CONTENTS);
            return contents != null && !contents.isEmpty() && predicate.test(contents);
        });
    }

    private static Supplier<ItemAttributeType> singleton(String id, Predicate<ItemStack> predicate) {
        return register(id, new SingletonItemAttribute.Type(type -> new SingletonItemAttribute(type, (stack, level) -> predicate.test(stack), SomeAssemblyRequired.MOD_ID + "." + id)));
    }

    private static Supplier<ItemAttributeType> register(String id, ItemAttributeType type) {
        return ITEM_ATTRIBUTE_TYPES.register(id, () -> type);
    }
}
