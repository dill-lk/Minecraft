/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 */
package net.mayaan.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.core.GlobalPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.behavior.Behavior;
import net.mayaan.world.entity.ai.behavior.BehaviorUtils;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.memory.MemoryStatus;
import net.mayaan.world.entity.ai.village.poi.PoiManager;
import net.mayaan.world.entity.npc.villager.Villager;
import net.mayaan.world.entity.schedule.Activity;

public class GoToPotentialJobSite
extends Behavior<Villager> {
    private static final int TICKS_UNTIL_TIMEOUT = 1200;
    final float speedModifier;

    public GoToPotentialJobSite(float speedModifier) {
        super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, (Object)((Object)MemoryStatus.VALUE_PRESENT)), 1200);
        this.speedModifier = speedModifier;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Villager body) {
        return body.getBrain().getActiveNonCoreActivity().map(activity -> activity == Activity.IDLE || activity == Activity.WORK || activity == Activity.PLAY).orElse(true);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) {
        return body.getBrain().hasMemoryValue(MemoryModuleType.POTENTIAL_JOB_SITE);
    }

    @Override
    protected void tick(ServerLevel level, Villager body, long timestamp) {
        BehaviorUtils.setWalkAndLookTargetMemories((LivingEntity)body, body.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get().pos(), this.speedModifier, 1);
    }

    @Override
    protected void stop(ServerLevel level, Villager body, long timestamp) {
        Optional<GlobalPos> potentialJobSitePos = body.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
        potentialJobSitePos.ifPresent(globalPos -> {
            BlockPos pos = globalPos.pos();
            ServerLevel serverLevel = level.getServer().getLevel(globalPos.dimension());
            if (serverLevel == null) {
                return;
            }
            PoiManager manager = serverLevel.getPoiManager();
            if (manager.exists(pos, p -> true)) {
                manager.release(pos);
            }
            level.debugSynchronizers().updatePoi(pos);
        });
        body.getBrain().eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
    }
}

