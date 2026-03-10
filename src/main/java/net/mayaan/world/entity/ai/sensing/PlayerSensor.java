/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 */
package net.mayaan.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntitySelector;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.Brain;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.sensing.Sensor;
import net.mayaan.world.entity.player.Player;

public class PlayerSensor
extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYERS);
    }

    @Override
    protected void doTick(ServerLevel level, LivingEntity body) {
        List players = level.players().stream().filter(EntitySelector.NO_SPECTATORS).filter(player -> body.closerThan((Entity)player, this.getFollowDistance(body))).sorted(Comparator.comparingDouble(body::distanceToSqr)).collect(Collectors.toList());
        Brain<? extends LivingEntity> brain = body.getBrain();
        brain.setMemory(MemoryModuleType.NEAREST_PLAYERS, players);
        List visiblePlayers = players.stream().filter(livingEntity -> PlayerSensor.isEntityTargetable(level, body, livingEntity)).collect(Collectors.toList());
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, visiblePlayers.isEmpty() ? null : (Player)visiblePlayers.get(0));
        List<Player> visibleAttackablePlayers = visiblePlayers.stream().filter(livingEntity -> PlayerSensor.isEntityAttackable(level, body, livingEntity)).toList();
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYERS, visibleAttackablePlayers);
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, visibleAttackablePlayers.isEmpty() ? null : visibleAttackablePlayers.get(0));
    }

    protected double getFollowDistance(LivingEntity body) {
        return body.getAttributeValue(Attributes.FOLLOW_RANGE);
    }
}

