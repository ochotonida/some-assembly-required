package someassemblyrequired.data.ingredient;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.ingredient.IngredientProperties;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModSoundEvents;

import javax.annotation.Nullable;

@SuppressWarnings("UnusedReturnValue")
public class IngredientBuilder {

    private final Item item;

    @Nullable
    private Component displayName;
    @Nullable
    private Component fullName;
    @Nullable
    private SoundEvent soundEvent;

    private ItemStack displayItem = ItemStack.EMPTY;
    private ItemStack container = ItemStack.EMPTY;

    private int height = 1;

    private boolean renderAsItem = true;

    public IngredientBuilder(Item item) {
        this.item = item;
    }

    public IngredientProperties build() {
        return new IngredientProperties(null, displayName, fullName, displayItem, container, soundEvent, height, renderAsItem);
    }

    public Item getItem() {
        return item;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public IngredientBuilder setDisplayName(Component displayName) {
        this.displayName = displayName;
        return this;
    }

    public IngredientBuilder setDisplayName(String translationKey) {
        return setDisplayName(Component.translatable(translationKey));
    }

    public IngredientBuilder setDisplayName(Item item) {
        return setDisplayName(getDefaultTranslationKey(item));
    }

    public IngredientBuilder setCustomDisplayName() {
        return setDisplayName(getItem());
    }

    public IngredientBuilder setFullName(Component fullName) {
        this.fullName = fullName;
        return this;
    }

    public IngredientBuilder setFullName(String translationKey) {
        return setFullName(Component.translatable(translationKey));
    }

    public IngredientBuilder setCustomFullName() {
        return setFullName(getDefaultTranslationKey(getItem()));
    }

    public IngredientBuilder setDisplayItem(ItemStack displayItem) {
        this.displayItem = displayItem;
        return this;
    }

    public IngredientBuilder setSpread(int color) {
        ItemStack spread = new ItemStack(ModItems.SPREAD.get());
        spread.getOrCreateTag().putInt("Color", color);
        return setDisplayItem(spread);
    }

    public IngredientBuilder setWetSound() {
        return setSound(ModSoundEvents.ADD_ITEM_WET.get());
    }

    public IngredientBuilder setMoistSound() {
        return setSound(ModSoundEvents.ADD_ITEM_MOIST.get());
    }

    public IngredientBuilder setSlimySound() {
        return setSound(ModSoundEvents.ADD_ITEM_SLIMY.get());
    }

    public IngredientBuilder setLeafySound() {
        return setSound(ModSoundEvents.ADD_ITEM_LEAFY.get());
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

    public IngredientBuilder setSound(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
        return this;
    }

    public IngredientBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public IngredientBuilder setRenderAsItem(boolean renderAsItem) {
        this.renderAsItem = renderAsItem;
        return this;
    }

    private static String getDefaultTranslationKey(Item item) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        // noinspection ConstantConditions
        if ("minecraft".equals(id.getNamespace()) || SomeAssemblyRequired.MODID.equals(id.getNamespace())) {
            return "%s.ingredient.%s".formatted(SomeAssemblyRequired.MODID, id.getPath());
        } else {
            return "%s.ingredient.%s.%s".formatted(SomeAssemblyRequired.MODID, id.getNamespace(), id.getPath());
        }
    }
}
