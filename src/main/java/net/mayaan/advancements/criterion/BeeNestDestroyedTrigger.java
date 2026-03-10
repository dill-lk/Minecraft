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
import net.mayaan.advancements.criterion.ItemPredicate;
import net.mayaan.advancements.criterion.MinMaxBounds;
import net.mayaan.advancements.criterion.SimpleCriterionTrigger;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockState;

public class BeeNestDestroyedTrigger
extends SimpleCriterionTrigger<TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, BlockState state, ItemStack itemStack, int numBeesInside) {
        this.trigger(player, t -> t.matches(state, itemStack, numBeesInside));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<Holder<Block>> block, Optional<ItemPredicate> item, MinMaxBounds.Ints beesInside) implements SimpleCriterionTrigger.SimpleInstance
    {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)BuiltInRegistries.BLOCK.holderByNameCodec().optionalFieldOf("block").forGetter(TriggerInstance::block), (App)ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("num_bees_inside", (Object)MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::beesInside)).apply((Applicative)i, TriggerInstance::new));

        public static Criterion<TriggerInstance> destroyedBeeNest(Block block, ItemPredicate.Builder itemPredicate, MinMaxBounds.Ints numBeesInside) {
            return CriteriaTriggers.BEE_NEST_DESTROYED.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(block.builtInRegistryHolder()), Optional.of(itemPredicate.build()), numBeesInside));
        }

        public boolean matches(BlockState state, ItemStack itemStack, int numBeesInside) {
            if (this.block.isPresent() && !state.is(this.block.get())) {
                return false;
            }
            if (this.item.isPresent() && !this.item.get().test(itemStack)) {
                return false;
            }
            return this.beesInside.matches(numBeesInside);
        }
    }
}

