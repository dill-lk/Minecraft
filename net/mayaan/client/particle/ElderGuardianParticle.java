/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.particle;

import net.mayaan.client.Mayaan;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.guardian.GuardianParticleModel;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.particle.Particle;
import net.mayaan.client.particle.ParticleProvider;
import net.mayaan.client.particle.ParticleRenderType;
import net.mayaan.client.renderer.entity.ElderGuardianRenderer;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.core.particles.SimpleParticleType;
import net.mayaan.util.RandomSource;

public class ElderGuardianParticle
extends Particle {
    protected final GuardianParticleModel model;
    protected final RenderType renderType = RenderTypes.entityTranslucent(ElderGuardianRenderer.GUARDIAN_ELDER_LOCATION);

    private ElderGuardianParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.model = new GuardianParticleModel(Mayaan.getInstance().getEntityModels().bakeLayer(ModelLayers.ELDER_GUARDIAN));
        this.gravity = 0.0f;
        this.lifetime = 30;
    }

    @Override
    public ParticleRenderType getGroup() {
        return ParticleRenderType.ELDER_GUARDIANS;
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new ElderGuardianParticle(level, x, y, z);
        }
    }
}

