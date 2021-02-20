package someassemblyrequired.data.builder;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.common.init.ModItems;
import someassemblyrequired.common.init.ModRecipeTypes;
import someassemblyrequired.common.init.ModSoundEvents;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SpreadTypeBuilder {

    private final Ingredient ingredient;
    private SoundEvent soundEvent;
    private Item container;

    private SpreadTypeBuilder(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public static void addSpreadTypes(Consumer<IFinishedRecipe> consumer) {
        spreadType(Items.HONEY_BOTTLE).build(consumer);
        spreadType(Items.MILK_BUCKET).build(consumer);
        spreadType(Items.BEETROOT_SOUP)
                .withContainer(Items.BOWL)
                .build(consumer);
        spreadType(Items.MUSHROOM_STEW)
                .withContainer(Items.BOWL)
                .build(consumer);
        spreadType(Items.POTION)
                .withContainer(Items.GLASS_BOTTLE)
                .build(consumer);
        spreadType(Items.RABBIT_STEW)
                .withContainer(Items.BOWL)
                .build(consumer);
        spreadType(Items.SUSPICIOUS_STEW)
                .withContainer(Items.BOWL)
                .build(consumer);

        spreadType(ModItems.KETCHUP_BOTTLE.get()).build(consumer);
        spreadType(ModItems.MAYONNAISE_BOTTLE.get()).build(consumer);
        spreadType(ModItems.SWEET_BERRY_JAM_BOTTLE.get()).build(consumer);
    }

    public static SpreadTypeBuilder spreadType(Ingredient ingredient) {
        return new SpreadTypeBuilder(ingredient);
    }

    public static SpreadTypeBuilder spreadType(Item ingredient) {
        return new SpreadTypeBuilder(Ingredient.fromItems(ingredient));
    }

    public SpreadTypeBuilder withContainer(Item container) {
        this.container = container;
        return this;
    }

    public SpreadTypeBuilder withSound(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
        return this;
    }

    public void build(Consumer<IFinishedRecipe> consumer) {
        // noinspection ConstantConditions
        String name = ingredient.getMatchingStacks()[0].getItem().getRegistryName().getPath();
        build(consumer, SomeAssemblyRequired.MODID + ":spread_types/" + name);
    }

    public void build(Consumer<IFinishedRecipe> consumer, String save) {
        build(consumer, new ResourceLocation(save));
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new SpreadTypeBuilder.Result(id, ingredient, container, soundEvent));
    }

    public static class Result implements IFinishedRecipe {

        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final Item container;
        private final SoundEvent soundEvent;

        public Result(ResourceLocation id, Ingredient ingredient, Item container, SoundEvent soundEvent) {
            this.id = id;
            this.ingredient = ingredient;
            this.container = container;
            this.soundEvent = soundEvent;
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        public void serialize(JsonObject object) {
            object.add("ingredient", ingredient.serialize());

            if (container != null && container != Items.AIR) {
                object.addProperty("container", container.getRegistryName().toString());
            }

            if (soundEvent != null && soundEvent != ModSoundEvents.ADD_SPREAD.get()) {
                object.addProperty("sound", soundEvent.getRegistryName().toString());
            }
        }

        @Override
        public ResourceLocation getID() {
            return id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return ModRecipeTypes.SPREAD_TYPE_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }
}
