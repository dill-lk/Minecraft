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
import net.mayaan.advancements.criterion.MinMaxBounds;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;

public record LightPredicate(MinMaxBounds.Ints composite) {
    public static final Codec<LightPredicate> CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Ints.CODEC.optionalFieldOf("light", (Object)MinMaxBounds.Ints.ANY).forGetter(LightPredicate::composite)).apply((Applicative)i, LightPredicate::new));

    public boolean matches(ServerLevel level, BlockPos pos) {
        if (!level.isLoaded(pos)) {
            return false;
        }
        return this.composite.matches(level.getMaxLocalRawBrightness(pos));
    }

    public static class Builder {
        private MinMaxBounds.Ints composite = MinMaxBounds.Ints.ANY;

        public static Builder light() {
            return new Builder();
        }

        public Builder setComposite(MinMaxBounds.Ints composite) {
            this.composite = composite;
            return this;
        }

        public LightPredicate build() {
            return new LightPredicate(this.composite);
        }
    }
}

