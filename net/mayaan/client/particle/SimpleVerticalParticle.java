/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.particle;

import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.particle.Particle;
import net.mayaan.client.particle.ParticleProvider;
import net.mayaan.client.particle.SingleQuadParticle;
import net.mayaan.client.particle.SpriteSet;
import net.mayaan.client.renderer.texture.TextureAtlasSprite;
import net.mayaan.core.particles.SimpleParticleType;
import net.mayaan.util.RandomSource;

public class SimpleVerticalParticle
extends SingleQuadParticle {
    private SimpleVerticalParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite, boolean upwards) {
        super(level, x, y, z, xa, ya, za, sprite);
        this.xd = xa;
        this.zd = za;
        this.yd = ya;
        this.gravity = 0.0f;
        this.yd += upwards ? 0.03 : -0.03;
        this.quadSize *= this.random.nextFloat() * 0.6f + 0.5f;
        this.lifetime = 8;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.OPAQUE;
    }

    public record ResetMobGrowthProvider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType>
    {
        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new SimpleVerticalParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random), true);
        }
    }

    public record PauseMobGrowthProvider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType>
    {
        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new SimpleVerticalParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random), false);
        }
    }
}

