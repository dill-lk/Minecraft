/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableSet
 */
package net.mayaan.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.SimpleContainer;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.behavior.Behavior;
import net.mayaan.world.entity.ai.behavior.BehaviorUtils;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.memory.MemoryStatus;
import net.mayaan.world.entity.npc.villager.Villager;
import net.mayaan.world.entity.npc.villager.VillagerProfession;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;

public class TradeWithVillager
extends Behavior<Villager> {
    private Set<Item> trades = ImmutableSet.of();

    public TradeWithVillager() {
        super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, (Object)((Object)MemoryStatus.VALUE_PRESENT), MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, (Object)((Object)MemoryStatus.VALUE_PRESENT)));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Villager body) {
        return BehaviorUtils.targetIsValid(body.getBrain(), MemoryModuleType.INTERACTION_TARGET, EntityType.VILLAGER);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) {
        return this.checkExtraStartConditions(level, body);
    }

    @Override
    protected void start(ServerLevel level, Villager myBody, long timestamp) {
        Villager target = (Villager)myBody.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        BehaviorUtils.lockGazeAndWalkToEachOther(myBody, target, 0.5f, 2);
        this.trades = TradeWithVillager.figureOutWhatIAmWillingToTrade(myBody, target);
    }

    @Override
    protected void tick(ServerLevel level, Villager body, long timestamp) {
        Villager target = (Villager)body.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        if (body.distanceToSqr(target) > 5.0) {
            return;
        }
        BehaviorUtils.lockGazeAndWalkToEachOther(body, target, 0.5f, 2);
        body.gossip(level, target, timestamp);
        boolean isFarmer = body.getVillagerData().profession().is(VillagerProfession.FARMER);
        if (body.hasExcessFood() && (isFarmer || target.wantsMoreFood())) {
            TradeWithVillager.throwHalfStack(body, Villager.FOOD_POINTS.keySet(), target);
        }
        if (isFarmer && body.getInventory().countItem(Items.WHEAT) > Items.WHEAT.getDefaultMaxStackSize() / 2) {
            TradeWithVillager.throwHalfStack(body, (Set<Item>)ImmutableSet.of((Object)Items.WHEAT), target);
        }
        if (!this.trades.isEmpty() && body.getInventory().hasAnyOf(this.trades)) {
            TradeWithVillager.throwHalfStack(body, this.trades, target);
        }
    }

    @Override
    protected void stop(ServerLevel level, Villager body, long timestamp) {
        body.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
    }

    private static Set<Item> figureOutWhatIAmWillingToTrade(Villager myBody, Villager target) {
        ImmutableSet<Item> targetItems = target.getVillagerData().profession().value().requestedItems();
        ImmutableSet<Item> selfItems = myBody.getVillagerData().profession().value().requestedItems();
        return targetItems.stream().filter(entry -> !selfItems.contains(entry)).collect(Collectors.toSet());
    }

    private static void throwHalfStack(Villager villager, Set<Item> items, LivingEntity target) {
        SimpleContainer inventory = villager.getInventory();
        ItemStack toThrow = ItemStack.EMPTY;
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            int count;
            Item item;
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack.isEmpty() || !items.contains(item = itemStack.getItem())) continue;
            if (itemStack.getCount() > itemStack.getMaxStackSize() / 2) {
                count = itemStack.getCount() / 2;
            } else {
                if (itemStack.getCount() <= 24) continue;
                count = itemStack.getCount() - 24;
            }
            itemStack.shrink(count);
            toThrow = new ItemStack(item, count);
            break;
        }
        if (!toThrow.isEmpty()) {
            BehaviorUtils.throwItem(villager, toThrow, target.position());
        }
    }
}

