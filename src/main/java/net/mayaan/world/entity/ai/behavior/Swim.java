/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 */
package net.mayaan.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.tags.FluidTags;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.ai.behavior.Behavior;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.memory.MemoryStatus;

public class Swim<T extends Mob>
extends Behavior<T> {
    private final float chance;

    public Swim(float chance) {
        super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of());
        this.chance = chance;
    }

    public static <T extends Mob> boolean shouldSwim(T mob) {
        return mob.isInWater() && mob.getFluidHeight(FluidTags.WATER) > mob.getFluidJumpThreshold() || mob.isInLava();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Mob body) {
        return Swim.shouldSwim(body);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Mob body, long timestamp) {
        return this.checkExtraStartConditions(level, body);
    }

    @Override
    protected void tick(ServerLevel level, Mob body, long timestamp) {
        if (body.getRandom().nextFloat() < this.chance) {
            body.getJumpControl().jump();
        }
    }
}

