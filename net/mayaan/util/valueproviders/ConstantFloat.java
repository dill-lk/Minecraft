/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.util.valueproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.mayaan.util.RandomSource;
import net.mayaan.util.valueproviders.FloatProvider;
import net.mayaan.util.valueproviders.FloatProviderType;

public class ConstantFloat
extends FloatProvider {
    public static final ConstantFloat ZERO = new ConstantFloat(0.0f);
    public static final MapCodec<ConstantFloat> CODEC = Codec.FLOAT.fieldOf("value").xmap(ConstantFloat::of, ConstantFloat::getValue);
    private final float value;

    public static ConstantFloat of(float value) {
        if (value == 0.0f) {
            return ZERO;
        }
        return new ConstantFloat(value);
    }

    private ConstantFloat(float value) {
        this.value = value;
    }

    public float getValue() {
        return this.value;
    }

    @Override
    public float sample(RandomSource random) {
        return this.value;
    }

    @Override
    public float getMinValue() {
        return this.value;
    }

    @Override
    public float getMaxValue() {
        return this.value;
    }

    @Override
    public FloatProviderType<?> getType() {
        return FloatProviderType.CONSTANT;
    }

    public String toString() {
        return Float.toString(this.value);
    }
}

