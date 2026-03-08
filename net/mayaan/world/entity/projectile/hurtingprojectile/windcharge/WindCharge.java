/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.projectile.hurtingprojectile.windcharge;

import java.util.Optional;
import java.util.function.Function;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.tags.BlockTags;
import net.mayaan.util.Mth;
import net.mayaan.util.random.WeightedList;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntityReference;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.projectile.ProjectileDeflection;
import net.mayaan.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
import net.mayaan.world.level.ExplosionDamageCalculator;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.SimpleExplosionDamageCalculator;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class WindCharge
extends AbstractWindCharge {
    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(true, false, Optional.of(Float.valueOf(1.22f)), BuiltInRegistries.BLOCK.get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()));
    private static final float RADIUS = 1.2f;
    private static final float MIN_CAMERA_DISTANCE_SQUARED = Mth.square(3.5f);
    private int noDeflectTicks = 5;

    public WindCharge(EntityType<? extends AbstractWindCharge> type, Level level) {
        super(type, level);
    }

    public WindCharge(Player player, Level level, double x, double y, double z) {
        super(EntityType.WIND_CHARGE, level, player, x, y, z);
    }

    public WindCharge(Level level, double x, double y, double z, Vec3 direction) {
        super((EntityType<? extends AbstractWindCharge>)EntityType.WIND_CHARGE, x, y, z, direction, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.noDeflectTicks > 0) {
            --this.noDeflectTicks;
        }
    }

    @Override
    public boolean deflect(ProjectileDeflection deflection, @Nullable Entity deflectingEntity, @Nullable EntityReference<Entity> newOwner, boolean byAttack) {
        if (this.noDeflectTicks > 0) {
            return false;
        }
        return super.deflect(deflection, deflectingEntity, newOwner, byAttack);
    }

    @Override
    protected void explode(Vec3 position) {
        this.level().explode(this, null, EXPLOSION_DAMAGE_CALCULATOR, position.x(), position.y(), position.z(), 1.2f, false, Level.ExplosionInteraction.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE, WeightedList.of(), SoundEvents.WIND_CHARGE_BURST);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        if (this.tickCount < 2 && distance < (double)MIN_CAMERA_DISTANCE_SQUARED) {
            return false;
        }
        return super.shouldRenderAtSqrDistance(distance);
    }
}

