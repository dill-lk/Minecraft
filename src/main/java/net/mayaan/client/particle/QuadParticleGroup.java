/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.particle;

import net.mayaan.CrashReport;
import net.mayaan.CrashReportCategory;
import net.mayaan.ReportedException;
import net.mayaan.client.Camera;
import net.mayaan.client.particle.ParticleEngine;
import net.mayaan.client.particle.ParticleGroup;
import net.mayaan.client.particle.ParticleRenderType;
import net.mayaan.client.particle.SingleQuadParticle;
import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.state.level.ParticleGroupRenderState;
import net.mayaan.client.renderer.state.level.QuadParticleRenderState;

public class QuadParticleGroup
extends ParticleGroup<SingleQuadParticle> {
    private final ParticleRenderType particleType;
    final QuadParticleRenderState particleTypeRenderState = new QuadParticleRenderState();

    public QuadParticleGroup(ParticleEngine engine, ParticleRenderType particleType) {
        super(engine);
        this.particleType = particleType;
    }

    @Override
    public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTickTime) {
        for (SingleQuadParticle particle : this.particles) {
            if (!frustum.pointInFrustum(particle.x, particle.y, particle.z)) continue;
            try {
                particle.extract(this.particleTypeRenderState, camera, partialTickTime);
            }
            catch (Throwable throwable) {
                CrashReport report = CrashReport.forThrowable(throwable, "Rendering Particle");
                CrashReportCategory category = report.addCategory("Particle being rendered");
                category.setDetail("Particle", particle::toString);
                category.setDetail("Particle Type", this.particleType::toString);
                throw new ReportedException(report);
            }
        }
        return this.particleTypeRenderState;
    }
}

