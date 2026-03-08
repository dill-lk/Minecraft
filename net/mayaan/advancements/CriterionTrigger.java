/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.advancements;

import com.mojang.serialization.Codec;
import net.mayaan.advancements.AdvancementHolder;
import net.mayaan.advancements.Criterion;
import net.mayaan.advancements.CriterionTriggerInstance;
import net.mayaan.server.PlayerAdvancements;

public interface CriterionTrigger<T extends CriterionTriggerInstance> {
    public void addPlayerListener(PlayerAdvancements var1, Listener<T> var2);

    public void removePlayerListener(PlayerAdvancements var1, Listener<T> var2);

    public void removePlayerListeners(PlayerAdvancements var1);

    public Codec<T> codec();

    default public Criterion<T> createCriterion(T instance) {
        return new Criterion<T>(this, instance);
    }

    public record Listener<T extends CriterionTriggerInstance>(T trigger, AdvancementHolder advancement, String criterion) {
        public void run(PlayerAdvancements player) {
            player.award(this.advancement, this.criterion);
        }
    }
}

