/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.levelgen.heightproviders;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.util.RandomSource;
import net.mayaan.util.random.WeightedList;
import net.mayaan.world.level.levelgen.WorldGenerationContext;
import net.mayaan.world.level.levelgen.heightproviders.HeightProvider;
import net.mayaan.world.level.levelgen.heightproviders.HeightProviderType;

public class WeightedListHeight
extends HeightProvider {
    public static final MapCodec<WeightedListHeight> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeightedList.nonEmptyCodec(HeightProvider.CODEC).fieldOf("distribution").forGetter(c -> c.distribution)).apply((Applicative)i, WeightedListHeight::new));
    private final WeightedList<HeightProvider> distribution;

    public WeightedListHeight(WeightedList<HeightProvider> distribution) {
        this.distribution = distribution;
    }

    @Override
    public int sample(RandomSource random, WorldGenerationContext heightAccessor) {
        return this.distribution.getRandomOrThrow(random).sample(random, heightAccessor);
    }

    @Override
    public HeightProviderType<?> getType() {
        return HeightProviderType.WEIGHTED_LIST;
    }
}

