package someassemblyrequired.data;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.util.Util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("ConstantConditions")
public class ItemModels extends ItemModelProvider {

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SomeAssemblyRequired.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Set<Item> items = ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> ForgeRegistries.ITEMS.getKey(item).getNamespace().equals(SomeAssemblyRequired.MODID))
                .collect(Collectors.toSet());

        removeAll(items, ModItems.SANDWICH.get());
        addSandwich();

        removeAll(items, ModItems.SPREAD.get());
        removeAll(items, Ingredients.MODEL_OVERRIDES.toArray(new ItemLike[]{}));
        addSpread();

        removeAll(items, ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get());
        addGeneratedModel("enchanted_golden_apple_slices", prefixItem("golden_apple_slices"));

        removeAll(items, item -> item instanceof BlockItem).forEach(
                block -> withExistingParent(ForgeRegistries.ITEMS.getKey(block).getPath(), prefixBlock(ForgeRegistries.ITEMS.getKey(block).getPath()))
        );

        items.forEach(this::addGeneratedModel);
    }

    private void addSpread() {
        HashMap<Item, ItemModelBuilder> customModels = new HashMap<>();
        addCustomIngredientModels(customModels);

        ItemModelBuilder spreadModel = addGeneratedModel(ModItems.SPREAD.get());
        for (int i = 0; i < Ingredients.MODEL_OVERRIDES.size(); i++) {
            Item item = Ingredients.MODEL_OVERRIDES.get(i);
            if (SomeAssemblyRequired.MODID.equals(ForgeRegistries.ITEMS.getKey(item).getNamespace())) {
                addGeneratedModel(item);
            }

            String path = getIngredientPath(item);

            ItemModelBuilder model;
            if (customModels.containsKey(item)) {
                model = customModels.get(item);
            } else {
                model = addGeneratedModel(path, prefixItem(path));
            }

            spreadModel.override()
                    .model(model)
                    .predicate(new ResourceLocation("custom_model_data"), i + 1)
                    .end();
        }
    }

    private void addCustomIngredientModels(HashMap<Item, ItemModelBuilder> customModels) {
        String path = getIngredientPath(Items.POTATO);
        ResourceLocation texture = prefixItem(path);
        customModels.put(Items.POTATO,
                getBuilder(path)
                        .texture("potato", texture)
                        .element()
                        .from(5, 4, 3.5F)
                        .to(11, 12, 8.5F)
                        .face(Direction.NORTH).uvs(0, 5, 8, 11).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
                        .face(Direction.SOUTH).uvs(8, 5, 16, 11).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                        .face(Direction.DOWN).uvs(0, 11, 6, 16).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).end()
                        .face(Direction.UP).uvs(6, 11, 12, 16).end()
                        .face(Direction.EAST).uvs(0, 0, 8, 5).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                        .face(Direction.WEST).uvs(8, 0, 16, 5).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
                        .faces(((direction, builder) -> builder.texture("#potato")))
                        .end()
        );
    }

    private String getIngredientPath(Item item) {
        return "ingredient/" + getItemPath(item);
    }

    private String getItemPath(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).toString().replace(':', '/');
    }

    private String getItemName(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }

    private void addSandwich() {
        getBuilder(getItemName(ModItems.SANDWICH.get())).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
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

    private static void removeAll(Set<Item> set, ItemLike... items) {
        Set<Item> result = Arrays.stream(items).map(ItemLike::asItem).collect(Collectors.toSet());
        set.removeAll(result);
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
