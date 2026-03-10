/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 */
package net.mayaan.advancements.criterion;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import net.mayaan.advancements.CriterionTrigger;
import net.mayaan.advancements.CriterionTriggerInstance;
import net.mayaan.advancements.criterion.ContextAwarePredicate;
import net.mayaan.advancements.criterion.EntityPredicate;
import net.mayaan.server.PlayerAdvancements;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.level.storage.loot.LootContext;
import net.mayaan.world.level.storage.loot.Validatable;
import net.mayaan.world.level.storage.loot.ValidationContextSource;

public abstract class SimpleCriterionTrigger<T extends SimpleInstance>
implements CriterionTrigger<T> {
    private final Map<PlayerAdvancements, Set<CriterionTrigger.Listener<T>>> players = Maps.newIdentityHashMap();

    @Override
    public final void addPlayerListener(PlayerAdvancements player, CriterionTrigger.Listener<T> listener) {
        this.players.computeIfAbsent(player, k -> Sets.newHashSet()).add(listener);
    }

    @Override
    public final void removePlayerListener(PlayerAdvancements player, CriterionTrigger.Listener<T> listener) {
        Set<CriterionTrigger.Listener<T>> listeners = this.players.get(player);
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                this.players.remove(player);
            }
        }
    }

    @Override
    public final void removePlayerListeners(PlayerAdvancements player) {
        this.players.remove(player);
    }

    protected void trigger(ServerPlayer player, Predicate<T> matcher) {
        PlayerAdvancements advancements = player.getAdvancements();
        Set<CriterionTrigger.Listener<T>> allListeners = this.players.get(advancements);
        if (allListeners == null || allListeners.isEmpty()) {
            return;
        }
        LootContext playerContext = EntityPredicate.createContext(player, player);
        List listeners = null;
        for (CriterionTrigger.Listener<Object> listener : allListeners) {
            Optional<ContextAwarePredicate> predicate;
            SimpleInstance triggerInstance = (SimpleInstance)listener.trigger();
            if (!matcher.test(triggerInstance) || !(predicate = triggerInstance.player()).isEmpty() && !predicate.get().matches(playerContext)) continue;
            if (listeners == null) {
                listeners = Lists.newArrayList();
            }
            listeners.add(listener);
        }
        if (listeners != null) {
            for (CriterionTrigger.Listener<Object> listener : listeners) {
                listener.run(advancements);
            }
        }
    }

    public static interface SimpleInstance
    extends CriterionTriggerInstance {
        @Override
        default public void validate(ValidationContextSource validator) {
            Validatable.validate(validator.entityContext(), "player", this.player());
        }

        public Optional<ContextAwarePredicate> player();
    }
}

