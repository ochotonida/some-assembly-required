package someassemblyrequired.common.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import someassemblyrequired.common.init.ModItems;

public class SandwichBuilder {

    private final Item bread;
    private final boolean isLoaf;
    private final NonNullList<ItemStack> ingredients = NonNullList.create();

    private SandwichBuilder(Item bread, boolean isLoaf) {
        this.bread = bread;
        this.isLoaf = isLoaf;
    }

    public static SandwichBuilder create() {
        return create(ModItems.BREAD_SLICE.get(), false).addBread();
    }

    public static SandwichBuilder create(IItemProvider bread, boolean isLoaf) {
        return new SandwichBuilder(bread.asItem(), isLoaf);
    }

    public SandwichBuilder addBread() {
        return add(bread);
    }

    public SandwichBuilder add(IItemProvider item) {
        return addStack(new ItemStack(item));
    }

    public SandwichBuilder addStack(ItemStack ingredient) {
        CompoundNBT tag = ingredient.getOrCreateTag();

        if (isLoaf && ingredient.getItem() != bread) {
            tag.putBoolean("IsOnLoaf", true);
        }

        ingredient.getOrCreateTag().putBoolean("IsOnSandwich", true);

        ingredients.add(ingredient);
        return this;
    }

    public ItemStack build() {
        addBread();
        ItemStack sandwich = new ItemStack(ModItems.SANDWICH.get());
        sandwich.getOrCreateChildTag("BlockEntityTag").put("Ingredients", new ItemStackHandler(ingredients).serializeNBT());
        return sandwich;
    }

    public static ItemStack blt() {
        return create()
                .add(ModItems.LETTUCE_LEAF.get())
                .add(ModItems.BACON_STRIPS.get())
                .add(ModItems.TOMATO_SLICES.get())
                .build();
    }
}
