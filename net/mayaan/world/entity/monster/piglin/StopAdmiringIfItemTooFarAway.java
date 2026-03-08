/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.Applicative
 */
package net.mayaan.world.entity.monster.piglin;

import com.mojang.datafixers.kinds.Applicative;
import java.util.Optional;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.behavior.BehaviorControl;
import net.mayaan.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.item.ItemEntity;
import net.mayaan.world.entity.monster.piglin.Piglin;

public class StopAdmiringIfItemTooFarAway<E extends Piglin> {
    public static BehaviorControl<LivingEntity> create(int maxDistanceToItem) {
        return BehaviorBuilder.create(i -> i.group(i.present(MemoryModuleType.ADMIRING_ITEM), i.registered(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM)).apply((Applicative)i, (admiring, nearest) -> (level, body, timestamp) -> {
            if (!body.getOffhandItem().isEmpty()) {
                return false;
            }
            Optional nearestVisibleWantedItem = i.tryGet(nearest);
            if (nearestVisibleWantedItem.isPresent() && ((ItemEntity)nearestVisibleWantedItem.get()).closerThan(body, maxDistanceToItem)) {
                return false;
            }
            admiring.erase();
            return true;
        }));
    }
}

