/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.particle;

import net.mayaan.client.Camera;
import net.mayaan.client.particle.NoRenderParticle;
import net.mayaan.client.particle.ParticleEngine;
import net.mayaan.client.particle.ParticleGroup;
import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.state.level.ParticleGroupRenderState;

public class NoRenderParticleGroup
extends ParticleGroup<NoRenderParticle> {
    private static final ParticleGroupRenderState EMPTY_RENDER_STATE = (ignored, camera) -> {};

    public NoRenderParticleGroup(ParticleEngine engine) {
        super(engine);
    }

    @Override
    public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTickTime) {
        return EMPTY_RENDER_STATE;
    }
}

