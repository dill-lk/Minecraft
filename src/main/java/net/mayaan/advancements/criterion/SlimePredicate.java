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
import net.mayaan.advancements.criterion.EntitySubPredicate;
import net.mayaan.advancements.criterion.EntitySubPredicates;
import net.mayaan.advancements.criterion.MinMaxBounds;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.monster.Slime;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record SlimePredicate(MinMaxBounds.Ints size) implements EntitySubPredicate
{
    public static final MapCodec<SlimePredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)MinMaxBounds.Ints.CODEC.optionalFieldOf("size", (Object)MinMaxBounds.Ints.ANY).forGetter(SlimePredicate::size)).apply((Applicative)i, SlimePredicate::new));

    public static SlimePredicate sized(MinMaxBounds.Ints size) {
        return new SlimePredicate(size);
    }

    @Override
    public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
        if (entity instanceof Slime) {
            Slime slime = (Slime)entity;
            return this.size.matches(slime.getSize());
        }
        return false;
    }

    public MapCodec<SlimePredicate> codec() {
        return EntitySubPredicates.SLIME;
    }
}

