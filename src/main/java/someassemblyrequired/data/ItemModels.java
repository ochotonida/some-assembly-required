package someassemblyrequired.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
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

        // kitchen knife
        removeAll(items, ModItems.KITCHEN_KNIFE.get());
        addHandHeldModel(ModItems.KITCHEN_KNIFE.get());

        // spreads
        removeAll(items, ModItems.SPREAD.get());
        addGeneratedModel("spread_on_loaf", prefixItem("spread_on_loaf"));
        addGeneratedModel(ModItems.SPREAD.get())
                .override()
                .model(getExistingFile(prefixItem("spread_on_loaf")))
                .predicate(Util.prefix("on_loaf"), 1)
                .end();

        // items with sandwich overrides
        removeAll(items,
                ModItems.APPLE_SLICES.get(),
                ModItems.GOLDEN_APPLE_SLICES.get(),
                ModItems.CHOPPED_CARROT.get(),
                ModItems.CHOPPED_GOLDEN_CARROT.get(),
                ModItems.CHOPPED_BEETROOT.get(),
                ModItems.PORK_CUTS.get(),
                ModItems.BACON_STRIPS.get(),
                ModItems.SLICED_TOASTED_CRIMSON_FUNGUS.get(),
                ModItems.SLICED_TOASTED_WARPED_FUNGUS.get(),
                ModItems.TOMATO_SLICES.get(),
                ModItems.LETTUCE_LEAF.get()
        ).forEach(item -> {
            String itemName = item.getRegistryName().getPath();
            addGeneratedModel(itemName + "_on_sandwich", prefixItem(itemName + "_on_sandwich"));
            addGeneratedModel(itemName, prefixItem(itemName))
                    .override()
                    .model(getExistingFile(prefixItem(itemName + "_on_sandwich")))
                    .predicate(Util.prefix("on_sandwich"), 1)
                    .end();
        });

        // enchanted golden apple slices
        removeAll(items, ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get());
        addGeneratedModel("enchanted_golden_apple_slices", prefixItem("golden_apple_slices"))
                .override()
                .model(getExistingFile(prefixItem("golden_apple_slices_on_sandwich")))
                .predicate(Util.prefix("on_sandwich"), 1)
                .end();

        // toasters
        for (Item toaster : removeAll(items, ModBlocks.getToasters())) {
            withExistingParent(toaster.getRegistryName().getPath(), prefixBlock("idle_redstone_toaster"));
        }

        // block items with sprites
        removeAll(items, ModItems.LETTUCE_SEEDS.get(), ModItems.TOMATO_SEEDS.get()).forEach(this::addGeneratedModel);

        // normal block items
        removeAll(items, item -> item instanceof BlockItem).forEach(
                block -> withExistingParent(block.getRegistryName().getPath(), prefixBlock(block.getRegistryName().getPath()))
        );

        // normal items
        items.forEach(this::addGeneratedModel);
    }

    private ResourceLocation prefixBlock(String path) {
        return Util.prefix("block/" + path);
    }

    private ResourceLocation prefixItem(String path) {
        return Util.prefix("item/" + path);
    }

    private void addHandHeldModel(Item item) {
        // noinspection ConstantConditions
        withExistingParent("item/" + item.getRegistryName().getPath(), "item/handheld").texture("layer0", prefixItem(item.getRegistryName().getPath()));
    }

    private ItemModelBuilder addGeneratedModel(Item item) {
        // noinspection ConstantConditions
        String name = item.getRegistryName().getPath();
        return withExistingParent("item/" + name, "item/generated").texture("layer0", prefixItem(name));
    }

    private ItemModelBuilder addGeneratedModel(String name, ResourceLocation texture) {
        return withExistingParent(name, "item/generated").texture("layer0", texture);
    }

    private static Collection<Item> removeAll(Set<Item> set, IItemProvider... items) {
        Set<Item> result = Arrays.stream(items).map(IItemProvider::asItem).collect(Collectors.toSet());
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
