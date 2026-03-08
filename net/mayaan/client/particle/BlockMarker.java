/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.particle;

import net.mayaan.client.Mayaan;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.particle.Particle;
import net.mayaan.client.particle.ParticleProvider;
import net.mayaan.client.particle.SingleQuadParticle;
import net.mayaan.core.particles.BlockParticleOption;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.block.state.BlockState;

public class BlockMarker
extends SingleQuadParticle {
    private final SingleQuadParticle.Layer layer;

    private BlockMarker(ClientLevel level, double x, double y, double z, BlockState state) {
        super(level, x, y, z, Mayaan.getInstance().getModelManager().getBlockStateModelSet().getParticleMaterial(state).sprite());
        this.gravity = 0.0f;
        this.lifetime = 80;
        this.hasPhysics = false;
        this.layer = SingleQuadParticle.Layer.bySprite(this.sprite);
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return this.layer;
    }

    @Override
    public float getQuadSize(float a) {
        return 0.5f;
    }

    public static class Provider
    implements ParticleProvider<BlockParticleOption> {
        @Override
        public Particle createParticle(BlockParticleOption option, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new BlockMarker(level, x, y, z, option.getState());
        }
    }
}

