/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.state.level;

import java.util.ArrayList;
import java.util.List;
import net.mayaan.client.renderer.SubmitNodeStorage;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.state.level.ParticleGroupRenderState;

public class ParticlesRenderState {
    public final List<ParticleGroupRenderState> particles = new ArrayList<ParticleGroupRenderState>();

    public void reset() {
        this.particles.forEach(ParticleGroupRenderState::clear);
        this.particles.clear();
    }

    public void add(ParticleGroupRenderState state) {
        this.particles.add(state);
    }

    public void submit(SubmitNodeStorage submitNodeStorage, CameraRenderState camera) {
        for (ParticleGroupRenderState particle : this.particles) {
            particle.submit(submitNodeStorage, camera);
        }
    }
}

