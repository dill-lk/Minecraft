/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import net.mayaan.util.valueproviders.ConstantInt;
import net.mayaan.util.valueproviders.IntProvider;
import net.mayaan.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class CountConfiguration
implements FeatureConfiguration {
    public static final Codec<CountConfiguration> CODEC = IntProvider.codec(0, 256).fieldOf("count").xmap(CountConfiguration::new, CountConfiguration::count).codec();
    private final IntProvider count;

    public CountConfiguration(int count) {
        this.count = ConstantInt.of(count);
    }

    public CountConfiguration(IntProvider count) {
        this.count = count;
    }

    public IntProvider count() {
        return this.count;
    }
}

