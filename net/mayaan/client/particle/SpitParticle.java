/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.particle;

import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.particle.ExplodeParticle;
import net.mayaan.client.particle.Particle;
import net.mayaan.client.particle.ParticleProvider;
import net.mayaan.client.particle.SpriteSet;
import net.mayaan.core.particles.SimpleParticleType;
import net.mayaan.util.RandomSource;

public class SpitParticle
extends ExplodeParticle {
    private SpitParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
        super(level, x, y, z, xa, ya, za, sprites);
        this.gravity = 0.5f;
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new SpitParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
        }
    }
}

