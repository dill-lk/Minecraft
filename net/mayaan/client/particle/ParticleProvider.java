/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.particle;

import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.particle.Particle;
import net.mayaan.client.particle.SingleQuadParticle;
import net.mayaan.core.particles.ParticleOptions;
import net.mayaan.util.RandomSource;
import org.jspecify.annotations.Nullable;

public interface ParticleProvider<T extends ParticleOptions> {
    public @Nullable Particle createParticle(T var1, ClientLevel var2, double var3, double var5, double var7, double var9, double var11, double var13, RandomSource var15);

    public static interface Sprite<T extends ParticleOptions> {
        public @Nullable SingleQuadParticle createParticle(T var1, ClientLevel var2, double var3, double var5, double var7, double var9, double var11, double var13, RandomSource var15);
    }
}

