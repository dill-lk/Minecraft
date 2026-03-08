/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.Applicative
 */
package net.mayaan.world.entity.ai.behavior;

import com.mojang.datafixers.kinds.Applicative;
import net.mayaan.core.BlockPos;
import net.mayaan.core.GlobalPos;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.behavior.BehaviorControl;
import net.mayaan.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.level.block.BellBlock;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.state.BlockState;

public class RingBell {
    private static final float BELL_RING_CHANCE = 0.95f;
    public static final int RING_BELL_FROM_DISTANCE = 3;

    public static BehaviorControl<LivingEntity> create() {
        return BehaviorBuilder.create(i -> i.group(i.present(MemoryModuleType.MEETING_POINT)).apply((Applicative)i, meetingPoint -> (level, body, timestamp) -> {
            BlockState state;
            if (level.getRandom().nextFloat() <= 0.95f) {
                return false;
            }
            BlockPos pos = ((GlobalPos)i.get(meetingPoint)).pos();
            if (pos.closerThan(body.blockPosition(), 3.0) && (state = level.getBlockState(pos)).is(Blocks.BELL)) {
                BellBlock bellBlock = (BellBlock)state.getBlock();
                bellBlock.attemptToRing(body, level, pos, null);
            }
            return true;
        }));
    }
}

