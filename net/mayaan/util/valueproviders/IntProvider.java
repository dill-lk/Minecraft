/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Either
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.DataResult
 */
package net.mayaan.util.valueproviders;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.util.RandomSource;
import net.mayaan.util.valueproviders.ConstantInt;
import net.mayaan.util.valueproviders.IntProviderType;

public abstract class IntProvider {
    private static final Codec<Either<Integer, IntProvider>> CONSTANT_OR_DISPATCH_CODEC = Codec.either((Codec)Codec.INT, (Codec)BuiltInRegistries.INT_PROVIDER_TYPE.byNameCodec().dispatch(IntProvider::getType, IntProviderType::codec));
    public static final Codec<IntProvider> CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(either -> (IntProvider)either.map(ConstantInt::of, f -> f), f -> f.getType() == IntProviderType.CONSTANT ? Either.left((Object)((ConstantInt)f).getValue()) : Either.right((Object)f));
    public static final Codec<IntProvider> NON_NEGATIVE_CODEC = IntProvider.codec(0, Integer.MAX_VALUE);
    public static final Codec<IntProvider> POSITIVE_CODEC = IntProvider.codec(1, Integer.MAX_VALUE);

    public static Codec<IntProvider> codec(int minValue, int maxValue) {
        return IntProvider.validateCodec(minValue, maxValue, CODEC);
    }

    public static <T extends IntProvider> Codec<T> validateCodec(int minValue, int maxValue, Codec<T> codec) {
        return codec.validate(value -> IntProvider.validate(minValue, maxValue, value));
    }

    private static <T extends IntProvider> DataResult<T> validate(int minValue, int maxValue, T value) {
        if (value.getMinValue() < minValue) {
            return DataResult.error(() -> "Value provider too low: " + minValue + " [" + value.getMinValue() + "-" + value.getMaxValue() + "]");
        }
        if (value.getMaxValue() > maxValue) {
            return DataResult.error(() -> "Value provider too high: " + maxValue + " [" + value.getMinValue() + "-" + value.getMaxValue() + "]");
        }
        return DataResult.success(value);
    }

    public abstract int sample(RandomSource var1);

    public abstract int getMinValue();

    public abstract int getMaxValue();

    public abstract IntProviderType<?> getType();
}

