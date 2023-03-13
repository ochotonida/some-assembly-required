package someassemblyrequired.integration.diet;

import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import someassemblyrequired.init.ModItems;
import someassemblyrequired.item.sandwich.SandwichItemHandler;
import someassemblyrequired.integration.ModCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class DietCompat {

    public static void setup() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(DietCompat::onInterModEnqueue);
    }

    public static void onInterModEnqueue(InterModEnqueueEvent event) {
        InterModComms.sendTo(ModCompat.DIET, "item",
                () -> new Tuple<Item, BiFunction<Player, ItemStack, Triple<List<ItemStack>, Integer, Float>>>(
                        ModItems.SANDWICH.get(),
                        (player, stack) -> SandwichItemHandler.get(stack).map(handler ->
                                new ImmutableTriple<>(handler.getItems(), handler.getTotalNutrition(), handler.getAverageSaturation())
                        ).orElse(
                                new ImmutableTriple<>(new ArrayList<>(), 0, 0F)
                        )));
    }
}
