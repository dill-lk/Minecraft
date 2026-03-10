/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.particle;

import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.particle.Particle;
import net.mayaan.client.particle.ParticleProvider;
import net.mayaan.client.particle.SingleQuadParticle;
import net.mayaan.client.particle.SpriteSet;
import net.mayaan.core.particles.SimpleParticleType;
import net.mayaan.util.RandomSource;

public class HugeExplosionParticle
extends SingleQuadParticle {
    private final SpriteSet sprites;

    protected HugeExplosionParticle(ClientLevel level, double x, double y, double z, double size, SpriteSet sprites) {
        super(level, x, y, z, 0.0, 0.0, 0.0, sprites.first());
        float col;
        this.lifetime = 6 + this.random.nextInt(4);
        this.rCol = col = this.random.nextFloat() * 0.6f + 0.4f;
        this.gCol = col;
        this.bCol = col;
        this.quadSize = 2.0f * (1.0f - (float)size * 0.5f);
        this.sprites = sprites;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public int getLightCoords(float a) {
        return 0xF000F0;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.OPAQUE;
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new HugeExplosionParticle(level, x, y, z, xAux, this.sprites);
        }
    }
}

