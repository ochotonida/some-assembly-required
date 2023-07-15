package someassemblyrequired.ingredient.behavior;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityTeleportEvent;

public class ChorusFruitBehavior implements IngredientBehavior {

    @Override
    public void onEaten(ItemStack item, LivingEntity entity) {
        if (entity.level() instanceof ServerLevel level) {
            double x1 = entity.getX();
            double y1 = entity.getY();
            double z1 = entity.getZ();

            for (int i = 0; i < 16; ++i) {
                double x2 = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * 16;
                double y2 = Mth.clamp(entity.getY() + level.getRandom().nextInt(16) - 8, level.getMinBuildHeight(), level.getMinBuildHeight() + level.getLogicalHeight() - 1);
                double z2 = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * 16;
                if (entity.isPassenger()) {
                    entity.stopRiding();
                }

                EntityTeleportEvent.ChorusFruit event = net.minecraftforge.event.ForgeEventFactory.onChorusFruitTeleport(entity, x2, y2, z2);
                if (event.isCanceled()) return;
                if (entity.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true)) {
                    SoundEvent soundEvent = entity instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                    level.playSound(null, x1, y1, z1, soundEvent, SoundSource.PLAYERS, 1, 1);
                    entity.playSound(soundEvent, 1, 1);
                    break;
                }
            }

            if (entity instanceof Player player) {
                player.getCooldowns().addCooldown(item.getItem(), 20);
            }
        }
    }
}
