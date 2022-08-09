package someassemblyrequired.data.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import someassemblyrequired.common.init.ModRecipeTypes;
import someassemblyrequired.common.util.JsonHelper;
import someassemblyrequired.common.util.Util;
import someassemblyrequired.integration.ModCompat;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SandwichSpoutingRecipeBuilder {

    public static void addFillingRecipes(Consumer<FinishedRecipe> consumer) {
        consumer.accept(create("water_bottle", PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER), FluidIngredient.fromFluid(Fluids.WATER, 250)));
        consumer.accept(create(Items.HONEY_BOTTLE, AllFluids.HONEY.get()));
        consumer.accept(create(AllItems.BUILDERS_TEA.get(), AllFluids.TEA.get()));
        consumer.accept(create(Items.MILK_BUCKET, ForgeMod.MILK.get()));
        consumer.accept(createPotionFillingRecipe("potion"));
    }

    public static Result create(Item result, Fluid fluid) {
        return create(new ItemStack(result), fluid, 250);
    }

    public static Result create(Item result, Fluid fluid, int amountRequired) {
        return create(new ItemStack(result), fluid, amountRequired);
    }

    public static Result create(ItemStack result, Fluid fluid, int amountRequired) {
        // noinspection ConstantConditions
        return create(result.getItem().getRegistryName().getPath(), result, FluidIngredient.fromFluid(fluid, amountRequired));
    }

    public static Result create(String name, ItemStack result, FluidIngredient ingredient) {
        return new Result(id(name), ingredient, result);
    }

    private static ResourceLocation id(String id) {
        return Util.id("sandwich_spouting/%s".formatted(id));
    }

    public static FinishedRecipe createPotionFillingRecipe(String id) {
        return new FinishedRecipe() {

            @Override
            public void serializeRecipeData(JsonObject object) {
                JsonHelper.addModLoadedCondition(object, ModCompat.CREATE);
            }

            @Override
            public ResourceLocation getId() {
                return id(id);
            }

            @Override
            public RecipeSerializer<?> getType() {
                return ModRecipeTypes.SANDWICH_POTION_SPOUTING_SERIALIZER.get();
            }

            @Nullable
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        };
    }

    public record Result(ResourceLocation id, FluidIngredient ingredient, ItemStack result) implements FinishedRecipe {

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipeTypes.SANDWICH_FLUID_SPOUTING_SERIALIZER.get();
        }

        public void serializeRecipeData(JsonObject object) {
            JsonArray conditions = new JsonArray();
            conditions.add(CraftingHelper.serialize(new ModLoadedCondition(ModCompat.CREATE)));
            object.add("conditions", conditions);
            object.add("fluid", ingredient.serialize());
            object.add("result", JsonHelper.writeItemStack(result));
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
