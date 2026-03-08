/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Either
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.levelgen.heightproviders;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.levelgen.VerticalAnchor;
import net.mayaan.world.level.levelgen.WorldGenerationContext;
import net.mayaan.world.level.levelgen.heightproviders.ConstantHeight;
import net.mayaan.world.level.levelgen.heightproviders.HeightProviderType;

public abstract class HeightProvider {
    private static final Codec<Either<VerticalAnchor, HeightProvider>> CONSTANT_OR_DISPATCH_CODEC = Codec.either(VerticalAnchor.CODEC, (Codec)BuiltInRegistries.HEIGHT_PROVIDER_TYPE.byNameCodec().dispatch(HeightProvider::getType, HeightProviderType::codec));
    public static final Codec<HeightProvider> CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(either -> (HeightProvider)either.map(ConstantHeight::of, f -> f), f -> f.getType() == HeightProviderType.CONSTANT ? Either.left((Object)((ConstantHeight)f).getValue()) : Either.right((Object)f));

    public abstract int sample(RandomSource var1, WorldGenerationContext var2);

    public abstract HeightProviderType<?> getType();
}

