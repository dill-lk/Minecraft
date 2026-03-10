/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.util.valueproviders;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.util.RandomSource;
import net.mayaan.util.random.Weighted;
import net.mayaan.util.random.WeightedList;
import net.mayaan.util.valueproviders.IntProvider;
import net.mayaan.util.valueproviders.IntProviderType;

public class WeightedListInt
extends IntProvider {
    public static final MapCodec<WeightedListInt> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeightedList.nonEmptyCodec(IntProvider.CODEC).fieldOf("distribution").forGetter(c -> c.distribution)).apply((Applicative)i, WeightedListInt::new));
    private final WeightedList<IntProvider> distribution;
    private final int minValue;
    private final int maxValue;

    public WeightedListInt(WeightedList<IntProvider> distribution) {
        this.distribution = distribution;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (Weighted<IntProvider> value : distribution.unwrap()) {
            int entryMin = value.value().getMinValue();
            int entryMax = value.value().getMaxValue();
            min = Math.min(min, entryMin);
            max = Math.max(max, entryMax);
        }
        this.minValue = min;
        this.maxValue = max;
    }

    @Override
    public int sample(RandomSource random) {
        return this.distribution.getRandomOrThrow(random).sample(random);
    }

    @Override
    public int getMinValue() {
        return this.minValue;
    }

    @Override
    public int getMaxValue() {
        return this.maxValue;
    }

    @Override
    public IntProviderType<?> getType() {
        return IntProviderType.WEIGHTED_LIST;
    }
}

