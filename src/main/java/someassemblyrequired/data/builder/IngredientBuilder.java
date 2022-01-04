package someassemblyrequired.data.builder;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.ingredient.DataIngredient;
import someassemblyrequired.common.init.ModItems;

import javax.annotation.Nullable;

@SuppressWarnings("UnusedReturnValue")
public class IngredientBuilder {

    private final Item item;

    @Nullable
    private Component displayName;
    @Nullable
    private Component fullName;

    private ItemStack displayItem = ItemStack.EMPTY;
    private ItemStack container = ItemStack.EMPTY;

    public IngredientBuilder(Item item) {
        this.item = item;
    }

    public DataIngredient build() {
        return new DataIngredient(null, displayName, fullName, displayItem, container, null);
    }

    public Item getItem() {
        return item;
    }

    public IngredientBuilder setDisplayName(Component displayName) {
        this.displayName = displayName;
        return this;
    }

    public IngredientBuilder setDisplayName(String translationKey) {
        return setDisplayName(new TranslatableComponent(translationKey));
    }

    public IngredientBuilder setCustomDisplayName() {
        return setDisplayName(getDefaultTranslationKey(item));
    }

    public IngredientBuilder setFullName(Component fullName) {
        this.fullName = fullName;
        return this;
    }

    public IngredientBuilder setFullName(String translationKey) {
        return setFullName(new TranslatableComponent(translationKey));
    }

    public IngredientBuilder setCustomFullName() {
        return setFullName(getDefaultTranslationKey(getItem()));
    }

    public IngredientBuilder setDisplayItem(ItemStack displayItem) {
        this.displayItem = displayItem;
        return this;
    }

    public IngredientBuilder setCustomModelData() {
        ItemStack display = new ItemStack(item);
        display.getOrCreateTag().putInt("CustomModelData", 1);
        return setDisplayItem(display);
    }

    public IngredientBuilder setSpread(int color) {
        ItemStack spread = new ItemStack(ModItems.SPREAD.get());
        spread.getOrCreateTag().putInt("Color", color);
        return setDisplayItem(spread);
    }

    public IngredientBuilder setContainer(ItemStack container) {
        this.container = container;
        return this;
    }

    public IngredientBuilder setContainer(Item item) {
        return setContainer(new ItemStack(item));
    }

    public IngredientBuilder setBottled() {
        return setContainer(Items.GLASS_BOTTLE);
    }

    public IngredientBuilder setBowled() {
        return setContainer(Items.BOWL);
    }

    public IngredientBuilder setBucketed() {
        return setContainer(Items.BUCKET);
    }

    private static String getDefaultTranslationKey(Item item) {
        ResourceLocation id = item.getRegistryName();
        // noinspection ConstantConditions
        if ("minecraft".equals(id.getNamespace()) || SomeAssemblyRequired.MODID.equals(id.getNamespace())) {
            return "ingredient.%s.%s".formatted(SomeAssemblyRequired.MODID, id.getPath());
        } else {
            return "ingredient.%s.%s.%s".formatted(SomeAssemblyRequired.MODID, id.getNamespace(), id.getPath());
        }
    }
}
