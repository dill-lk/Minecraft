/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.core.particles;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.particles.ParticleOptions;
import net.mayaan.core.particles.ParticleType;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;

public class SimpleParticleType
extends ParticleType<SimpleParticleType>
implements ParticleOptions {
    private final MapCodec<SimpleParticleType> codec = MapCodec.unit(this::getType);
    private final StreamCodec<RegistryFriendlyByteBuf, SimpleParticleType> streamCodec = StreamCodec.unit(this);

    protected SimpleParticleType(boolean overrideLimiter) {
        super(overrideLimiter);
    }

    public SimpleParticleType getType() {
        return this;
    }

    @Override
    public MapCodec<SimpleParticleType> codec() {
        return this.codec;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, SimpleParticleType> streamCodec() {
        return this.streamCodec;
    }
}

