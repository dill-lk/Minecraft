/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.state.level;

import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.state.level.CameraRenderState;

public interface ParticleGroupRenderState {
    public void submit(SubmitNodeCollector var1, CameraRenderState var2);

    default public void clear() {
    }
}

