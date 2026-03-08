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

public class ClampedNormalFloat
extends FloatProvider {
    public static final MapCodec<ClampedNormalFloat> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("mean").forGetter(c -> Float.valueOf(c.mean)), (App)Codec.FLOAT.fieldOf("deviation").forGetter(c -> Float.valueOf(c.deviation)), (App)Codec.FLOAT.fieldOf("min").forGetter(c -> Float.valueOf(c.min)), (App)Codec.FLOAT.fieldOf("max").forGetter(c -> Float.valueOf(c.max))).apply((Applicative)i, ClampedNormalFloat::new)).validate(c -> {
        if (c.max < c.min) {
            return DataResult.error(() -> "Max must be larger than min: [" + c.min + ", " + c.max + "]");
        }
        return DataResult.success((Object)c);
    });
    private final float mean;
    private final float deviation;
    private final float min;
    private final float max;

    public static ClampedNormalFloat of(float mean, float deviation, float min, float max) {
        return new ClampedNormalFloat(mean, deviation, min, max);
    }

    private ClampedNormalFloat(float mean, float deviation, float min, float max) {
        this.mean = mean;
        this.deviation = deviation;
        this.min = min;
        this.max = max;
    }

    @Override
    public float sample(RandomSource random) {
        return ClampedNormalFloat.sample(random, this.mean, this.deviation, this.min, this.max);
    }

    public static float sample(RandomSource random, float mean, float deviation, float min, float max) {
        return Mth.clamp(Mth.normal(random, mean, deviation), min, max);
    }

    @Override
    public float getMinValue() {
        return this.min;
    }

    @Override
    public float getMaxValue() {
        return this.max;
    }

    @Override
    public FloatProviderType<?> getType() {
        return FloatProviderType.CLAMPED_NORMAL;
    }

    public String toString() {
        return "normal(" + this.mean + ", " + this.deviation + ") in [" + this.min + "-" + this.max + "]";
    }
}

