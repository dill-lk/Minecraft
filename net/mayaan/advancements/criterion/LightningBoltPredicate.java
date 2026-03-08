/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.advancements.criterion;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.mayaan.advancements.criterion.EntityPredicate;
import net.mayaan.advancements.criterion.EntitySubPredicate;
import net.mayaan.advancements.criterion.EntitySubPredicates;
import net.mayaan.advancements.criterion.MinMaxBounds;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LightningBolt;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record LightningBoltPredicate(MinMaxBounds.Ints blocksSetOnFire, Optional<EntityPredicate> entityStruck) implements EntitySubPredicate
{
    public static final MapCodec<LightningBoltPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)MinMaxBounds.Ints.CODEC.optionalFieldOf("blocks_set_on_fire", (Object)MinMaxBounds.Ints.ANY).forGetter(LightningBoltPredicate::blocksSetOnFire), (App)EntityPredicate.CODEC.optionalFieldOf("entity_struck").forGetter(LightningBoltPredicate::entityStruck)).apply((Applicative)i, LightningBoltPredicate::new));

    public static LightningBoltPredicate blockSetOnFire(MinMaxBounds.Ints count) {
        return new LightningBoltPredicate(count, Optional.empty());
    }

    public MapCodec<LightningBoltPredicate> codec() {
        return EntitySubPredicates.LIGHTNING;
    }

    @Override
    public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
        if (!(entity instanceof LightningBolt)) {
            return false;
        }
        LightningBolt bolt = (LightningBolt)entity;
        return this.blocksSetOnFire.matches(bolt.getBlocksSetOnFire()) && (this.entityStruck.isEmpty() || bolt.getHitEntities().anyMatch(e -> this.entityStruck.get().matches(level, position, (Entity)e)));
    }
}

