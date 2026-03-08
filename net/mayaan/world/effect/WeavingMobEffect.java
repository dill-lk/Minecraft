/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 */
package net.mayaan.world.effect;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.function.ToIntFunction;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.RandomSource;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.effect.MobEffectCategory;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.gamerules.GameRules;

class WeavingMobEffect
extends MobEffect {
    private final ToIntFunction<RandomSource> maxCobwebs;

    protected WeavingMobEffect(MobEffectCategory category, int color, ToIntFunction<RandomSource> maxCobwebs) {
        super(category, color, ParticleTypes.ITEM_COBWEB);
        this.maxCobwebs = maxCobwebs;
    }

    @Override
    public void onMobRemoved(ServerLevel level, LivingEntity mob, int amplifier, Entity.RemovalReason reason) {
        if (reason == Entity.RemovalReason.KILLED && (mob instanceof Player || level.getGameRules().get(GameRules.MOB_GRIEFING).booleanValue())) {
            this.spawnCobwebsRandomlyAround(level, mob.getRandom(), mob.blockPosition());
        }
    }

    private void spawnCobwebsRandomlyAround(ServerLevel level, RandomSource random, BlockPos pos) {
        HashSet positionsToTransform = Sets.newHashSet();
        int cobwebCount = this.maxCobwebs.applyAsInt(random);
        for (BlockPos blockPos : BlockPos.randomInCube(random, 15, pos, 1)) {
            BlockPos below = blockPos.below();
            if (positionsToTransform.contains(blockPos) || !level.getBlockState(blockPos).canBeReplaced() || !level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) continue;
            positionsToTransform.add(blockPos.immutable());
            if (positionsToTransform.size() < cobwebCount) continue;
            break;
        }
        for (BlockPos blockPos : positionsToTransform) {
            level.setBlock(blockPos, Blocks.COBWEB.defaultBlockState(), 3);
            level.levelEvent(3018, blockPos, 0);
        }
    }
}

