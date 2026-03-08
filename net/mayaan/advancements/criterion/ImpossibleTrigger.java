/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.mayaan.advancements.CriterionTrigger;
import net.mayaan.advancements.CriterionTriggerInstance;
import net.mayaan.server.PlayerAdvancements;
import net.mayaan.world.level.storage.loot.ValidationContextSource;

public class ImpossibleTrigger
implements CriterionTrigger<TriggerInstance> {
    @Override
    public void addPlayerListener(PlayerAdvancements player, CriterionTrigger.Listener<TriggerInstance> listener) {
    }

    @Override
    public void removePlayerListener(PlayerAdvancements player, CriterionTrigger.Listener<TriggerInstance> listener) {
    }

    @Override
    public void removePlayerListeners(PlayerAdvancements player) {
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance() implements CriterionTriggerInstance
    {
        public static final Codec<TriggerInstance> CODEC = MapCodec.unitCodec((Object)new TriggerInstance());

        @Override
        public void validate(ValidationContextSource validator) {
        }
    }
}

