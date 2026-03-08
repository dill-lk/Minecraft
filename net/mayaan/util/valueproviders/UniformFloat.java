/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.DataResult
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.util.valueproviders;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.util.valueproviders.FloatProvider;
import net.mayaan.util.valueproviders.FloatProviderType;

public class UniformFloat
extends FloatProvider {
    public static final MapCodec<UniformFloat> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("min_inclusive").forGetter(u -> Float.valueOf(u.minInclusive)), (App)Codec.FLOAT.fieldOf("max_exclusive").forGetter(u -> Float.valueOf(u.maxExclusive))).apply((Applicative)i, UniformFloat::new)).validate(u -> {
        if (u.maxExclusive <= u.minInclusive) {
            return DataResult.error(() -> "Max must be larger than min, min_inclusive: " + u.minInclusive + ", max_exclusive: " + u.maxExclusive);
        }
        return DataResult.success((Object)u);
    });
    private final float minInclusive;
    private final float maxExclusive;

    private UniformFloat(float minInclusive, float maxExclusive) {
        this.minInclusive = minInclusive;
        this.maxExclusive = maxExclusive;
    }

    public static UniformFloat of(float minInclusive, float maxExclusive) {
        if (maxExclusive <= minInclusive) {
            throw new IllegalArgumentException("Max must exceed min");
        }
        return new UniformFloat(minInclusive, maxExclusive);
    }

    @Override
    public float sample(RandomSource random) {
        return Mth.randomBetween(random, this.minInclusive, this.maxExclusive);
    }

    @Override
    public float getMinValue() {
        return this.minInclusive;
    }

    @Override
    public float getMaxValue() {
        return this.maxExclusive;
    }

    @Override
    public FloatProviderType<?> getType() {
        return FloatProviderType.UNIFORM;
    }

    public String toString() {
        return "[" + this.minInclusive + "-" + this.maxExclusive + "]";
    }
}

