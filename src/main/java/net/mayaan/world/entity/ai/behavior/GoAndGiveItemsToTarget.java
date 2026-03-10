/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.behavior;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.behavior.Behavior;
import net.mayaan.world.entity.ai.behavior.BehaviorUtils;
import net.mayaan.world.entity.ai.behavior.PositionTracker;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.memory.MemoryStatus;
import net.mayaan.world.entity.npc.InventoryCarrier;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.phys.Vec3;

public class GoAndGiveItemsToTarget<E extends LivingEntity>
extends Behavior<E> {
    private static final int CLOSE_ENOUGH_DISTANCE_TO_TARGET = 3;
    private static final int ITEM_PICKUP_COOLDOWN_AFTER_THROWING = 60;
    private final Vec3 throwVelocity;
    private final Function<LivingEntity, Optional<PositionTracker>> targetPositionGetter;
    private final float speedModifier;
    private final ItemThrower<E> itemThrower;

    public GoAndGiveItemsToTarget(Function<LivingEntity, Optional<PositionTracker>> targetPositionGetter, float speedModifier, int timeoutDuration, ItemThrower<E> itemThrower) {
        super(Map.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryStatus.REGISTERED), timeoutDuration);
        this.targetPositionGetter = targetPositionGetter;
        this.speedModifier = speedModifier;
        this.itemThrower = itemThrower;
        this.throwVelocity = new Vec3(0.2f, 0.3f, 0.2f);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E body) {
        return this.canThrowItemToTarget(body);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, E body, long timestamp) {
        return this.canThrowItemToTarget(body);
    }

    @Override
    protected void start(ServerLevel level, E body, long timestamp) {
        this.targetPositionGetter.apply((LivingEntity)body).ifPresent(positionTracker -> BehaviorUtils.setWalkAndLookTargetMemories(body, positionTracker, this.speedModifier, 3));
    }

    @Override
    protected void tick(ServerLevel level, E body, long timestamp) {
        ItemStack item;
        Optional<PositionTracker> targetPosition = this.targetPositionGetter.apply((LivingEntity)body);
        if (targetPosition.isEmpty()) {
            return;
        }
        PositionTracker depositTarget = targetPosition.get();
        Vec3 depositPosition = depositTarget.currentPosition();
        double distanceToTarget = depositPosition.distanceTo(((Entity)body).getEyePosition());
        if (distanceToTarget < 3.0 && !(item = ((InventoryCarrier)body).getInventory().removeItem(0, 1)).isEmpty()) {
            BehaviorUtils.throwItem(body, item, depositPosition.add(0.0, 1.0, 0.0), this.throwVelocity, 0.2f);
            this.itemThrower.onItemThrown(level, body, item, depositTarget.currentBlockPosition());
            ((LivingEntity)body).getBrain().setMemory(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, 60);
        }
    }

    private boolean canThrowItemToTarget(E body) {
        if (((InventoryCarrier)body).getInventory().isEmpty()) {
            return false;
        }
        Optional<PositionTracker> positionTracker = this.targetPositionGetter.apply((LivingEntity)body);
        return positionTracker.isPresent();
    }

    @FunctionalInterface
    public static interface ItemThrower<E> {
        public void onItemThrown(ServerLevel var1, E var2, ItemStack var3, BlockPos var4);
    }
}

