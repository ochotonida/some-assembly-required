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
    private final NonNullList<ItemStack> ingredients = NonNullList.create();

    private SandwichBuilder(Item bread) {
        this.bread = bread;
    }

    public static SandwichBuilder create() {
        return create(ModItems.BREAD_SLICE.get()).addBread();
    }

    public static SandwichBuilder create(IItemProvider bread) {
        return new SandwichBuilder(bread.asItem());
    }

    public SandwichBuilder addBread() {
        return add(bread);
    }

    public SandwichBuilder addFakeSpread(int color) {
        return addFakeSpread(color, false);
    }

    public SandwichBuilder addFakeSpread(int color, boolean hasGlint) {
        ItemStack spread = new ItemStack(ModItems.SPREAD.get());
        CompoundNBT tag = spread.getOrCreateTag();
        tag.putInt("Color", color);
        tag.putBoolean("HasGlint", hasGlint);
        return addStack(spread);
    }

    public SandwichBuilder add(IItemProvider item) {
        return addStack(new ItemStack(item));
    }

    private SandwichBuilder addStack(ItemStack ingredient) {
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
