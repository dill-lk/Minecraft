/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block;

import net.mayaan.core.BlockPos;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.item.FallingBlockEntity;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.state.BlockState;

public interface Fallable {
    default public void onLand(Level level, BlockPos pos, BlockState state, BlockState replacedBlock, FallingBlockEntity entity) {
    }

    default public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity entity) {
    }

    default public DamageSource getFallDamageSource(Entity entity) {
        return entity.damageSources().fallingBlock(entity);
    }
}

