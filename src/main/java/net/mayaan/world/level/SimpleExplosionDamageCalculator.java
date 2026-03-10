/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level;

import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.core.HolderSet;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Explosion;
import net.mayaan.world.level.ExplosionDamageCalculator;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.material.FluidState;

public class SimpleExplosionDamageCalculator
extends ExplosionDamageCalculator {
    private final boolean explodesBlocks;
    private final boolean damagesEntities;
    private final Optional<Float> knockbackMultiplier;
    private final Optional<HolderSet<Block>> immuneBlocks;

    public SimpleExplosionDamageCalculator(boolean explodesBlocks, boolean damagesEntities, Optional<Float> knockbackMultiplier, Optional<HolderSet<Block>> immuneBlocks) {
        this.explodesBlocks = explodesBlocks;
        this.damagesEntities = damagesEntities;
        this.knockbackMultiplier = knockbackMultiplier;
        this.immuneBlocks = immuneBlocks;
    }

    @Override
    public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid) {
        if (this.immuneBlocks.isPresent()) {
            if (block.is(this.immuneBlocks.get())) {
                return Optional.of(Float.valueOf(3600000.0f));
            }
            return Optional.empty();
        }
        return super.getBlockExplosionResistance(explosion, level, pos, block, fluid);
    }

    @Override
    public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) {
        return this.explodesBlocks;
    }

    @Override
    public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
        return this.damagesEntities;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public float getKnockbackMultiplier(Entity entity) {
        boolean creativeFlying;
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (player.getAbilities().flying) {
                return 0.0f;
            }
        }
        boolean bl = creativeFlying = false;
        if (creativeFlying) {
            return 0.0f;
        }
        float f = this.knockbackMultiplier.orElseGet(() -> Float.valueOf(super.getKnockbackMultiplier(entity))).floatValue();
        return f;
    }
}

