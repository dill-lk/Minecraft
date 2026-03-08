/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.particle;

import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.particle.NoRenderParticle;
import net.mayaan.client.particle.Particle;
import net.mayaan.client.particle.ParticleProvider;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.core.particles.SimpleParticleType;
import net.mayaan.util.RandomSource;

public class HugeExplosionSeedParticle
extends NoRenderParticle {
    private HugeExplosionSeedParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.lifetime = 8;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 6; ++i) {
            double xx = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
            double yy = this.y + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
            double zz = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
            this.level.addParticle(ParticleTypes.EXPLOSION, xx, yy, zz, (float)this.age / (float)this.lifetime, 0.0, 0.0);
        }
        ++this.age;
        if (this.age == this.lifetime) {
            this.remove();
        }
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new HugeExplosionSeedParticle(level, x, y, z);
        }
    }
}

