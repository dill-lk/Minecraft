/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableSet
 *  com.mojang.datafixers.util.Pair
 */
package net.mayaan.world.entity.animal.frog;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.mayaan.util.valueproviders.UniformInt;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.ai.ActivityData;
import net.mayaan.world.entity.ai.behavior.AnimalPanic;
import net.mayaan.world.entity.ai.behavior.CountDownCooldownTicks;
import net.mayaan.world.entity.ai.behavior.FollowTemptation;
import net.mayaan.world.entity.ai.behavior.GateBehavior;
import net.mayaan.world.entity.ai.behavior.LookAtTargetSink;
import net.mayaan.world.entity.ai.behavior.MoveToTargetSink;
import net.mayaan.world.entity.ai.behavior.RandomStroll;
import net.mayaan.world.entity.ai.behavior.SetEntityLookTargetSometimes;
import net.mayaan.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.mayaan.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.memory.MemoryStatus;
import net.mayaan.world.entity.animal.frog.Tadpole;
import net.mayaan.world.entity.schedule.Activity;

public class TadpoleAi {
    private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0f;
    private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 0.5f;
    private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25f;

    public static List<ActivityData<Tadpole>> getActivities() {
        return List.of(TadpoleAi.initCoreActivity(), TadpoleAi.initIdleActivity());
    }

    private static ActivityData<Tadpole> initCoreActivity() {
        return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new AnimalPanic(2.0f), (Object)new LookAtTargetSink(45, 90), (Object)new MoveToTargetSink(), (Object)new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)));
    }

    private static ActivityData<Tadpole> initIdleActivity() {
        return ActivityData.create(Activity.IDLE, ImmutableList.of((Object)Pair.of((Object)0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0f, UniformInt.of(30, 60))), (Object)Pair.of((Object)1, (Object)new FollowTemptation(s -> Float.valueOf(1.25f))), (Object)Pair.of((Object)2, new GateBehavior((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, (Object)((Object)MemoryStatus.VALUE_ABSENT)), (Set<MemoryModuleType<?>>)ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL, ImmutableList.of((Object)Pair.of(RandomStroll.swim(0.5f), (Object)2), (Object)Pair.of(SetWalkTargetFromLookTarget.create(0.5f, 3), (Object)3), (Object)Pair.of(BehaviorBuilder.triggerIf(Entity::isInWater), (Object)5))))));
    }

    public static void updateActivity(Tadpole body) {
        body.getBrain().setActiveActivityToFirstValid((List<Activity>)ImmutableList.of((Object)Activity.IDLE));
    }
}

