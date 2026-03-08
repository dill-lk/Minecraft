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
import net.mayaan.advancements.criterion.ContextAwarePredicate;
import net.mayaan.advancements.criterion.EntityPredicate;
import net.mayaan.advancements.criterion.SimpleCriterionTrigger;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.item.ItemInstance;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.storage.loot.LootContext;
import net.mayaan.world.level.storage.loot.LootParams;
import net.mayaan.world.level.storage.loot.Validatable;
import net.mayaan.world.level.storage.loot.ValidationContextSource;
import net.mayaan.world.level.storage.loot.parameters.LootContextParamSets;
import net.mayaan.world.level.storage.loot.parameters.LootContextParams;

public class AnyBlockInteractionTrigger
extends SimpleCriterionTrigger<TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, BlockPos pos, ItemInstance tool) {
        ServerLevel level = player.level();
        BlockState state = level.getBlockState(pos);
        LootParams params = new LootParams.Builder(level).withParameter(LootContextParams.ORIGIN, pos.getCenter()).withParameter(LootContextParams.THIS_ENTITY, player).withParameter(LootContextParams.BLOCK_STATE, state).withParameter(LootContextParams.TOOL, tool).create(LootContextParamSets.ADVANCEMENT_LOCATION);
        LootContext context = new LootContext.Builder(params).create(Optional.empty());
        this.trigger(player, t -> t.matches(context));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> location) implements SimpleCriterionTrigger.SimpleInstance
    {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)ContextAwarePredicate.CODEC.optionalFieldOf("location").forGetter(TriggerInstance::location)).apply((Applicative)i, TriggerInstance::new));

        public boolean matches(LootContext locationContext) {
            return this.location.isEmpty() || this.location.get().matches(locationContext);
        }

        @Override
        public void validate(ValidationContextSource validator) {
            SimpleCriterionTrigger.SimpleInstance.super.validate(validator);
            Validatable.validate(validator.context(LootContextParamSets.ADVANCEMENT_LOCATION), "location", this.location);
        }
    }
}

