package someassemblyrequired.data;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModBlocks;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.util.Util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
                .filter(item -> ForgeRegistries.ITEMS.getKey(item).getNamespace().equals(SomeAssemblyRequired.MODID))
                .collect(Collectors.toSet());

        removeAll(items, ModItems.SANDWICH.get());
        getBuilder(ModBlocks.SANDWICH.getId().getPath()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .texture("particle", prefixItem("bread_slice"))
                .transforms()
                .transform(ItemTransforms.TransformType.GUI)
                .rotation(30, 45, 0)
                .scale(0.8F)
                .end()
                .transform(ItemTransforms.TransformType.GROUND)
                .rotation(0, 180, 0)
                .scale(0.5F)
                .end()
                .transform(ItemTransforms.TransformType.HEAD)
                .rotation(0, 180, 0)
                .translation(0, 0.5F, 0)
                .end()
                .transform(ItemTransforms.TransformType.FIXED)
                .rotation(0, 180, 0)
                .translation(0, -4, 0)
                .end()
                .transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
                .rotation(75, 315, 0)
                .translation(0, 2.5F, 0)
                .scale(0.55F)
                .end()
                .transform(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, 315, 0)
                .translation(0, 1, 0)
                .scale(0.5F)
                .end()
                .end();

        removeAll(items, ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get());

        removeAll(items, ModItems.SPREAD.get());
        ItemModelBuilder builder = addGeneratedModel(ModItems.SPREAD.get());
        removeAll(items, Ingredients.itemsWithCustomModel.toArray(new ItemLike[]{}));
        List<Item> itemsWithCustomModel = Ingredients.itemsWithCustomModel;
        for (int i = 0; i < itemsWithCustomModel.size(); i++) {
            Item item = itemsWithCustomModel.get(i);
            if (SomeAssemblyRequired.MODID.equals(ForgeRegistries.ITEMS.getKey(item).getNamespace())) {
                addGeneratedModel(item);
            }
            String id = "%s/%s".formatted(ForgeRegistries.ITEMS.getKey(item).getNamespace(), ForgeRegistries.ITEMS.getKey(item).getPath());
            String path = "ingredient/" + id;

            builder.override()
                    .model(addGeneratedModel(path, prefixItem(path)))
                    .predicate(new ResourceLocation("custom_model_data"), i + 1)
                    .end();
        }

        removeAll(items, ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get());
        addGeneratedModel("enchanted_golden_apple_slices", prefixItem("golden_apple_slices"));

        removeAll(items, item -> item instanceof BlockItem).forEach(
                block -> withExistingParent(ForgeRegistries.ITEMS.getKey(block).getPath(), prefixBlock(ForgeRegistries.ITEMS.getKey(block).getPath()))
        );

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
        String name = ForgeRegistries.ITEMS.getKey(item).getPath();
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
