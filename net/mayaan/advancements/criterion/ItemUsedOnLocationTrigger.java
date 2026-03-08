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
import java.util.Arrays;
import java.util.Optional;
import net.mayaan.advancements.CriteriaTriggers;
import net.mayaan.advancements.Criterion;
import net.mayaan.advancements.criterion.ContextAwarePredicate;
import net.mayaan.advancements.criterion.EntityPredicate;
import net.mayaan.advancements.criterion.ItemPredicate;
import net.mayaan.advancements.criterion.LocationPredicate;
import net.mayaan.advancements.criterion.SimpleCriterionTrigger;
import net.mayaan.advancements.criterion.StatePropertiesPredicate;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.util.StringRepresentable;
import net.mayaan.world.item.ItemInstance;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.Property;
import net.mayaan.world.level.storage.loot.LootContext;
import net.mayaan.world.level.storage.loot.LootParams;
import net.mayaan.world.level.storage.loot.Validatable;
import net.mayaan.world.level.storage.loot.ValidationContextSource;
import net.mayaan.world.level.storage.loot.parameters.LootContextParamSets;
import net.mayaan.world.level.storage.loot.parameters.LootContextParams;
import net.mayaan.world.level.storage.loot.predicates.LocationCheck;
import net.mayaan.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.mayaan.world.level.storage.loot.predicates.LootItemCondition;
import net.mayaan.world.level.storage.loot.predicates.MatchTool;

public class ItemUsedOnLocationTrigger
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

        public static Criterion<TriggerInstance> placedBlock(Block block) {
            ContextAwarePredicate location = ContextAwarePredicate.create(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).build());
            return CriteriaTriggers.PLACED_BLOCK.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(location)));
        }

        public static Criterion<TriggerInstance> placedBlock(LootItemCondition.Builder ... conditions) {
            ContextAwarePredicate location = ContextAwarePredicate.create((LootItemCondition[])Arrays.stream(conditions).map(LootItemCondition.Builder::build).toArray(LootItemCondition[]::new));
            return CriteriaTriggers.PLACED_BLOCK.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(location)));
        }

        public static <T extends Comparable<T>> Criterion<TriggerInstance> placedBlockWithProperties(Block block, Property<T> property, String propertyValue) {
            StatePropertiesPredicate.Builder predicateBuilder = StatePropertiesPredicate.Builder.properties().hasProperty(property, propertyValue);
            ContextAwarePredicate location = ContextAwarePredicate.create(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(predicateBuilder).build());
            return CriteriaTriggers.PLACED_BLOCK.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(location)));
        }

        public static Criterion<TriggerInstance> placedBlockWithProperties(Block block, Property<Boolean> property, boolean propertyValue) {
            return TriggerInstance.placedBlockWithProperties(block, property, String.valueOf(propertyValue));
        }

        public static Criterion<TriggerInstance> placedBlockWithProperties(Block block, Property<Integer> property, int propertyValue) {
            return TriggerInstance.placedBlockWithProperties(block, property, String.valueOf(propertyValue));
        }

        public static <T extends Comparable<T> & StringRepresentable> Criterion<TriggerInstance> placedBlockWithProperties(Block block, Property<T> properties, T propertyValue) {
            return TriggerInstance.placedBlockWithProperties(block, properties, ((StringRepresentable)propertyValue).getSerializedName());
        }

        private static TriggerInstance itemUsedOnLocation(LocationPredicate.Builder location, ItemPredicate.Builder item) {
            ContextAwarePredicate predicate = ContextAwarePredicate.create(LocationCheck.checkLocation(location).build(), MatchTool.toolMatches(item).build());
            return new TriggerInstance(Optional.empty(), Optional.of(predicate));
        }

        public static Criterion<TriggerInstance> itemUsedOnBlock(LocationPredicate.Builder location, ItemPredicate.Builder item) {
            return CriteriaTriggers.ITEM_USED_ON_BLOCK.createCriterion(TriggerInstance.itemUsedOnLocation(location, item));
        }

        public static Criterion<TriggerInstance> allayDropItemOnBlock(LocationPredicate.Builder location, ItemPredicate.Builder item) {
            return CriteriaTriggers.ALLAY_DROP_ITEM_ON_BLOCK.createCriterion(TriggerInstance.itemUsedOnLocation(location, item));
        }

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

