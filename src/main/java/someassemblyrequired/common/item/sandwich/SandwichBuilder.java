package someassemblyrequired.common.item.sandwich;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.ItemLike;
import someassemblyrequired.common.init.ModItems;

public class SandwichBuilder {

    private final Item bread;
    private final NonNullList<ItemStack> ingredients = NonNullList.create();

    private SandwichBuilder(Item bread) {
        this.bread = bread;
    }

    public static SandwichBuilder builder() {
        return builder(ModItems.BREAD_SLICE.get()).addBread();
    }

    public static SandwichBuilder builder(ItemLike bread) {
        return new SandwichBuilder(bread.asItem());
    }

    public SandwichBuilder addBread() {
        return add(bread);
    }

    public SandwichBuilder add(ItemLike item) {
        return add(new ItemStack(item));
    }

    public SandwichBuilder add(Potion potion) {
        ItemStack item = new ItemStack(Items.POTION);
        PotionUtils.setPotion(item, potion);
        return add(item);
    }

    public SandwichBuilder add(ItemStack ingredient) {
        ingredient.getOrCreateTag().putBoolean("IsOnSandwich", true);
        ingredients.add(ingredient);
        return this;
    }

    public ItemStack build() {
        addBread();
        ItemStack sandwich = new ItemStack(ModItems.SANDWICH.get());
        sandwich.getOrCreateTagElement("BlockEntityTag").put("Sandwich", new SandwichItemHandler(ingredients).serializeNBT());
        return sandwich;
    }
}
