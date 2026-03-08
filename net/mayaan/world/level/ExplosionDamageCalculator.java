/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level;

import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Explosion;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.material.FluidState;
import net.mayaan.world.phys.Vec3;

public class ExplosionDamageCalculator {
    public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid) {
        if (block.isAir() && fluid.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Float.valueOf(Math.max(block.getBlock().getExplosionResistance(), fluid.getExplosionResistance())));
    }

    public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) {
        return true;
    }

    public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
        return true;
    }

    public float getKnockbackMultiplier(Entity entity) {
        return 1.0f;
    }

    public float getEntityDamageAmount(Explosion explosion, Entity entity, float exposure) {
        float doubleRadius = explosion.radius() * 2.0f;
        Vec3 center = explosion.center();
        double dist = Math.sqrt(entity.distanceToSqr(center)) / (double)doubleRadius;
        double pow = (1.0 - dist) * (double)exposure;
        return (float)((pow * pow + pow) / 2.0 * 7.0 * (double)doubleRadius + 1.0);
    }
}

