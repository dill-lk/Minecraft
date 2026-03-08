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
import net.mayaan.core.BlockPos;
import net.mayaan.core.particles.SimpleParticleType;
import net.mayaan.tags.FluidTags;
import net.mayaan.util.RandomSource;

public class BubbleParticle
extends SingleQuadParticle {
    private BubbleParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        this.setSize(0.02f, 0.02f);
        this.quadSize *= this.random.nextFloat() * 0.6f + 0.2f;
        this.xd = xa * (double)0.2f + (double)((this.random.nextFloat() * 2.0f - 1.0f) * 0.02f);
        this.yd = ya * (double)0.2f + (double)((this.random.nextFloat() * 2.0f - 1.0f) * 0.02f);
        this.zd = za * (double)0.2f + (double)((this.random.nextFloat() * 2.0f - 1.0f) * 0.02f);
        this.lifetime = (int)(8.0 / ((double)this.random.nextFloat() * 0.8 + 0.2));
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
            return;
        }
        this.yd += 0.002;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= (double)0.85f;
        this.yd *= (double)0.85f;
        this.zd *= (double)0.85f;
        if (!this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(FluidTags.WATER)) {
            this.remove();
        }
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.OPAQUE;
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            BubbleParticle particle = new BubbleParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
            return particle;
        }
    }
}

