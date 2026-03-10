/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.particle;

import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.particle.HugeExplosionParticle;
import net.mayaan.client.particle.Particle;
import net.mayaan.client.particle.ParticleProvider;
import net.mayaan.client.particle.SpriteSet;
import net.mayaan.core.particles.SimpleParticleType;
import net.mayaan.util.RandomSource;

public class SonicBoomParticle
extends HugeExplosionParticle {
    protected SonicBoomParticle(ClientLevel level, double x, double y, double z, double size, SpriteSet sprites) {
        super(level, x, y, z, size, sprites);
        this.lifetime = 16;
        this.quadSize = 1.5f;
        this.setSpriteFromAge(sprites);
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new SonicBoomParticle(level, x, y, z, xAux, this.sprites);
        }
    }
}

