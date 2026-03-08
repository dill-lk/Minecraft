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
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.entity.animal.Animal;
import net.mayaan.world.level.storage.loot.LootContext;
import net.mayaan.world.level.storage.loot.Validatable;
import net.mayaan.world.level.storage.loot.ValidationContextSource;

public class TameAnimalTrigger
extends SimpleCriterionTrigger<TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, Animal animal) {
        LootContext animalContext = EntityPredicate.createContext(player, animal);
        this.trigger(player, (T t) -> t.matches(animalContext));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entity) implements SimpleCriterionTrigger.SimpleInstance
    {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::entity)).apply((Applicative)i, TriggerInstance::new));

        public static Criterion<TriggerInstance> tamedAnimal() {
            return CriteriaTriggers.TAME_ANIMAL.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty()));
        }

        public static Criterion<TriggerInstance> tamedAnimal(EntityPredicate.Builder entity) {
            return CriteriaTriggers.TAME_ANIMAL.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity))));
        }

        public boolean matches(LootContext animal) {
            return this.entity.isEmpty() || this.entity.get().matches(animal);
        }

        @Override
        public void validate(ValidationContextSource validator) {
            SimpleCriterionTrigger.SimpleInstance.super.validate(validator);
            Validatable.validate(validator.entityContext(), "entity", this.entity);
        }
    }
}

