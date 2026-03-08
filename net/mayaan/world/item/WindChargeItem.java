/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.core.Direction;
import net.mayaan.core.Position;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.stats.Stats;
import net.mayaan.util.RandomSource;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.projectile.Projectile;
import net.mayaan.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.ProjectileItem;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.DispenserBlock;
import net.mayaan.world.phys.Vec3;

public class WindChargeItem
extends Item
implements ProjectileItem {
    public static final float PROJECTILE_SHOOT_POWER = 1.5f;

    public WindChargeItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            Projectile.spawnProjectileFromRotation((source, l, itemStack) -> new WindCharge(player, level, player.position().x(), player.getEyePosition().y(), player.position().z()), serverLevel, stack, player, 0.0f, 1.5f, 1.0f);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WIND_CHARGE_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));
        player.awardStat(Stats.ITEM_USED.get(this));
        stack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        RandomSource random = level.getRandom();
        double dirX = random.triangle((double)direction.getStepX(), 0.11485000000000001);
        double dirY = random.triangle((double)direction.getStepY(), 0.11485000000000001);
        double dirZ = random.triangle((double)direction.getStepZ(), 0.11485000000000001);
        Vec3 dir = new Vec3(dirX, dirY, dirZ);
        WindCharge windCharge = new WindCharge(level, position.x(), position.y(), position.z(), dir);
        windCharge.setDeltaMovement(dir);
        return windCharge;
    }

    @Override
    public void shoot(Projectile projectile, double xd, double yd, double zd, float pow, float uncertainty) {
    }

    @Override
    public ProjectileItem.DispenseConfig createDispenseConfig() {
        return ProjectileItem.DispenseConfig.builder().positionFunction((source, direction) -> DispenserBlock.getDispensePosition(source, 1.0, Vec3.ZERO)).uncertainty(6.6666665f).power(1.0f).overrideDispenseEvent(1051).build();
    }
}

