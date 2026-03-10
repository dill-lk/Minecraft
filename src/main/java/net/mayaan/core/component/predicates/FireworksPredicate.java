/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.core.component.predicates;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.mayaan.advancements.criterion.CollectionPredicate;
import net.mayaan.advancements.criterion.MinMaxBounds;
import net.mayaan.advancements.criterion.SingleComponentItemPredicate;
import net.mayaan.core.component.DataComponentType;
import net.mayaan.core.component.DataComponents;
import net.mayaan.core.component.predicates.FireworkExplosionPredicate;
import net.mayaan.world.item.component.FireworkExplosion;
import net.mayaan.world.item.component.Fireworks;

public record FireworksPredicate(Optional<CollectionPredicate<FireworkExplosion, FireworkExplosionPredicate.FireworkPredicate>> explosions, MinMaxBounds.Ints flightDuration) implements SingleComponentItemPredicate<Fireworks>
{
    public static final Codec<FireworksPredicate> CODEC = RecordCodecBuilder.create(i -> i.group((App)CollectionPredicate.codec(FireworkExplosionPredicate.FireworkPredicate.CODEC).optionalFieldOf("explosions").forGetter(FireworksPredicate::explosions), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("flight_duration", (Object)MinMaxBounds.Ints.ANY).forGetter(FireworksPredicate::flightDuration)).apply((Applicative)i, FireworksPredicate::new));

    @Override
    public DataComponentType<Fireworks> componentType() {
        return DataComponents.FIREWORKS;
    }

    @Override
    public boolean matches(Fireworks value) {
        if (this.explosions.isPresent() && !this.explosions.get().test(value.explosions())) {
            return false;
        }
        return this.flightDuration.matches(value.flightDuration());
    }
}

