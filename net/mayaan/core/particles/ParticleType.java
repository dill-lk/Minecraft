/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.core.particles;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.particles.ParticleOptions;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;

public abstract class ParticleType<T extends ParticleOptions> {
    private final boolean overrideLimiter;

    protected ParticleType(boolean overrideLimiter) {
        this.overrideLimiter = overrideLimiter;
    }

    public boolean getOverrideLimiter() {
        return this.overrideLimiter;
    }

    public abstract MapCodec<T> codec();

    public abstract StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec();
}

