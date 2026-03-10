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
import net.mayaan.advancements.criterion.DamagePredicate;
import net.mayaan.advancements.criterion.EntityPredicate;
import net.mayaan.advancements.criterion.SimpleCriterionTrigger;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.damagesource.DamageSource;

public class EntityHurtPlayerTrigger
extends SimpleCriterionTrigger<TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, DamageSource source, float originalDamage, float actualDamage, boolean blocked) {
        this.trigger(player, t -> t.matches(player, source, originalDamage, actualDamage, blocked));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<DamagePredicate> damage) implements SimpleCriterionTrigger.SimpleInstance
    {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)DamagePredicate.CODEC.optionalFieldOf("damage").forGetter(TriggerInstance::damage)).apply((Applicative)i, TriggerInstance::new));

        public static Criterion<TriggerInstance> entityHurtPlayer() {
            return CriteriaTriggers.ENTITY_HURT_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty()));
        }

        public static Criterion<TriggerInstance> entityHurtPlayer(DamagePredicate damage) {
            return CriteriaTriggers.ENTITY_HURT_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(damage)));
        }

        public static Criterion<TriggerInstance> entityHurtPlayer(DamagePredicate.Builder damage) {
            return CriteriaTriggers.ENTITY_HURT_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(damage.build())));
        }

        public boolean matches(ServerPlayer player, DamageSource source, float originalDamage, float actualDamage, boolean blocked) {
            return !this.damage.isPresent() || this.damage.get().matches(player, source, originalDamage, actualDamage, blocked);
        }
    }
}

