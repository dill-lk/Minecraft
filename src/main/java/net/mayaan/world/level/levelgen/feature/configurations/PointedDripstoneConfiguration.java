/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class PointedDripstoneConfiguration
implements FeatureConfiguration {
    public static final Codec<PointedDripstoneConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.floatRange((float)0.0f, (float)1.0f).fieldOf("chance_of_taller_dripstone").orElse((Object)Float.valueOf(0.2f)).forGetter(c -> Float.valueOf(c.chanceOfTallerDripstone)), (App)Codec.floatRange((float)0.0f, (float)1.0f).fieldOf("chance_of_directional_spread").orElse((Object)Float.valueOf(0.7f)).forGetter(c -> Float.valueOf(c.chanceOfDirectionalSpread)), (App)Codec.floatRange((float)0.0f, (float)1.0f).fieldOf("chance_of_spread_radius2").orElse((Object)Float.valueOf(0.5f)).forGetter(c -> Float.valueOf(c.chanceOfSpreadRadius2)), (App)Codec.floatRange((float)0.0f, (float)1.0f).fieldOf("chance_of_spread_radius3").orElse((Object)Float.valueOf(0.5f)).forGetter(c -> Float.valueOf(c.chanceOfSpreadRadius3))).apply((Applicative)i, PointedDripstoneConfiguration::new));
    public final float chanceOfTallerDripstone;
    public final float chanceOfDirectionalSpread;
    public final float chanceOfSpreadRadius2;
    public final float chanceOfSpreadRadius3;

    public PointedDripstoneConfiguration(float chanceOfTallerDripstone, float chanceOfDirectionalSpread, float chanceOfSpreadRadius2, float chanceOfSpreadRadius3) {
        this.chanceOfTallerDripstone = chanceOfTallerDripstone;
        this.chanceOfDirectionalSpread = chanceOfDirectionalSpread;
        this.chanceOfSpreadRadius2 = chanceOfSpreadRadius2;
        this.chanceOfSpreadRadius3 = chanceOfSpreadRadius3;
    }
}

