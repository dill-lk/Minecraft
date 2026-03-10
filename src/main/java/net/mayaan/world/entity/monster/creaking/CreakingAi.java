/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableSet
 *  com.mojang.datafixers.util.Pair
 */
package net.mayaan.world.entity.monster.creaking;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Optional;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.valueproviders.UniformInt;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.ActivityData;
import net.mayaan.world.entity.ai.behavior.DoNothing;
import net.mayaan.world.entity.ai.behavior.LookAtTargetSink;
import net.mayaan.world.entity.ai.behavior.MeleeAttack;
import net.mayaan.world.entity.ai.behavior.MoveToTargetSink;
import net.mayaan.world.entity.ai.behavior.RandomStroll;
import net.mayaan.world.entity.ai.behavior.RunOne;
import net.mayaan.world.entity.ai.behavior.SetEntityLookTargetSometimes;
import net.mayaan.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.mayaan.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.mayaan.world.entity.ai.behavior.StartAttacking;
import net.mayaan.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.mayaan.world.entity.ai.behavior.Swim;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.memory.MemoryStatus;
import net.mayaan.world.entity.monster.creaking.Creaking;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.schedule.Activity;

public class CreakingAi {
    static ActivityData<Creaking> initCoreActivity() {
        return ActivityData.create(Activity.CORE, 0, ImmutableList.of((Object)new Swim<Creaking>(0.8f){

            @Override
            protected boolean checkExtraStartConditions(ServerLevel level, Creaking body) {
                return body.canMove() && super.checkExtraStartConditions(level, (LivingEntity)body);
            }
        }, (Object)new LookAtTargetSink(45, 90), (Object)new MoveToTargetSink()));
    }

    static ActivityData<Creaking> initIdleActivity() {
        return ActivityData.create(Activity.IDLE, 10, ImmutableList.of(StartAttacking.create((level, mob) -> {
            Creaking creaking;
            return mob instanceof Creaking && (creaking = mob).isActive();
        }, (level, mob) -> mob.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER)), SetEntityLookTargetSometimes.create(8.0f, UniformInt.of(30, 60)), new RunOne(ImmutableList.of((Object)Pair.of(RandomStroll.stroll(0.3f), (Object)2), (Object)Pair.of(SetWalkTargetFromLookTarget.create(0.3f, 3), (Object)2), (Object)Pair.of((Object)new DoNothing(30, 60), (Object)1)))));
    }

    static ActivityData<Creaking> initFightActivity(Creaking body) {
        return ActivityData.create(Activity.FIGHT, 10, ImmutableList.of(SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0f), MeleeAttack.create(Creaking::canMove, 40), StopAttackingIfTargetInvalid.create((level, target) -> !CreakingAi.isAttackTargetStillReachable(body, target))), ImmutableSet.of((Object)Pair.of(MemoryModuleType.ATTACK_TARGET, (Object)((Object)MemoryStatus.VALUE_PRESENT))));
    }

    private static boolean isAttackTargetStillReachable(Creaking creaking, LivingEntity target) {
        Optional<List<Player>> visibleAttackablePlayers = creaking.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYERS);
        return visibleAttackablePlayers.map(players -> {
            Player player;
            return target instanceof Player && players.contains(player = (Player)target);
        }).orElse(false);
    }

    protected static List<ActivityData<Creaking>> getActivities(Creaking creaking) {
        return List.of(CreakingAi.initCoreActivity(), CreakingAi.initIdleActivity(), CreakingAi.initFightActivity(creaking));
    }

    public static void updateActivity(Creaking creaking) {
        if (!creaking.canMove()) {
            creaking.getBrain().useDefaultActivity();
        } else {
            creaking.getBrain().setActiveActivityToFirstValid((List<Activity>)ImmutableList.of((Object)Activity.FIGHT, (Object)Activity.IDLE));
        }
    }
}

