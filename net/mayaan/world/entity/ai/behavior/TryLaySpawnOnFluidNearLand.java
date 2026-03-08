/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.Applicative
 */
package net.mayaan.world.entity.ai.behavior;

import com.mojang.datafixers.kinds.Applicative;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.tags.BlockTags;
import net.mayaan.tags.FluidTags;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.behavior.BehaviorControl;
import net.mayaan.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gameevent.GameEvent;

public class TryLaySpawnOnFluidNearLand {
    public static BehaviorControl<LivingEntity> create(Block spawnBlock) {
        return BehaviorBuilder.create((BehaviorBuilder.Instance<E> i) -> i.group(i.absent(MemoryModuleType.ATTACK_TARGET), i.present(MemoryModuleType.WALK_TARGET), i.present(MemoryModuleType.IS_PREGNANT)).apply((Applicative)i, (attackTarget, walkTarget, pregnant) -> (level, body, timestamp) -> {
            if (body.isInWater() || !body.onGround()) {
                return false;
            }
            BlockPos belowPos = body.blockPosition().below();
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos spawnPos;
                BlockPos relativePos = belowPos.relative(direction);
                if (!level.getBlockState(relativePos).getCollisionShape(level, relativePos).getFaceShape(Direction.UP).isEmpty() || !level.getFluidState(relativePos).is(FluidTags.SUPPORTS_FROGSPAWN) && !level.getBlockState(relativePos).is(BlockTags.SUPPORTS_FROGSPAWN) || !level.getBlockState(spawnPos = relativePos.above()).isAir()) continue;
                BlockState newState = spawnBlock.defaultBlockState();
                level.setBlock(spawnPos, newState, 3);
                level.gameEvent(GameEvent.BLOCK_PLACE, spawnPos, GameEvent.Context.of(body, newState));
                level.playSound(null, body, SoundEvents.FROG_LAY_SPAWN, SoundSource.BLOCKS, 1.0f, 1.0f);
                pregnant.erase();
                return true;
            }
            return true;
        }));
    }
}

