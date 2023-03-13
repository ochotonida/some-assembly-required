package someassemblyrequired.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ItemTrigger extends SimpleCriterionTrigger<ItemTrigger.Instance> {

    private final ResourceLocation id;

    public ItemTrigger(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Instance createInstance(JsonObject json, EntityPredicate.Composite entityPredicate, DeserializationContext conditionsParser) {
        return new Instance(entityPredicate, ItemPredicate.fromJson(json.get("item")));
    }

    public Instance instance() {
        return new Instance(EntityPredicate.Composite.ANY, ItemPredicate.ANY);
    }

    public void trigger(ServerPlayer player, ItemStack item) {
        this.trigger(player, (instance) -> instance.test(item));
    }

    public class Instance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;

        public Instance(EntityPredicate.Composite player, ItemPredicate item) {
            super(id, player);
            this.item = item;
        }

        public boolean test(ItemStack item) {
            return this.item.matches(item);
        }

        public JsonObject serializeToJson(SerializationContext conditions) {
            JsonObject jsonobject = super.serializeToJson(conditions);
            jsonobject.add("item", this.item.serializeToJson());
            return jsonobject;
        }
    }
}
