/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.advancements.criterion;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.mayaan.advancements.criterion.EntitySubPredicate;
import net.mayaan.advancements.criterion.EntitySubPredicates;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.animal.sheep.Sheep;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record SheepPredicate(Optional<Boolean> sheared) implements EntitySubPredicate
{
    public static final MapCodec<SheepPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.optionalFieldOf("sheared").forGetter(SheepPredicate::sheared)).apply((Applicative)i, SheepPredicate::new));

    public MapCodec<SheepPredicate> codec() {
        return EntitySubPredicates.SHEEP;
    }

    @Override
    public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
        if (entity instanceof Sheep) {
            Sheep sheep = (Sheep)entity;
            return !this.sheared.isPresent() || sheep.isSheared() == this.sheared.get().booleanValue();
        }
        return false;
    }

    public static SheepPredicate hasWool() {
        return new SheepPredicate(Optional.of(false));
    }
}

