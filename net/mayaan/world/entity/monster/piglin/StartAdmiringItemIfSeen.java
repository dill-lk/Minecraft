/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.Applicative
 */
package net.mayaan.world.entity.monster.piglin;

import com.mojang.datafixers.kinds.Applicative;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.behavior.BehaviorControl;
import net.mayaan.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.item.ItemEntity;
import net.mayaan.world.entity.monster.piglin.PiglinAi;

public class StartAdmiringItemIfSeen {
    public static BehaviorControl<LivingEntity> create(int admireDuration) {
        return BehaviorBuilder.create(i -> i.group(i.present(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM), i.absent(MemoryModuleType.ADMIRING_ITEM), i.absent(MemoryModuleType.ADMIRING_DISABLED), i.absent(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM)).apply((Applicative)i, (nearestItem, admiring, admiringDisabled, walkDisabled) -> (level, body, timestamp) -> {
            ItemEntity itemEntity = (ItemEntity)i.get(nearestItem);
            if (!PiglinAi.isLovedItem(itemEntity.getItem())) {
                return false;
            }
            admiring.setWithExpiry(true, admireDuration);
            return true;
        }));
    }
}

