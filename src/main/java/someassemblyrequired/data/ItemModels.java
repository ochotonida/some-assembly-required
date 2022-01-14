package someassemblyrequired.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.ingredient.CustomIngredientModels;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.util.Util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemModels extends ItemModelProvider {

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SomeAssemblyRequired.MODID, existingFileHelper);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void registerModels() {
        Set<Item> items = ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item.getRegistryName().getNamespace().equals(SomeAssemblyRequired.MODID))
                .collect(Collectors.toSet());

        // ignored
        removeAll(items, ModItems.SANDWICH.get());

        removeAll(items, ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get());

        // items with sandwich overrides
        removeAll(items, ModItems.SPREAD.get());
        ItemModelBuilder builder = addGeneratedModel(ModItems.SPREAD.get());
        removeAll(items, CustomIngredientModels.itemsWithCustomModel.toArray(new ItemLike[]{}));
        for (Item item : CustomIngredientModels.itemsWithCustomModel) {
            if (SomeAssemblyRequired.MODID.equals(item.getRegistryName().getNamespace())) {
                addGeneratedModel(item);
            }
            String id = "%s/%s".formatted(item.getRegistryName().getNamespace(), item.getRegistryName().getPath());
            String path = "ingredient/" + id;

            builder.override()
                    .model(addGeneratedModel(path, prefixItem(path)))
                    .predicate(Util.id(id), 1)
                    .end();
        }

        // enchanted golden apple slices
        removeAll(items, ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get());
        addGeneratedModel("enchanted_golden_apple_slices", prefixItem("golden_apple_slices"));

        // toasters
        for (Item toaster : removeAll(items, ModBlocks.getToasters())) {
            withExistingParent(toaster.getRegistryName().getPath(), prefixBlock("idle_redstone_toaster"));
        }

        // normal block items
        removeAll(items, item -> item instanceof BlockItem).forEach(
                block -> withExistingParent(block.getRegistryName().getPath(), prefixBlock(block.getRegistryName().getPath()))
        );

        // normal items
        items.forEach(this::addGeneratedModel);
    }

    private ResourceLocation prefixBlock(String path) {
        return Util.id("block/" + path);
    }

    private ResourceLocation prefixItem(String path) {
        return Util.id("item/" + path);
    }

    private ItemModelBuilder addGeneratedModel(Item item) {
        // noinspection ConstantConditions
        String name = item.getRegistryName().getPath();
        return withExistingParent("item/" + name, "item/generated").texture("layer0", prefixItem(name));
    }

    private ItemModelBuilder addGeneratedModel(String name, ResourceLocation texture) {
        return withExistingParent(name, "item/generated").texture("layer0", texture);
    }

    private static Collection<Item> removeAll(Set<Item> set, ItemLike... items) {
        Set<Item> result = Arrays.stream(items).map(ItemLike::asItem).collect(Collectors.toSet());
        set.removeAll(result);

        if (result.size() != items.length) {
            throw new IllegalArgumentException();
        }

        return result;
    }

    private static <T> Collection<T> removeAll(Set<T> set, Predicate<T> pred) {
        Set<T> result = set.stream().filter(pred).collect(Collectors.toSet());
        set.removeAll(result);

        if (result.size() <= 0) {
            throw new IllegalArgumentException();
        }

        return result;
    }
}
