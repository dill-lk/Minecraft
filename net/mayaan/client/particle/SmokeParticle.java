/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.particle;

import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.particle.BaseAshSmokeParticle;
import net.mayaan.client.particle.Particle;
import net.mayaan.client.particle.ParticleProvider;
import net.mayaan.client.particle.SpriteSet;
import net.mayaan.core.particles.SimpleParticleType;
import net.mayaan.util.RandomSource;

public class SmokeParticle
extends BaseAshSmokeParticle {
    protected SmokeParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, float scale, SpriteSet sprites) {
        super(level, x, y, z, 0.1f, 0.1f, 0.1f, xa, ya, za, scale, sprites, 0.3f, 8, -0.1f, true);
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new SmokeParticle(level, x, y, z, xAux, yAux, zAux, 1.0f, this.sprites);
        }
    }
}

