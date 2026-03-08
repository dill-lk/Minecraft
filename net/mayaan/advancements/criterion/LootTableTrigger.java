/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.advancements.criterion;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.mayaan.advancements.CriteriaTriggers;
import net.mayaan.advancements.Criterion;
import net.mayaan.advancements.criterion.ContextAwarePredicate;
import net.mayaan.advancements.criterion.EntityPredicate;
import net.mayaan.advancements.criterion.SimpleCriterionTrigger;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.level.storage.loot.LootTable;

public class LootTableTrigger
extends SimpleCriterionTrigger<TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    @Override
    public void trigger(ServerPlayer player, ResourceKey<LootTable> lootTable) {
        this.trigger(player, (T t) -> t.matches(lootTable));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, ResourceKey<LootTable> lootTable) implements SimpleCriterionTrigger.SimpleInstance
    {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)LootTable.KEY_CODEC.fieldOf("loot_table").forGetter(TriggerInstance::lootTable)).apply((Applicative)i, TriggerInstance::new));

        public static Criterion<TriggerInstance> lootTableUsed(ResourceKey<LootTable> lootTable) {
            return CriteriaTriggers.GENERATE_LOOT.createCriterion(new TriggerInstance(Optional.empty(), lootTable));
        }

        public boolean matches(ResourceKey<LootTable> lootTable) {
            return this.lootTable == lootTable;
        }
    }
}

