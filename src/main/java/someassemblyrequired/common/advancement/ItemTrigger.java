package someassemblyrequired.common.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

public class ItemTrigger extends AbstractCriterionTrigger<ItemTrigger.Instance> {

    private final ResourceLocation id;

    public ItemTrigger(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        return new Instance(entityPredicate, ItemPredicate.deserialize(json.get("item")));
    }

    public Instance instance() {
        return new Instance(EntityPredicate.AndPredicate.ANY_AND, ItemPredicate.ANY);
    }

    public void trigger(ServerPlayerEntity player, ItemStack item) {
        this.triggerListeners(player, (instance) -> instance.test(item));
    }

    public class Instance extends CriterionInstance {

        private final ItemPredicate item;

        public Instance(EntityPredicate.AndPredicate player, ItemPredicate item) {
            super(id, player);
            this.item = item;
        }

        public boolean test(ItemStack item) {
            return this.item.test(item);
        }

        public JsonObject serialize(ConditionArraySerializer conditions) {
            JsonObject jsonobject = super.serialize(conditions);
            jsonobject.add("item", this.item.serialize());
            return jsonobject;
        }
    }
}
